package io.allteran.cascade.managerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {
    private String message;
    private List<EmployeeDTO> employeeDTOList;
}
