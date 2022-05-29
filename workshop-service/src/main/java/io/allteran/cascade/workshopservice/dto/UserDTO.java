package io.allteran.cascade.workshopservice.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private RoleDTO role;
}
