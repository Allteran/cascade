package io.allteran.cascade.managerservice.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrganizationDTO implements Serializable {
    private String id;
    private String inn;
    private String name;

}
