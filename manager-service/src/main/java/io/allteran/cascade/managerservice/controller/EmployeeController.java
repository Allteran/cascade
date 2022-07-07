package io.allteran.cascade.managerservice.controller;

import io.allteran.cascade.managerservice.domain.Employee;
import io.allteran.cascade.managerservice.domain.Role;
import io.allteran.cascade.managerservice.dto.EmployeeDTO;
import io.allteran.cascade.managerservice.dto.EmployeeResponse;
import io.allteran.cascade.managerservice.exception.UserFieldException;
import io.allteran.cascade.managerservice.mapper.EmployeeMapper;
import io.allteran.cascade.managerservice.service.EmployeeService;
import io.allteran.cascade.managerservice.service.OrganizationService;
import io.allteran.cascade.managerservice.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/v1/manage/employee")
//@CrossOrigin(origins = "http://localhost:8100")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final OrganizationService organizationService;
    private final RoleService roleService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, OrganizationService organizationService, RoleService roleService) {
        this.employeeService = employeeService;
        this.organizationService = organizationService;
        this.roleService = roleService;
    }

    @GetMapping("/list")
    public ResponseEntity<EmployeeResponse> findAll() {
        List<Employee> employeeList = employeeService.findAll();
        if(employeeList != null && !employeeList.isEmpty()) {
            List<EmployeeDTO> employeeDTOList = employeeList.stream().map(EmployeeMapper::convertToDTO).toList();
            return new ResponseEntity<>(new EmployeeResponse("OK", employeeDTOList), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new EmployeeResponse("List of employees is empty", Collections.emptyList()), HttpStatus.OK);
        }
    }

    @GetMapping("/search/id-list/")
    public ResponseEntity<EmployeeResponse> findAllByIdList(@RequestParam List<String> id) {
        List<Employee> employeeList = employeeService.findAllById(id);
        if(employeeList != null) {
            List<EmployeeDTO> dtoList = employeeList.stream().map(EmployeeMapper::convertToDTO).toList();
            return new ResponseEntity<>(new EmployeeResponse("OK", dtoList), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new EmployeeResponse("List of employees is empty", Collections.emptyList()), HttpStatus.OK);
        }
    }
    @GetMapping("/search/role")
    public ResponseEntity<EmployeeResponse> findAllByRoles(@RequestParam List<String> role) {
        List<Role> roleList = role.stream().map(roleService::findById).toList();
        List<Employee> employeeList = employeeService.findAllByRoles(roleList);
        if(employeeList != null && !employeeList.isEmpty()) {
            List<EmployeeDTO> dtos = employeeList.stream().map(EmployeeMapper::convertToDTO).toList();
            return new ResponseEntity<>(new EmployeeResponse("OK", dtos), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new EmployeeResponse("List of employee with specified roles is empty", Collections.emptyList()), HttpStatus.OK);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<EmployeeResponse> findById(@PathVariable("id") String id) {
        Employee employee = employeeService.findById(id);
        if(employee != null) {
            EmployeeDTO dto = EmployeeMapper.convertToDTO(employee);
            return new ResponseEntity<>( new EmployeeResponse("OK", Collections.singletonList(dto)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new EmployeeResponse("Can't find user with id [" + id + "]", Collections.emptyList()),HttpStatus.OK);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<EmployeeResponse> create(@RequestBody EmployeeDTO dto) {
        Employee employee = EmployeeMapper.convertToEntity(dto,organizationService, roleService);
        Employee createdEmployee = employeeService.create(employee);
        if(createdEmployee != null && !createdEmployee.getId().isEmpty()) {
            EmployeeResponse response = new EmployeeResponse("OK", Collections.singletonList(EmployeeMapper.convertToDTO(createdEmployee)));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new EmployeeResponse("User cannot be created due to an error", Collections.emptyList()),HttpStatus.OK);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EmployeeResponse> update(@PathVariable("id") String id,
                                                   @RequestBody EmployeeDTO employeeDTO) {
        Employee updatedEmployee;
        try {
            updatedEmployee = employeeService.update(employeeService.findById(id), EmployeeMapper.convertToEntity(employeeDTO,organizationService, roleService));
        } catch (UserFieldException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(new EmployeeResponse("Can't update employee with id [" + id + "] due to an error", Collections.emptyList()), HttpStatus.OK);
        }
        if(updatedEmployee != null && !updatedEmployee.getId().isEmpty()) {
            EmployeeResponse response = new EmployeeResponse("OK", Collections.singletonList(EmployeeMapper.convertToDTO(updatedEmployee)));
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(new EmployeeResponse("Update wasn't successful due to an error", Collections.emptyList()), HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<EmployeeResponse> delete(@PathVariable("id") String id) {
        employeeService.delete(id);
        return new ResponseEntity<>(new EmployeeResponse("User with id [" + "] was deleted successful", Collections.emptyList()), HttpStatus.OK);
    }

    @PostMapping("/test/admin")
    public ResponseEntity<EmployeeResponse> createAdmin() {
        Employee admin = employeeService.createAdmin();
        if(admin != null) {
            EmployeeResponse response = new EmployeeResponse("OK", Collections.singletonList(EmployeeMapper.convertToDTO(admin)));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new EmployeeResponse("Admin wasn't created due to an error, check logs", Collections.emptyList()), HttpStatus.OK);
        }
    }

    @PostMapping("/test/eng")
    public ResponseEntity<EmployeeResponse> createEngineer() {
        Employee engineer = employeeService.createEngineer();
        if(engineer != null) {
            EmployeeResponse response = new EmployeeResponse("OK", Collections.singletonList(EmployeeMapper.convertToDTO(engineer)));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new EmployeeResponse("Engineer wasn't created due to an error, check logs", Collections.emptyList()), HttpStatus.OK);
        }
    }

    @PostMapping("/test/heng")
    public ResponseEntity<EmployeeResponse> createHeadEngineer() {
        Employee headEngineer = employeeService.createHeadEngineer();
        if(headEngineer != null) {
            EmployeeResponse response = new EmployeeResponse("OK", Collections.singletonList(EmployeeMapper.convertToDTO(headEngineer)));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new EmployeeResponse("Head Engineer wasn't created due to an error, check logs", Collections.emptyList()), HttpStatus.OK);
        }
    }

    @PutMapping("/test/upd/{id}")
    public ResponseEntity<EmployeeResponse> updateRaw(@PathVariable("id") String employeeId,
                                                      @RequestBody EmployeeDTO employee) {
        Employee updatedEmployee = employeeService.updateRaw(EmployeeMapper.convertToEntity(employee, organizationService, roleService), employeeId);
        if(updatedEmployee != null && !updatedEmployee.getId().isEmpty()) {
            EmployeeDTO updatedEmployeeDTO = EmployeeMapper.convertToDTO(updatedEmployee);
            return new ResponseEntity<>(new EmployeeResponse("OK", Collections.singletonList(updatedEmployeeDTO)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new EmployeeResponse("Cant perform raw update, check logs", Collections.emptyList()), HttpStatus.OK);
        }
    }


    /*private EmployeeDTO convertToDTO(Employee employee) {
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

    private Employee convertToEntity(EmployeeDTO dto) {
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
    }*/
}
