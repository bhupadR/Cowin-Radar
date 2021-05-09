package cowin.appointment.finder.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class StateResponse {

    @JsonProperty("states")
    List<State> states;
    @JsonProperty("ttl")
    String total;


}
