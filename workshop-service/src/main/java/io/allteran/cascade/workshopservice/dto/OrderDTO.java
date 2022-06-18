package io.allteran.cascade.workshopservice.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class OrderDTO {
    private String id;
    private String deviceName;

    private String deviceTypeId;
    private String deviceTypeName;

    private String statusId;
    private String statusName;

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
    private String posNickname;

    private String authorId;
    private String authorName;

    private String engineerId;
    private String engineerName;
    private String engineerRole;
}
