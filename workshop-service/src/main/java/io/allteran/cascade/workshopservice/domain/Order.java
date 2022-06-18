package io.allteran.cascade.workshopservice.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Document(value = "order")
public class Order {
    @Id
    private String id;
    private String deviceName;

    private DeviceType deviceType;
    private Status status;

    private String serialNumber;
    private String defect;
    private String equipSet;
    private String appearance;
    private String warranty;

    private String performedActions;

    private LocalDate creationDate;
    private LocalDate issueDate;

    private double preliminaryPrice;
    private double servicePrice;
    private double componentPrice;
    private double marginPrice;
    private double totalPrice;

    private double engineerRate;

    private double directorProfit;
    private double headEngineerProfit;
    private double engineerProfit;
    private double managerProfit;
    private double employeeProfit;

    private String customerName;
    private String customerPhone;

    private String posId;

    private String authorId;

    private String engineerId;
    private String engineerRole;
}

