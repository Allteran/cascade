package io.allteran.cascade.managerservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Table(name = "employee")
@Entity
@Getter
@Setter
public class Employee {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    private String phone;
    private String firstName;
    private String lastName;

    private String password;
    @Transient
    private String passwordConfirm;
    @Transient
    private String newPassword;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    private boolean active;
    private LocalDateTime creationDate;
    private LocalDateTime hireDate;
    private LocalDateTime dismissalDate;

    @ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinTable(name="employee_roles",
            joinColumns = {@JoinColumn(name="user_id", referencedColumnName="id")},
            inverseJoinColumns = {@JoinColumn(name="role_id", referencedColumnName="id")}
    )
    private Set<Role> roles;
}
