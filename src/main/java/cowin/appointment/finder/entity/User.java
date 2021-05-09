package cowin.appointment.finder.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Data
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "email")
    private String email;
    @Column(name = "enquiry_age")
    private int enquiryAge;
    @Column(name = "pincode")
    private String pincode;
    @Column(name = "disctrict_id")
    private String districtId;
    @Column(name = "state_name")
    private String stateName;
    @Column(name = "state_id")
    private String stateId;
    @Column(name = "email_count",nullable = false)
    private int emailCount;
    @Column(name = "status")
    private int status;
}
