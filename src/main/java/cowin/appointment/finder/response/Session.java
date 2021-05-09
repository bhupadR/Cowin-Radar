package cowin.appointment.finder.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Session{
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
    public String from;
    public String to;
    @JsonProperty("lat")
    public int lattitude;
    @JsonProperty("long")
    public int longitude;
    @JsonProperty("fee_type")
    public String feeType;
    @JsonProperty("session_id")
    public String sessionId;
    public String date;
    @JsonProperty("available_capacity")
    public int availableCapacity;
    public String fee;
    @JsonProperty("min_age_limit")
    public int minAgeLimit;
    public String vaccine;
    public List<String> slots;
}