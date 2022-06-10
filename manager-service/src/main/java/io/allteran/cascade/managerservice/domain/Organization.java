package io.allteran.cascade.managerservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "organization")
@Entity
@Getter
@Setter
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String inn;
    private String name;
}
