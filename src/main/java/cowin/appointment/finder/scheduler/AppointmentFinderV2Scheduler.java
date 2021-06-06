package cowin.appointment.finder.scheduler;

import cowin.appointment.finder.processor.CenterMessage;
import cowin.appointment.finder.processor.CowinResponseProcessor;
import cowin.appointment.finder.response.Center;
import cowin.appointment.finder.response.Session;
import cowin.appointment.finder.service.CowinAuthClientService;
import cowin.appointment.finder.service.TelegramClientService;
import cowin.appointment.finder.utility.SlotFinder;
import lombok.extern.log4j.Log4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Log4j
public class AppointmentFinderV2Scheduler {

    @Autowired
    CowinAuthClientService cowinAuthClientService;

    @Autowired
    TelegramClientService telegramClientService;

    @Value("${admin.number}")
    String adminNumber;

    @Value("#{'${list.of.numbers}'.split(',')}")
    List<String> listOfNumbers;

    @Value("${admin.phone.number}")
    String phoneNumber;

    @Autowired
    CowinResponseProcessor cowinResponseProcessor;

    @Autowired
    SlotFinder slotFinder;

    String puneDistrict ="363";

    @Value("${beneficiary.id.bhupad}")
    String bhupadBeneficiaryId;

    @Value("${beneficiary.id.nitin}")
    String nitinBeneficiaryId;

    @Value("#{'${beneficiary.id.list}'.split(',')}")
    private List<String> beneficiariesList;

    int counter = 0;

    @Scheduled(cron = "${appointment.private.scheduler.cron}")
    public void appointmentFinder(){
        if(null == cowinAuthClientService.getToken()){
            generateToken();
            if(null == cowinAuthClientService.getToken()){
                System.out.println(" Failed to generate token ");
                return;
            }
        }
        try {
            Response centerBySessionDistrictResponse = cowinAuthClientService.findCenterBySessionDistrict(puneDistrict);
            counter++;
            if(centerBySessionDistrictResponse.code() == 200) {
                String centerBySessionDistrictStringResponse = centerBySessionDistrictResponse.body().string();
                centerBySessionDistrictResponse.close();
                List<Center> availableCentersAndSessions = cowinResponseProcessor.findAvailableCentersAndSessions(centerBySessionDistrictStringResponse);
                if (!availableCentersAndSessions.isEmpty()) {
                    // for age 18+ dose1
                    List<Center> availableCentersByAge = slotFinder.findAvailableCentersByAge(availableCentersAndSessions, 25);
                    if(!availableCentersByAge.isEmpty()){
                        List<Center> centerForDose1 = slotFinder.findCenterForDose1(availableCentersByAge);
                        if(!centerForDose1.isEmpty()) {
                            List<CenterMessage> centerMessages = centerForDose1.stream().map(center -> CenterMessage.builder().name(center.getName()).
                                    date(center.getSessions().stream().map(Session::getDate).collect(Collectors.toList())).build()).
                                    collect(Collectors.toList());
                            System.out.println("18+ centers found: "+centerMessages);
                            telegramClientService.sendTelegramMessageToNumbers("Center available for 18+ : "+centerMessages, listOfNumbers);
                            centerForDose1.forEach(center -> {
                                try {
                                    Response response = cowinAuthClientService.bookSlot(center, 1, beneficiariesList);
                                    if(response.code() == 200){
                                        String responseBody = response.body().string();
                                        System.out.println(" Vaccine Scheduled response "+responseBody);
                                        telegramClientService.sendTelegramMessageToNumbers(" Vaccine Booked "+responseBody,listOfNumbers);
                                    }else {
                                        System.out.println(response.body().string());
                                    }
                                    response.close();

                                } catch (IOException exception) {
                                    exception.printStackTrace();
                                }
                            });
                        }
                    }

                    /*Response centerBySessionDistrictAndDateResponse = cowinAuthClientService.
                            findCenterBySessionDistrictByDate("24-06-2021", puneDistrict);
                    counter++;
                    if(centerBySessionDistrictAndDateResponse.code() == 200) {
                        //for age 45+ dose2
                        List<Center> availableCentersAndSessionsByDate = cowinResponseProcessor.findAvailableCentersAndSessions(centerBySessionDistrictAndDateResponse.body().string());
                        centerBySessionDistrictAndDateResponse.close();
                        if(!availableCentersAndSessionsByDate.isEmpty()) {
                            List<Center> availableCentersByOlderAge = slotFinder.findAvailableCentersByAge(availableCentersAndSessionsByDate, 46);
                            if (!availableCentersByOlderAge.isEmpty()) {
                                List<Center> centerForDose2 = slotFinder.findCenterForDose2(availableCentersByOlderAge);
                                if (!centerForDose2.isEmpty()) {
                                    List<CenterMessage> centerMessages = centerForDose2.stream().map(center -> CenterMessage.builder().name(center.getName()).
                                            date(center.getSessions().stream().map(Session::getDate).collect(Collectors.toList())).build()).
                                            collect(Collectors.toList());
                                    System.out.println("45+ centers found: "+centerMessages);
                                    telegramClientService.sendTelegramMessageToNumbers("Center available for 45+ : " + centerMessages, listOfNumbers);
                                }
                            }
                        }
                    }else {
                        cowinAuthClientService.setToken(null);
                    }*/
                }
            }else {
                cowinAuthClientService.setToken(null);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void generateToken(){
        System.out.println(" number of request processed "+counter);
        counter = 0;
        String token = cowinAuthClientService.authenticateAndToken(phoneNumber);
        if(token == null){
            telegramClientService.sendMessageToAdmin("private authentication failed",adminNumber);
        }
    }
}
