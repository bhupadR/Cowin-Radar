package cowin.appointment.finder.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
class State {
    @JsonProperty("state_id")
    int stateId;

    @JsonProperty("state_name")
    String stateName;
}
