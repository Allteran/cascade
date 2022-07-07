package io.allteran.cascade.managerservice.controller;

import io.allteran.cascade.managerservice.domain.POSType;
import io.allteran.cascade.managerservice.dto.POSTypeDTO;
import io.allteran.cascade.managerservice.dto.POSTypeResponse;
import io.allteran.cascade.managerservice.exception.NotFoundException;
import io.allteran.cascade.managerservice.service.POSTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/v1/manage/pos-type")
//@CrossOrigin(origins = "http://localhost:8100")
public class POSTypeController {
    private final POSTypeService posTypeService;
    private final ModelMapper modelMapper;

    @Autowired
    public POSTypeController(POSTypeService posTypeService, ModelMapper modelMapper) {
        this.posTypeService = posTypeService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/list")
    public ResponseEntity<POSTypeResponse> findAll() {
        List<POSType> posTypeList = posTypeService.findAll();
        if(posTypeList != null && !posTypeList.isEmpty()) {
            List<POSTypeDTO> posTypeDTOS = posTypeList.stream().map(this::convertToDTO).toList();
            return new ResponseEntity<>(new POSTypeResponse("OK", posTypeDTOS), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new POSTypeResponse("List of POSTypes is empty", Collections.emptyList()), HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<POSTypeResponse> findById(@PathVariable("id") String id) {
        POSType posType;
        try {
            posType = posTypeService.findById(id);
        } catch (NotFoundException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(new POSTypeResponse("Cant find POSType with id [" + id + "] due to an error", Collections.emptyList()), HttpStatus.OK);
        }
        if(posType != null && !posType.getId().isEmpty()) {
            POSTypeDTO posTypeDTO = convertToDTO(posType);
            return new ResponseEntity<>(new POSTypeResponse("OK", Collections.singletonList(posTypeDTO)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new POSTypeResponse("Cant find POSType with id [" + id + "]", Collections.emptyList()), HttpStatus.OK);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<POSTypeResponse> create(@RequestBody POSTypeDTO posTypeDTO) {
        POSType createdType = posTypeService.create(convertToEntity(posTypeDTO));
        if(createdType != null && !createdType.getId().isEmpty()) {
            POSTypeDTO createdTypeDTO = convertToDTO(createdType);
            return new ResponseEntity<>(new POSTypeResponse("OK", Collections.singletonList(createdTypeDTO)), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new POSTypeResponse("Cant create POSType, check logs for further info", Collections.emptyList()), HttpStatus.OK);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<POSTypeResponse> update(@PathVariable("id") String typeId,
                                                  @RequestBody POSTypeDTO posTypeDTO) {
        POSType updatedPOSType = posTypeService.update(convertToEntity(posTypeDTO), typeId);
        if(updatedPOSType != null && !updatedPOSType.getId().isEmpty()) {
            POSTypeDTO updatedTypeDTO = convertToDTO(updatedPOSType);
            return new ResponseEntity<>(new POSTypeResponse("OK", Collections.singletonList(updatedTypeDTO)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new POSTypeResponse("Cant update POSType with ID [" + typeId + "]", Collections.emptyList()), HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<POSTypeResponse> delete(@PathVariable("id") String id) {
        posTypeService.delete(id);
        return new ResponseEntity<>(new POSTypeResponse("POSType with id [" + id + "] were deleted successfully", Collections.emptyList()), HttpStatus.OK);
    }

    private POSTypeDTO convertToDTO(POSType posType) {
        return modelMapper.map(posType, POSTypeDTO.class);
    }

    private POSType convertToEntity(POSTypeDTO posTypeDTO) {
        return modelMapper.map(posTypeDTO, POSType.class);
    }
}
