package cowin.appointment.finder.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Center {

    @JsonProperty("center_id")
    public int centerId;
    public String name;
    public String address;
    @JsonProperty("state_name")
    public String stateName;
    @JsonProperty("district_name")
    public String districtName;
    @JsonProperty("block_name")
    public String blockName;
    public int pincode;
    public int lat;
    @JsonProperty("long")
    public int longitude;
    public String from;
    public String to;
    @JsonProperty("fee_type")
    public String feeType;
    public List<Session> sessions;
    public List<VaccineFees> vaccineFees;

}
