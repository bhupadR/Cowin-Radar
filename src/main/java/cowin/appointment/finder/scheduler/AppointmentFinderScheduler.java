package cowin.appointment.finder.scheduler;

import cowin.appointment.finder.entity.CenterEntity;
import cowin.appointment.finder.entity.MessageArchive;
import cowin.appointment.finder.entity.User;
import cowin.appointment.finder.repository.CenterRepositroy;
import cowin.appointment.finder.repository.MessageArchiveRepository;
import cowin.appointment.finder.repository.UserRepository;
import cowin.appointment.finder.response.Center;
import cowin.appointment.finder.response.CenterResponse;
import cowin.appointment.finder.service.CowinClientService;
import cowin.appointment.finder.service.EmailService;
import cowin.appointment.finder.service.TelegramClientService;
import cowin.appointment.finder.utility.MessageUtil;
import cowin.appointment.finder.utility.ResponseUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Log4j
public class AppointmentFinderScheduler {

    @Autowired
    CowinClientService cowinClientService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageArchiveRepository messageArchiveRepository;

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    @Autowired
    TelegramClientService telegramClientService;

    @Autowired
    ResponseUtil responseUtil;

    @Autowired
    EmailService emailService;

    @Autowired
    MessageUtil messageUtil;

    @Autowired
    CenterRepositroy centerRepositroy;

    @Value("${admin.number}")
    String chatId;

    @Scheduled(cron = "${appointment.scheduler.cron}")
    public void appointmentFinder(){

        log.info("scheduler started "+new Date());
        List<User> userList = userRepository.findAllByStatus(0);
        log.info( userList);
        if(!userList.isEmpty()){

            Map<String,List<User>> pincodeMap = userList.stream().collect(Collectors.groupingBy(User::getPincode));

            Map<String,List<User>> districtIdMap = userList.stream().collect(Collectors.groupingBy(User::getDistrictId));
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.DAY_OF_MONTH,0);

            for(Map.Entry<String,List<User>> entrySet : pincodeMap.entrySet()){
                try {
                    CenterResponse centerResponse = cowinClientService.calendarByPin(entrySet.getKey(), sdf.format(instance.getTime()));
                    processCalenderResponse(entrySet, centerResponse,"Vaccine Slot By Pin Code");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for(Map.Entry<String,List<User>> entrySet : districtIdMap.entrySet()){
                try {
                    CenterResponse centerResponse = cowinClientService.calendarByDistrict(entrySet.getKey(), sdf.format(instance.getTime()));
                    processCalenderResponse(entrySet,centerResponse,"Vaccine Slot By District Id");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            log.info("scheduler run completed ");
        }
    }

    private void processCalenderResponse(Map.Entry<String, List<User>> entrySet, CenterResponse centerResponse, String subject) {
        if(responseUtil.isCapacityAvailable(centerResponse)){
            List<User> youngUser = entrySet.getValue().stream().filter(user -> user.getEnquiryAge() < 45).
                    collect(Collectors.toList());
            if(!youngUser.isEmpty() && responseUtil.isCenterAvailableForYoungUser(centerResponse)){
                List<Center> centers = responseUtil.findAvailableCenters(centerResponse,18);
                log.info(" pin code ----------------- "+ entrySet.getKey());
                log.info( " young user ###############");
                log.info(centers);
                notifyUser(youngUser, centers,subject);
            }
            List<User> elderUser = entrySet.getValue().stream().filter(user -> user.getEnquiryAge() >= 45).
                    collect(Collectors.toList());
            if(!elderUser.isEmpty() && responseUtil.isCenterAvailableForElderUser(centerResponse)){
                List<Center> centers = responseUtil.findAvailableCenters(centerResponse,45);
                log.info(" pin code ----------------- "+ entrySet.getKey());
                log.info( " elder user ###############");
                log.info(centers);
                notifyUser(elderUser,centers,subject);
            }
        }else{
            log.info(" no available centers found ");
        }
    }

    @Transactional
    public void notifyUser(List<User> youngUser, List<Center> centers,String subject) {
        String message = messageUtil.createMessageTable(centers);
        String [] email = new String[youngUser.size()];
        youngUser.stream().filter(user -> null != user.getEmail()).
                map(User::getEmail).collect(Collectors.toList()).toArray(email);
        emailService.sendSimpleMessage(email,message, subject);
        youngUser.forEach(user -> user.setEmailCount(user.getEmailCount()+1));
        userRepository.saveAll(youngUser);
        List<MessageArchive> collect = youngUser.stream().map(user -> MessageArchive.builder().message(subject+"\n"+message).
                user(user).build()).
                collect(Collectors.toList());
        messageArchiveRepository.saveAll(collect);
        telegramClientService.sendTelegramText(message+" "+subject,youngUser);
        createCenter(centers);
        log.info(" notification triggered "+message);
    }


    private void createCenter(List<Center> centers){
        List<CenterEntity> centerEntities = centers.stream().map(center -> {
            return CenterEntity.builder().latitude(center.getLat()).longitude(center.longitude).name(center.name).
                    pincode(center.getPincode()).
                    isEighteenPlus(center.getSessions().stream().filter(session -> (session.getAvailableCapacity() > 0 &&
                            session.getMinAgeLimit() >= 18)).count() > 0).
                    isFortyFivePlus(center.getSessions().stream().filter(session -> (session.getAvailableCapacity() > 0 &&
                            session.getMinAgeLimit() >= 45)).count() > 0).address(center.getAddress()).build();
        }).collect(Collectors.toList());
        List<CenterEntity> centerRepositroyAll = centerRepositroy.findAll();
        centerEntities.removeAll(centerRepositroyAll);
        if(!centerEntities.isEmpty()){
            log.info(" adding new center "+centerEntities);
            centerRepositroy.saveAll(centerEntities);
            telegramClientService.sendMessageToAdmin(" new center added "+centerEntities.stream().map(CenterEntity::getName)
                    .collect(Collectors.toList()),chatId);
        }
    }
}
