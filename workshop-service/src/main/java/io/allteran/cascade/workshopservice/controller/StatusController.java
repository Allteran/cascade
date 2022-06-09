package io.allteran.cascade.workshopservice.controller;


import io.allteran.cascade.workshopservice.domain.Status;
import io.allteran.cascade.workshopservice.dto.StatusDTO;
import io.allteran.cascade.workshopservice.service.StatusService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/workshop/status")
@CrossOrigin(origins = "http://localhost:8100")
public class StatusController {
    private final StatusService statusService;
    private final ModelMapper modelMapper;

    @Autowired
    public StatusController(StatusService statusService, ModelMapper modelMapper) {
        this.statusService = statusService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{statusId}")
    public ResponseEntity<StatusDTO> findById(@PathVariable("statusId") String id) {
        Status status = statusService.findById(id);
        if(status == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            StatusDTO statusDTO = convertToDTO(status);
            return new ResponseEntity<>(statusDTO, HttpStatus.OK);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<StatusDTO>> findAll() {
        List<Status> statusList = statusService.findAll();
        if(statusList == null || statusList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<StatusDTO> statusDTOs = statusList.stream().map(this::convertToDTO).toList();
        return new ResponseEntity<>(statusDTOs, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<StatusDTO> create(@RequestBody StatusDTO statusDTO) {
        Status status = convertToEntity(statusDTO);
        Status createdStatus = statusService.create(status);
        if(createdStatus != null) {
            StatusDTO createdStatusDTO = convertToDTO(createdStatus);
            return new ResponseEntity<>(createdStatusDTO, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{statusId}")
    public ResponseEntity<StatusDTO> update(@PathVariable("statusId") String statusId,
                         @RequestBody StatusDTO statusDTO) {
        Status status = statusService.update(convertToEntity(statusDTO), statusId);
        if(status.getId() == null || status.getId().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } else {
            return new ResponseEntity<>(convertToDTO(status), HttpStatus.ACCEPTED);
        }
    }

    @DeleteMapping("/delete/{statusId}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("statusId") String id) {
        statusService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private StatusDTO convertToDTO(Status status) {
        return modelMapper.map(status, StatusDTO.class);
    }
    private Status convertToEntity(StatusDTO statusDTO) {
        return modelMapper.map(statusDTO, Status.class);
    }

}
