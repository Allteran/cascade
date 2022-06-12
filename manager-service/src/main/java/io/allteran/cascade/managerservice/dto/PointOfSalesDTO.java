package io.allteran.cascade.managerservice.dto;

import lombok.Data;

@Data
public class PointOfSalesDTO {
    private String id;
    private String region;
    private String city;
    private String address;
    private String shortName;
    private String typeId;
    private String typeName;
}
