package io.allteran.cascade.managerservice.controller;

import io.allteran.cascade.managerservice.domain.POSType;
import io.allteran.cascade.managerservice.domain.PointOfSales;
import io.allteran.cascade.managerservice.dto.POSResponse;
import io.allteran.cascade.managerservice.dto.PointOfSalesDTO;
import io.allteran.cascade.managerservice.exception.NotFoundException;
import io.allteran.cascade.managerservice.service.POSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/v1/manage/pos")
@CrossOrigin(origins = "http://localhost:8100")
public class POSController {
    private final POSService posService;

    @Autowired
    public POSController(POSService posService) {
        this.posService = posService;
    }

    @GetMapping("/list")
    public ResponseEntity<POSResponse> findAll() {
        List<PointOfSales> posList = posService.findAll();
        if(posList != null && !posList.isEmpty()) {
            List<PointOfSalesDTO> dtoList = posList.stream().map(this::convertToDTO).toList();
            return new ResponseEntity<>(new POSResponse("OK", dtoList), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new POSResponse("List of Point Of Sales is empty", Collections.emptyList()), HttpStatus.OK);
        }
    }

    @GetMapping("/search/id-list/")
    public ResponseEntity<POSResponse> findAllByIdList(@RequestParam List<String> id) {
        List<PointOfSales> posList;
        try {
            posList = posService.findAllById(id);
        } catch (NotFoundException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(new POSResponse("Cant find point of sales with certain IDs cause of error", Collections.emptyList()), HttpStatus.OK);
        }
        if(posList != null && !posList.isEmpty()) {
            List<PointOfSalesDTO> dtoList = posList.stream().map(this::convertToDTO).toList();
            return new ResponseEntity<>(new POSResponse("OK", dtoList), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new POSResponse("Cant find point of sales with certain IDs", Collections.emptyList()), HttpStatus.OK);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<POSResponse> findById(@PathVariable("id") String id) {
        PointOfSales pos;
        try {
            pos = posService.findById(id);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(new POSResponse("Cant find point of sales with id [" + id + "] due to an error", Collections.emptyList()), HttpStatus.OK);
        }
        if(pos != null && !pos.getId().isEmpty()) {
            PointOfSalesDTO posDto = convertToDTO(pos);
            return new ResponseEntity<>(new POSResponse("OK", Collections.singletonList(posDto)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new POSResponse("Cant find point of sales with id [" + id + "]", Collections.emptyList()), HttpStatus.OK);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<POSResponse> create(@RequestBody PointOfSalesDTO posDTO) {
        PointOfSales createdPOS = posService.create(convertToEntity(posDTO));
        if(createdPOS != null && !createdPOS.getId().isEmpty()) {
            PointOfSalesDTO createdPosDTO = convertToDTO(createdPOS);
            return new ResponseEntity<>(new POSResponse("OK", Collections.singletonList(createdPosDTO)), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new POSResponse("Can't create Point Of Sales", Collections.emptyList()), HttpStatus.OK);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<POSResponse> update(@PathVariable("id") String id,
                                              @RequestBody PointOfSalesDTO posDTO) {
        PointOfSales updatedPOS = posService.update(convertToEntity(posDTO), id);
        if(updatedPOS != null && !updatedPOS.getId().isEmpty()) {
            PointOfSalesDTO updatedPosDTO = convertToDTO(updatedPOS);
            return new ResponseEntity<>(new POSResponse("OK", Collections.singletonList(updatedPosDTO)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new POSResponse("Can't update Point Of Sales with ID [" + id + "]", Collections.emptyList()), HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<POSResponse> delete(@PathVariable("id") String id) {
        posService.delete(id);
        return new ResponseEntity<>(new POSResponse("POS with ID [" + "] were deleted successfully" , Collections.emptyList()), HttpStatus.OK);
    }

    private PointOfSalesDTO convertToDTO(PointOfSales pos) {
        PointOfSalesDTO posDTO = new PointOfSalesDTO();
        posDTO.setId(pos.getId());
        posDTO.setRegion(pos.getRegion());
        posDTO.setCity(pos.getCity());
        posDTO.setAddress(pos.getAddress());
        posDTO.setShortName(pos.getShortName());
        posDTO.setTypeId(pos.getType().getId());
        posDTO.setTypeName(pos.getType().getName());

        return posDTO;
    }

    private PointOfSales convertToEntity(PointOfSalesDTO posDTO) {
        PointOfSales pos = new PointOfSales();

        pos.setId(posDTO.getId());
        pos.setRegion(posDTO.getRegion());
        pos.setCity(posDTO.getCity());
        pos.setAddress(posDTO.getAddress());
        pos.setShortName(posDTO.getShortName());

        POSType posType = new POSType();
        posType.setId(posDTO.getTypeId());
        posType.setName(posDTO.getTypeName());

        pos.setType(posType);

        return pos;
    }
}


