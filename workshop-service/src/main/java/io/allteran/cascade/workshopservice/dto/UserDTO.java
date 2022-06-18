package io.allteran.cascade.workshopservice.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String phone;
    private String roleId;
    private String roleName;
}
