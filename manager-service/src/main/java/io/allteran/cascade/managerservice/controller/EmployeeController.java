package io.allteran.cascade.managerservice.controller;

import io.allteran.cascade.managerservice.domain.Employee;
import io.allteran.cascade.managerservice.domain.Organization;
import io.allteran.cascade.managerservice.domain.Role;
import io.allteran.cascade.managerservice.dto.EmployeeDTO;
import io.allteran.cascade.managerservice.service.EmployeeService;
import io.allteran.cascade.managerservice.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/manage/employee")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final OrganizationService organizationService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, OrganizationService organizationService) {
        this.employeeService = employeeService;
        this.organizationService = organizationService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<EmployeeDTO>> findAll() {
        List<Employee> employeeList = employeeService.findAll();
        if(employeeList != null && !employeeList.isEmpty()) {
            List<EmployeeDTO> employeeDTOList = employeeList.stream().map(this::convertToDTO).toList();
            return new ResponseEntity<>(employeeDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/query")
    public ResponseEntity<List<EmployeeDTO>> findByQuery(@RequestParam List<String> id) {
        List<Employee> employeeList = employeeService.findAllById(id);
        if(employeeList != null) {
            List<EmployeeDTO> dtoList = employeeList.stream().map(this::convertToDTO).toList();
            return new ResponseEntity<>(dtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<EmployeeDTO> findById(@PathVariable("id") String id) {
        Employee employee = employeeService.findById(id);
        if(employee != null) {
            EmployeeDTO dto = convertToDTO(employee);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<EmployeeDTO> create(@RequestBody EmployeeDTO dto) {
        Employee employee = convertToEntity(dto);
        Employee createdEmployee = employeeService.create(employee);
        if(createdEmployee != null && !createdEmployee.getId().isEmpty()) {
            return new ResponseEntity<>(convertToDTO(createdEmployee), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EmployeeDTO> update(@PathVariable("id") String id,
                                              @RequestBody EmployeeDTO employeeDTO) {
        Employee updatedEmployee = employeeService.update(employeeService.findById(id), convertToEntity(employeeDTO));
        if(updatedEmployee != null && !updatedEmployee.getId().isEmpty()) {
            return new ResponseEntity<>(convertToDTO(updatedEmployee), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") String id) {
        employeeService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
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

        Set<String> roles = employee.getRoles().stream().map(Enum::name).collect(Collectors.toSet());
        dto.setRoles(roles);

        return dto;
    }

    private Employee convertToEntity(EmployeeDTO dto) {
        Employee entity = new Employee();

        entity.setId(dto.getId());
        entity.setPhone(dto.getPhone());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPassword(dto.getPassword());
        entity.setPasswordConfirm(dto.getPasswordConfirm());
        entity.setNewPassword(dto.getNewPassword());

        Organization organization = organizationService.findById(dto.getOrganizationId());
        entity.setOrganization(organization);
        entity.setCreationDate(dto.getCreationDate());
        entity.setHireDate(dto.getHireDate());
        entity.setDismissalDate(dto.getDismissalDate());

        Set<Role> roles = dto.getRoles().stream().map(Role::valueOf).collect(Collectors.toSet());
        entity.setRoles(roles);

        return entity;
    }
}
