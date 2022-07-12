package io.allteran.cascade.workshopservice.controller;

import io.allteran.cascade.workshopservice.domain.DeviceType;
import io.allteran.cascade.workshopservice.dto.DeviceTypeDTO;
import io.allteran.cascade.workshopservice.dto.DeviceTypeResponse;
import io.allteran.cascade.workshopservice.service.DeviceTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
    public ResponseEntity<DeviceTypeResponse> findAll() {
        List<DeviceType> typeList = deviceTypeService.findAll();
        if(typeList == null || typeList.isEmpty()) {
            return ResponseEntity.ok(new DeviceTypeResponse("Cant find any Device Type in database", Collections.emptyList()));
        } else {
            List<DeviceTypeDTO> typeDTOS = typeList.stream().map(this::convertToDTO).toList();
            return ResponseEntity.ok(new DeviceTypeResponse("OK", typeDTOS));
        }
    }

    @GetMapping("{typeId}")
    public ResponseEntity<DeviceTypeResponse> findById(@PathVariable("typeId") String typeId) {
        DeviceType type = deviceTypeService.findById(typeId);
        if(type == null) {
            return ResponseEntity.ok(new DeviceTypeResponse("Can't find DEVICE TYPE with id [" + typeId +"]", Collections.emptyList()));
        } else {
            DeviceTypeDTO typeDTO = convertToDTO(type);
            return ResponseEntity.ok(new DeviceTypeResponse("OK", Collections.singletonList(typeDTO)));
        }
    }

    @PostMapping("/new")
    public ResponseEntity<DeviceTypeResponse> create (@RequestBody DeviceTypeDTO deviceTypeDTO) {
        DeviceType deviceType = convertToEntity(deviceTypeDTO);
        DeviceType createdType = deviceTypeService.create(deviceType);
        if(createdType.getId() != null && !createdType.getId().isEmpty()) {
            DeviceTypeDTO createdTypeDTO = convertToDTO(createdType);
            return new ResponseEntity<>(new DeviceTypeResponse("OK", Collections.singletonList(createdTypeDTO)), HttpStatus.CREATED);
        } else {
            return ResponseEntity.ok(new DeviceTypeResponse("Can' create DEVICE TYPE, check logs", Collections.emptyList()));
        }
    }

    @PutMapping("/update/{typeId}")
    public ResponseEntity<DeviceTypeResponse> update(@PathVariable("typeId") String typeId,
                                                @RequestBody DeviceTypeDTO deviceTypeDTO) {
        DeviceType type = deviceTypeService.update(convertToEntity(deviceTypeDTO), typeId);
        if(type != null && !type.getId().isEmpty()) {
            DeviceTypeDTO updatedTypeDTO = convertToDTO(type);
            return new ResponseEntity<>(new DeviceTypeResponse("OK", Collections.singletonList(updatedTypeDTO)), HttpStatus.ACCEPTED);
        } else {
            return ResponseEntity.ok(new DeviceTypeResponse("OK", Collections.emptyList()));
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
