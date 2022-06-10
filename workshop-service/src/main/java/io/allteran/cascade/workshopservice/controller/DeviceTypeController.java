package io.allteran.cascade.workshopservice.controller;

import io.allteran.cascade.workshopservice.domain.DeviceType;
import io.allteran.cascade.workshopservice.dto.DeviceTypeDTO;
import io.allteran.cascade.workshopservice.service.DeviceTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/workshop/device-type")
@CrossOrigin(origins = "http://localhost:8100")
public class DeviceTypeController {
    private final DeviceTypeService deviceTypeService;
    private final ModelMapper modelMapper;

    @Autowired
    public DeviceTypeController(DeviceTypeService deviceTypeService, ModelMapper modelMapper) {
        this.deviceTypeService = deviceTypeService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/list")
    public ResponseEntity<List<DeviceTypeDTO>> findAll() {
        List<DeviceType> typeList = deviceTypeService.findAll();
        if(typeList == null || typeList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<DeviceTypeDTO> typeDTOS = typeList.stream().map(this::convertToDTO).toList();
            return new ResponseEntity<>(typeDTOS, HttpStatus.OK);
        }
    }

    @GetMapping("{typeId}")
    public ResponseEntity<DeviceTypeDTO> findById(@PathVariable("typeId") String typeId) {
        DeviceType type = deviceTypeService.findById(typeId);
        if(type == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            DeviceTypeDTO typeDTO = convertToDTO(type);
            return new ResponseEntity<>(typeDTO, HttpStatus.OK);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<DeviceTypeDTO> create (@RequestBody DeviceTypeDTO deviceTypeDTO) {
        DeviceType deviceType = convertToEntity(deviceTypeDTO);
        DeviceType createdType = deviceTypeService.create(deviceType);
        if(createdType.getId() != null && !createdType.getId().isEmpty()) {
            DeviceTypeDTO createdTypeDTO = convertToDTO(createdType);
            return new ResponseEntity<>(createdTypeDTO, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{typeId}")
    public ResponseEntity<DeviceTypeDTO> update(@PathVariable("typeId") String typeId,
                                                @RequestBody DeviceTypeDTO deviceTypeDTO) {
        DeviceType type = deviceTypeService.update(convertToEntity(deviceTypeDTO), typeId);
        if(type != null && !type.getId().isEmpty()) {
            DeviceTypeDTO updatedTypeDTO = convertToDTO(type);
            return new ResponseEntity<>(updatedTypeDTO, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @DeleteMapping("/delete/{typeId}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("typeId") String typeId) {
        deviceTypeService.delete(typeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private DeviceType convertToEntity(DeviceTypeDTO deviceTypeDTO) {
        return modelMapper.map(deviceTypeDTO, DeviceType.class);
    }

    private DeviceTypeDTO convertToDTO(DeviceType deviceType) {
        return modelMapper.map(deviceType, DeviceTypeDTO.class);
    }
}
