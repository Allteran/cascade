package io.allteran.cascade.managerservice.controller;

import io.allteran.cascade.managerservice.domain.Employee;
import io.allteran.cascade.managerservice.domain.Role;
import io.allteran.cascade.managerservice.dto.CredentialsDTO;
import io.allteran.cascade.managerservice.dto.EmployeeDTO;
import io.allteran.cascade.managerservice.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signIn")
    public ResponseEntity<EmployeeDTO> signIn(@RequestBody CredentialsDTO credentialsDTO) {
        log.info("Trying to login {}", credentialsDTO.getLogin());
        Employee signedInUser = authService.signIn(credentialsDTO);
        return ResponseEntity.ok(convertToDTO(signedInUser));
    }

    @PostMapping("/validateToken")
    public ResponseEntity<EmployeeDTO> validateToken(@RequestParam String token) {
        log.info("Trying to validate token {}", token);
        Employee validatedTokenUser = authService.validateToken(token);
        return ResponseEntity.ok(convertToDTO(validatedTokenUser));
    }


    private EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setPhone(employee.getPhone());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setPassword(employee.getPassword());
        dto.setPasswordConfirm(employee.getPasswordConfirm());
        dto.setNewPassword(employee.getNewPassword());

        dto.setOrganizationId(employee.getOrganization().getId());
        dto.setOrganizationName(employee.getOrganization().getName());

        dto.setCreationDate(employee.getCreationDate());
        dto.setHireDate(employee.getHireDate());
        dto.setDismissalDate(employee.getDismissalDate());

        Set<String> roles = employee.getRoles().stream().map(Role::getId).collect(Collectors.toSet());
        dto.setRoles(roles);

        return dto;
    }
}

