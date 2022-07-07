package io.allteran.cascade.managerservice.controller;

import io.allteran.cascade.managerservice.domain.Role;
import io.allteran.cascade.managerservice.dto.RoleDTO;
import io.allteran.cascade.managerservice.dto.RoleResponse;
import io.allteran.cascade.managerservice.exception.NotFoundException;
import io.allteran.cascade.managerservice.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/v1/manage/employee-role")
//@CrossOrigin(origins = "http://localhost:8100")
public class RoleController {
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/list")
    public ResponseEntity<RoleResponse> findAll() {
        List<Role> roles = roleService.findAll();
        if(roles != null && !roles.isEmpty()) {
            List<RoleDTO> dtoList = roles.stream().map(this::convertToDTO).toList();
            return new ResponseEntity<>(new RoleResponse("OK", dtoList), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new RoleResponse("Role list is empty", Collections.emptyList()), HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> findById(@PathVariable("id") String id) {
        Role role;
        try {
            role = roleService.findById(id);
        } catch (NotFoundException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(new RoleResponse("Role with certain ID not found in DB", Collections.emptyList()), HttpStatus.OK);
        }
        if(role != null && !role.getId().isEmpty()) {
            RoleDTO roleDTO = convertToDTO(role);
            return new ResponseEntity<>(new RoleResponse("OK", Collections.singletonList(roleDTO)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new RoleResponse("Role with certain ID not found in DB", Collections.emptyList()), HttpStatus.OK);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<RoleResponse> create(@RequestBody RoleDTO role) {
        Role createdRole = roleService.create(convertToEntity(role));
        if(createdRole != null && !createdRole.getId().isEmpty()) {
            RoleDTO createdDTO = convertToDTO(createdRole);
            return new ResponseEntity<>(new RoleResponse("OK", Collections.singletonList(createdDTO)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new RoleResponse("Role cannot be created due to an error, check logs", Collections.emptyList()), HttpStatus.OK);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RoleResponse> update(@PathVariable("id") String id,
                                               @RequestBody RoleDTO role) {
        Role updatedRole = roleService.update(convertToEntity(role), id);
        if(updatedRole != null && !updatedRole.getId().isEmpty()) {
            RoleDTO updatedDTO = convertToDTO(updatedRole);
            return new ResponseEntity<>(new RoleResponse("OK", Collections.singletonList(updatedDTO)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new RoleResponse("Role cannot be updated due to an error, check logs", Collections.emptyList()), HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") String id) {
        roleService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private RoleDTO convertToDTO(Role r) {
        return new RoleDTO(r.getId(), r.getName());
    }

    private Role convertToEntity(RoleDTO dto) {
        return new Role(dto.getId(), dto.getName());
    }
}
