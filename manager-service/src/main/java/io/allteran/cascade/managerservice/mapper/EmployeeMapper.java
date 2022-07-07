package io.allteran.cascade.managerservice.mapper;

import io.allteran.cascade.managerservice.domain.Employee;
import io.allteran.cascade.managerservice.domain.Organization;
import io.allteran.cascade.managerservice.domain.Role;
import io.allteran.cascade.managerservice.dto.EmployeeDTO;
import io.allteran.cascade.managerservice.service.OrganizationService;
import io.allteran.cascade.managerservice.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

public class EmployeeMapper {

    public static EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setPhone(employee.getPhone());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setPassword(employee.getPassword());
        dto.setPasswordConfirm(employee.getPasswordConfirm());
        dto.setNewPassword(employee.getNewPassword());
        dto.setToken(employee.getToken());

        dto.setOrganizationId(employee.getOrganization().getId());
        dto.setOrganizationName(employee.getOrganization().getName());

        dto.setCreationDate(employee.getCreationDate());
        dto.setHireDate(employee.getHireDate());
        dto.setDismissalDate(employee.getDismissalDate());

        Set<String> roles = employee.getRoles().stream().map(Role::getId).collect(Collectors.toSet());
        dto.setRoles(roles);

        return dto;
    }

    public static Employee convertToEntity(EmployeeDTO dto, OrganizationService organizationService, RoleService roleService) {
        Employee entity = new Employee();

        entity.setId(dto.getId());
        entity.setPhone(dto.getPhone());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPassword(dto.getPassword());
        entity.setPasswordConfirm(dto.getPasswordConfirm());
        entity.setNewPassword(dto.getNewPassword());
        entity.setToken(dto.getToken());

        Organization organization = organizationService.findById(dto.getOrganizationId());
        entity.setOrganization(organization);
        entity.setCreationDate(dto.getCreationDate());
        entity.setHireDate(dto.getHireDate());
        entity.setDismissalDate(dto.getDismissalDate());

        Set<Role> roles = dto.getRoles().stream().map(roleService::findById).collect(Collectors.toSet());
        entity.setRoles(roles);

        return entity;
    }
}
