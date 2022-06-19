package io.allteran.cascade.workshopservice.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
public class DeviceTypeDTO implements Serializable {
    private String id;
    private String name;
}
