package io.allteran.cascade.workshopservice.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class StatusDTO implements Serializable {
    private String id;
    private String name;
}
