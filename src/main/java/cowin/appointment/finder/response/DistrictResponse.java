package cowin.appointment.finder.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DistrictResponse {

    @JsonProperty("districts")
    List<District> districts;

    @JsonProperty("ttl")
    String total;
}
