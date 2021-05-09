package cowin.appointment.finder.utility;

import cowin.appointment.finder.response.Center;
import cowin.appointment.finder.response.CenterResponse;
import cowin.appointment.finder.response.Session;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResponseUtil {

    public boolean isCapacityAvailable(CenterResponse centerResponse){
        if(centerResponse == null){
            return false;
        }else {
            List<Center> centers = centerResponse.getCenters().stream().filter(center -> {
                List<Session> sessions = center.getSessions().stream().
                        filter(session -> session.getAvailableCapacity() > 0).collect(Collectors.toList());
                return !sessions.isEmpty();
            }).collect(Collectors.toList());
            return !centers.isEmpty();
        }
    }

    public boolean isCenterAvailableForYoungUser(CenterResponse centerResponse){
        if(centerResponse == null){
            return false;
        }else {
            List<Center> centers = centerResponse.getCenters().stream().filter(center -> {
                List<Session> sessions = center.getSessions().stream().filter(session -> (session.getAvailableCapacity()>0 &&
                        session.getMinAgeLimit() >= 18 && session.getMinAgeLimit()<45)).collect(Collectors.toList());
                return !sessions.isEmpty();
            }).collect(Collectors.toList());
            return !centers.isEmpty();
        }
    }

    public boolean isCenterAvailableForElderUser(CenterResponse centerResponse) {
        if(centerResponse == null){
            return false;
        }else {
            List<Center> centers = centerResponse.getCenters().stream().filter(center -> {
                List<Session> sessions = center.getSessions().stream().filter(session -> ( session.getAvailableCapacity() >0 &&
                        session.getMinAgeLimit() >= 18)).collect(Collectors.toList());
                return !sessions.isEmpty();
            }).collect(Collectors.toList());
            return !centers.isEmpty();
        }
    }

    public List<Center> findAvailableCenters(CenterResponse centerResponse, int age) {
        if(centerResponse == null){
            return Collections.emptyList();
        }else {
            List<Center> centers = new ArrayList<>();
            centerResponse.getCenters().forEach(center -> {
                List<Session> sessions = center.getSessions().stream().filter(session -> (session.getAvailableCapacity() > 0 && session.getMinAgeLimit() <= age))
                        .collect(Collectors.toList());
                if(!sessions.isEmpty()) {
                    center.setSessions(sessions);
                    centers.add(center);
                }
            });
            return centers;
        }
    }
}
