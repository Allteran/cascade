package io.allteran.cascade.workshopservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class POSResponse {
    private String message;
    private List<PointOfSalesDTO> posList;
}
