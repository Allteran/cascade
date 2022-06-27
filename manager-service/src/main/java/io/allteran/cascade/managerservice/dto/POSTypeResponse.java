package io.allteran.cascade.managerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class POSTypeResponse {
    private String message;
    private List<POSTypeDTO> posTypeList;
}
