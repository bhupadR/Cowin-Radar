package cowin.appointment.finder.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class District {

    @JsonProperty("district_id")
    int districtId;
    @JsonProperty("district_name")
    String districtName;
}
