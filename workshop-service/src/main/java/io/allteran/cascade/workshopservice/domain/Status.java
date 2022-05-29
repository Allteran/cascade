package io.allteran.cascade.workshopservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("status")
@Getter
@Setter
public class Status {
    @Id
    private String id;
    private String name;
}
