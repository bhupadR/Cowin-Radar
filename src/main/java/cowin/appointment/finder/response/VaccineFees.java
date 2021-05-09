package cowin.appointment.finder.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VaccineFees {

    @JsonProperty("vaccine")
    String vaccineName;
    @JsonProperty("fee")
    String fee;
}
