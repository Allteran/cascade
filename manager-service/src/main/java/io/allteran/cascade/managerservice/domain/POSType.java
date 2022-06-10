package io.allteran.cascade.managerservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "pos_type")
@Entity
@Getter
@Setter
public class POSType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String name;
}
