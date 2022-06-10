package io.allteran.cascade.managerservice.controller;

import io.allteran.cascade.managerservice.domain.POSType;
import io.allteran.cascade.managerservice.dto.POSTypeDTO;
import io.allteran.cascade.managerservice.service.POSTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/manage/pos-type")
public class POSTypeController {
    private final POSTypeService posTypeService;
    private final ModelMapper modelMapper;

    @Autowired
    public POSTypeController(POSTypeService posTypeService, ModelMapper modelMapper) {
        this.posTypeService = posTypeService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/list")
    public ResponseEntity<List<POSTypeDTO>> findAll() {
        List<POSType> posTypeList = posTypeService.findAll();
        if(posTypeList != null && !posTypeList.isEmpty()) {
            List<POSTypeDTO> posTypeDTOS = posTypeList.stream().map(this::convertToDTO).toList();
            return new ResponseEntity<>(posTypeDTOS, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<POSTypeDTO> findById(@PathVariable("id") String id) {
        POSType posType = posTypeService.findById(id);
        if(posType != null && !posType.getId().isEmpty()) {
            POSTypeDTO posTypeDTO = convertToDTO(posType);
            return new ResponseEntity<>(posTypeDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<POSTypeDTO> create(@RequestBody POSTypeDTO posTypeDTO) {
        POSType createdType = posTypeService.create(convertToEntity(posTypeDTO));
        if(createdType != null && !createdType.getId().isEmpty()) {
            POSTypeDTO createdTypeDTO = convertToDTO(createdType);
            return new ResponseEntity<>(createdTypeDTO, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<POSTypeDTO> update(@PathVariable("id") String typeId,
                                             @RequestBody POSTypeDTO posTypeDTO) {
        POSType updatedPOSType = posTypeService.update(convertToEntity(posTypeDTO), typeId);
        if(updatedPOSType != null && !updatedPOSType.getId().isEmpty()) {
            POSTypeDTO updatedTypeDTO = convertToDTO(updatedPOSType);
            return new ResponseEntity<>(updatedTypeDTO, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") String id) {
        posTypeService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private POSTypeDTO convertToDTO(POSType posType) {
        return modelMapper.map(posType, POSTypeDTO.class);
    }

    private POSType convertToEntity(POSTypeDTO posTypeDTO) {
        return modelMapper.map(posTypeDTO, POSType.class);
    }
}
