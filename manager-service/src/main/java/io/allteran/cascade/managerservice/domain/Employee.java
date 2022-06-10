package io.allteran.cascade.managerservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Table(name = "employee")
@Entity
@Getter
@Setter
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String phone;
    private String firstName;
    private String lastName;

    private String password;
    @Transient
    private String passwordConfirm;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    private boolean active;
    private LocalDateTime creationDate;
    private LocalDateTime hireDate;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "employee_role", joinColumns = @JoinColumn(name = "employee_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
}
