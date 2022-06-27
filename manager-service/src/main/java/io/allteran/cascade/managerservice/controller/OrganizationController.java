package io.allteran.cascade.managerservice.controller;

import io.allteran.cascade.managerservice.domain.Organization;
import io.allteran.cascade.managerservice.dto.OrganizationDTO;
import io.allteran.cascade.managerservice.dto.OrganizationResponse;
import io.allteran.cascade.managerservice.service.OrganizationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/v1/manage/organization")
@CrossOrigin(origins = "http://localhost:8100")
public class OrganizationController {
    private final OrganizationService orgService;
    private final ModelMapper modelMapper;

    @Autowired
    public OrganizationController(OrganizationService orgService, ModelMapper modelMapper) {
        this.orgService = orgService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/list")
    public ResponseEntity<OrganizationResponse> findAll() {
        List<Organization> orgList = orgService.findAll();
        if(orgList != null && !orgList.isEmpty()) {
            List<OrganizationDTO> orgDTOList = orgList.stream().map(this::convertToDTO).toList();
            return new ResponseEntity<>(new OrganizationResponse("OK", orgDTOList), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new OrganizationResponse("List of Organizations is empty", Collections.emptyList()), HttpStatus.OK);
        }
    }

    @GetMapping("{organizationId}")
    public ResponseEntity<OrganizationResponse> findById(@PathVariable("organizationId") String orgId) {
        Organization organization = orgService.findById(orgId);
        if(organization != null) {
            OrganizationDTO organizationDTO = convertToDTO(organization);
            return new ResponseEntity<>(new OrganizationResponse("OK", Collections.singletonList(organizationDTO)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new OrganizationResponse("Cant find organization with id [" + orgId + "]", Collections.emptyList()), HttpStatus.OK);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<OrganizationResponse> create(@RequestBody OrganizationDTO organizationDTO) {
        Organization organization = convertToEntity(organizationDTO);
        Organization createdOrganization = orgService.create(organization);
        if(createdOrganization.getId() != null && !createdOrganization.getId().isEmpty()) {
            OrganizationDTO createdOrganizationDTO = convertToDTO(createdOrganization);
            return new ResponseEntity<>(new OrganizationResponse("OK", Collections.singletonList(createdOrganizationDTO)), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new OrganizationResponse("Cant create organization, check logs", Collections.emptyList()), HttpStatus.OK);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<OrganizationResponse> update(@PathVariable("id") String organizationId,
                                                       @RequestBody OrganizationDTO organizationDTO) {
        Organization updatedOrg = orgService.update(convertToEntity(organizationDTO), organizationId);
        if(updatedOrg != null && !updatedOrg.getId().isEmpty()) {
            OrganizationDTO updatedOrgDTO = convertToDTO(updatedOrg);
            return new ResponseEntity<>(new OrganizationResponse("OK", Collections.singletonList(updatedOrgDTO)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new OrganizationResponse("Cant update organization with id [" + organizationId + "]", Collections.emptyList()), HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<OrganizationResponse> delete(@PathVariable("id") String organizationId) {
        orgService.delete(organizationId);
        return new ResponseEntity<>(new OrganizationResponse("Organization with id [" + organizationId + "] were deleted successfully", Collections.emptyList()), HttpStatus.OK);
    }


    private Organization convertToEntity(OrganizationDTO dto) {
        return modelMapper.map(dto, Organization.class);
    }

    private OrganizationDTO convertToDTO(Organization organization) {
        return modelMapper.map(organization, OrganizationDTO.class);
    }

}
