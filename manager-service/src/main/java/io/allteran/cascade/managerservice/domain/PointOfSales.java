package io.allteran.cascade.managerservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Table(name = "point_of_sales")
@Entity
@Getter
@Setter
public class PointOfSales {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    private String region;
    private String city;
    private String address;
    private String shortName;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id")
    private POSType type;
}
