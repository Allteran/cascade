package io.allteran.cascade.workshopservice.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Data
public class OrderDTO implements Serializable {
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
    private String posShortName;

    private String authorId;
    private String authorName;

    private String engineerId;
    private String engineerName;
    private Set<String> engineerRoles;
}
