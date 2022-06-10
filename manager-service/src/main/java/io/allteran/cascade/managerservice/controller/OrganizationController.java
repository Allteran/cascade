package io.allteran.cascade.managerservice.controller;

import io.allteran.cascade.managerservice.domain.Organization;
import io.allteran.cascade.managerservice.dto.OrganizationDTO;
import io.allteran.cascade.managerservice.service.OrganizationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/manage/organization")
public class OrganizationController {
    private final OrganizationService orgService;
    private final ModelMapper modelMapper;

    @Autowired
    public OrganizationController(OrganizationService orgService, ModelMapper modelMapper) {
        this.orgService = orgService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/list")
    public ResponseEntity<List<OrganizationDTO>> findAll() {
        List<Organization> orgList = orgService.findAll();
        if(orgList != null && !orgList.isEmpty()) {
            List<OrganizationDTO> orgDTOList = orgList.stream().map(this::convertToDTO).toList();
            return new ResponseEntity<>(orgDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{organizationId}")
    public ResponseEntity<OrganizationDTO> findById(@PathVariable("organizationId") String orgId) {
        Organization organization = orgService.findById(orgId);
        if(organization != null) {
            OrganizationDTO organizationDTO = convertToDTO(organization);
            return new ResponseEntity<>(organizationDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<OrganizationDTO> create(@RequestBody OrganizationDTO organizationDTO) {
        Organization organization = convertToEntity(organizationDTO);
        Organization createdOrganization = orgService.create(organization);
        if(createdOrganization.getId() != null && !createdOrganization.getId().isEmpty()) {
            OrganizationDTO createdOrganizationDTO = convertToDTO(createdOrganization);
            return new ResponseEntity<>(createdOrganizationDTO, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<OrganizationDTO> update(@PathVariable("id") String organizationId,
                                                  @RequestBody OrganizationDTO organizationDTO) {
        Organization updatedOrg = orgService.update(convertToEntity(organizationDTO), organizationId);
        if(updatedOrg != null && !updatedOrg.getId().isEmpty()) {
            OrganizationDTO updatedOrgDTO = convertToDTO(updatedOrg);
            return new ResponseEntity<>(updatedOrgDTO, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") String organizationId) {
        orgService.delete(organizationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    private Organization convertToEntity(OrganizationDTO dto) {
        return modelMapper.map(dto, Organization.class);
    }

    private OrganizationDTO convertToDTO(Organization organization) {
        return modelMapper.map(organization, OrganizationDTO.class);
    }

}
