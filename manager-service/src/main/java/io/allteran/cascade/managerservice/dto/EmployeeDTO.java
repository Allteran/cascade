package io.allteran.cascade.managerservice.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class EmployeeDTO implements Serializable {
    private String id;
    private String phone;
    private String token;
    private String firstName;
    private String lastName;
    private String password;
    private String passwordConfirm;
    private String newPassword;
    private String organizationId;
    private String organizationName;
    private boolean active;
    private LocalDateTime creationDate;
    private LocalDateTime hireDate;
    private LocalDateTime dismissalDate;
    private Set<String> roles;
}
