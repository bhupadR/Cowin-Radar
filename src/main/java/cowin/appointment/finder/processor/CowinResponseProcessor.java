package cowin.appointment.finder.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cowin.appointment.finder.response.Center;
import cowin.appointment.finder.response.CenterResponse;
import cowin.appointment.finder.response.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CowinResponseProcessor {

    @Autowired
    ObjectMapper objectMapper;

    public List<Center> findAvailableCentersAndSessions(String centerBySessionDistrictResponse) throws JsonProcessingException {
        CenterResponse centerResponse = objectMapper.readValue(centerBySessionDistrictResponse, CenterResponse.class);
        List<Center> availableCenters = centerResponse.getCenters().stream().filter(center ->
                center.getSessions().stream().anyMatch(session -> session.getAvailableCapacity() > 0)).
                collect(Collectors.toList());
        availableCenters.forEach(center -> {
            List<Session> availableSessions = center.getSessions().stream().filter(session -> session.getAvailableCapacity() > 0).collect(Collectors.toList());
            center.setSessions(availableSessions);
        });
        return availableCenters;
    }
}
