package io.allteran.cascade.workshopservice.controller;


import io.allteran.cascade.workshopservice.domain.Status;
import io.allteran.cascade.workshopservice.dto.StatusDTO;
import io.allteran.cascade.workshopservice.dto.StatusResponse;
import io.allteran.cascade.workshopservice.service.StatusService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
    public ResponseEntity<StatusResponse> findById(@PathVariable("statusId") String id) {
        Status status = statusService.findById(id);
        if(status == null) {
            return ResponseEntity.ok(new StatusResponse("Can't find STATUS with id [" + id + "]", Collections.emptyList()));
        } else {
            StatusDTO statusDTO = convertToDTO(status);
            return ResponseEntity.ok(new StatusResponse("OK", Collections.singletonList(statusDTO)));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<StatusResponse> findAll() {
        List<Status> statusList = statusService.findAll();
        if(statusList == null || statusList.isEmpty()) {
            return ResponseEntity.ok(new StatusResponse("Can't find any statuses in DB, check logs", Collections.emptyList()));
        }
        List<StatusDTO> statusDTOs = statusList.stream().map(this::convertToDTO).toList();
        return ResponseEntity.ok(new StatusResponse("OK", statusDTOs));
    }

    @PostMapping("/new")
    public ResponseEntity<StatusResponse> create(@RequestBody StatusDTO statusDTO) {
        Status status = convertToEntity(statusDTO);
        Status createdStatus = statusService.create(status);
        if(createdStatus != null) {
            StatusDTO createdStatusDTO = convertToDTO(createdStatus);
            return new ResponseEntity<>(new StatusResponse("OK", Collections.singletonList(createdStatusDTO)), HttpStatus.CREATED);
        } else {
            return ResponseEntity.ok(new StatusResponse("Can't create STATUS, check logs", Collections.emptyList()));
        }
    }

    @PutMapping("/update/{statusId}")
    public ResponseEntity<StatusResponse> update(@PathVariable("statusId") String statusId,
                         @RequestBody StatusDTO statusDTO) {
        Status status = statusService.update(convertToEntity(statusDTO), statusId);
        if(status.getId() == null || status.getId().isEmpty()) {
            return ResponseEntity.ok(new StatusResponse("Cant update status due to an error, check logs", Collections.emptyList()));
        } else {
            return new ResponseEntity<>(new StatusResponse("OK", Collections.singletonList(convertToDTO(status))), HttpStatus.ACCEPTED);
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
