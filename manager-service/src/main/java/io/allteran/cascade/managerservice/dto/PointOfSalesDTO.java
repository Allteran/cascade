package io.allteran.cascade.managerservice.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PointOfSalesDTO implements Serializable {
    private String id;
    private String region;
    private String city;
    private String address;
    private String shortName;
    private String typeId;
    private String typeName;
}
