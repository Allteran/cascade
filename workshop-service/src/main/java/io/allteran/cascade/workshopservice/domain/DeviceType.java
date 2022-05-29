package io.allteran.cascade.workshopservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document("device_type")
public class DeviceType {
    @Id
    private String id;
    private String name;
}

