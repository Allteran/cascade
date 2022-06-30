package io.allteran.cascade.apigateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO implements Serializable {
    private String id;
    private String phone;
    private String firstName;
    private String lastName;
    private String token;
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
