package cowin.appointment.finder.utility;

import cowin.appointment.finder.response.Center;
import cowin.appointment.finder.response.Session;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SlotFinder {

    public List<Center> findAvailableCentersByAge(List<Center> availableCenters, Integer age){
        List<Center> availableCentersByAge = availableCenters.stream().filter(center ->
                center.getSessions().stream().anyMatch(session -> session.getMinAgeLimit() <= age)).
                collect(Collectors.toList());
        availableCentersByAge.forEach(center -> {
            List<Session> availableSessionByAge = center.getSessions().stream().filter(session ->
                    session.getAvailableCapacity() > 0 && session.getMinAgeLimit() <= age).collect(Collectors.toList());
            center.setSessions(availableSessionByAge);
        });
        return availableCentersByAge;
    }

    public List<Center> findCenterForDose1(List<Center> centers){
        List<Center> availableDose1Centers = centers.stream().filter(center -> center.getSessions().stream().
                anyMatch(session -> session.getAvailableCapacityDose1() > 0)).collect(Collectors.toList());
        availableDose1Centers.forEach(center -> {
            List<Session> availableSessionDose1 = center.getSessions().stream().filter(session ->
                    session.getAvailableCapacityDose1() > 0).collect(Collectors.toList());
            center.setSessions(availableSessionDose1);
        });
        return availableDose1Centers;
    }

    public List<Center> findCenterForDose2(List<Center> availableCentersByOlderAge) {
        List<Center> availableDose2Centers = availableCentersByOlderAge.stream().filter(center -> center.getSessions().stream().
                anyMatch(session -> session.getAvailableCapacityDose2() > 0)).collect(Collectors.toList());
        availableDose2Centers.forEach(center -> {
            List<Session> availableSessionDose2 = center.getSessions().stream().filter(session ->
                    session.getAvailableCapacityDose2() > 0).collect(Collectors.toList());
            center.setSessions(availableSessionDose2);
        });
        return availableDose2Centers;
    }
}
