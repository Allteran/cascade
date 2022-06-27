package io.allteran.cascade.managerservice.dto;

import io.allteran.cascade.managerservice.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {
    private String message;
    private List<RoleDTO> roles;
}
