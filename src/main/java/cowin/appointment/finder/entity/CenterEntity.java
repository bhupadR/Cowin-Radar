package cowin.appointment.finder.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "center")
@Data
@Builder
@EqualsAndHashCode(of = {"name","pincode"})
@NoArgsConstructor
@AllArgsConstructor
public class CenterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name",unique = true,nullable = false)
    private String name;
    @Column(name = "address")
    private String address;
    @Column(name = "pincode")
    private Integer pincode;
    @Column(name = "distance",columnDefinition = "varchar(255) default NULL")
    private String distance;
    @Column(name="unit")
    private String unit;
    @Column(name = "latitude")
    private Integer latitude;
    @Column(name = "longitude")
    private Integer longitude;
    @Column(name = "is_eighteen_plus")
    private Boolean isEighteenPlus;
    @Column(name = "is_forty_five_plus")
    private Boolean isFortyFivePlus;
    @Column(name = "map_link",columnDefinition = "varchar(1000)")
    private String mapLink;
}
