package io.allteran.cascade.workshopservice.dto;

import io.allteran.cascade.workshopservice.domain.DeviceType;
import io.allteran.cascade.workshopservice.domain.Status;

import java.time.LocalDate;

public class OrderDTO {
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

    private String customerName;
    private String customerPhone;

    private String author;
    private String engineer;
}
