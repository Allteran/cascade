package io.allteran.cascade.managerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationResponse {
    private String message;
    private List<OrganizationDTO> orgList;
}
