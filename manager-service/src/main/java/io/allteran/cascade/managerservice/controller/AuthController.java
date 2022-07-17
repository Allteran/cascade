package io.allteran.cascade.managerservice.controller;

import io.allteran.cascade.managerservice.domain.Employee;
import io.allteran.cascade.managerservice.dto.CredentialsDTO;
import io.allteran.cascade.managerservice.dto.EmployeeDTO;
import io.allteran.cascade.managerservice.dto.EmployeeResponse;
import io.allteran.cascade.managerservice.mapper.EmployeeMapper;
import io.allteran.cascade.managerservice.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("api/v1/auth")
//@CrossOrigin(origins = "http://localhost:8100")
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
        return ResponseEntity.ok(EmployeeMapper.convertToDTO(authService.signIn(credentialsDTO)));
    }

    @PostMapping("/validateToken")
    public ResponseEntity<EmployeeDTO> validateToken(@RequestParam String token) {
        log.info("Trying to validate token {}", token);
        Employee validatedTokenUser = authService.validateToken(token);
        return ResponseEntity.ok(EmployeeMapper.convertToDTO(validatedTokenUser));
    }
}

