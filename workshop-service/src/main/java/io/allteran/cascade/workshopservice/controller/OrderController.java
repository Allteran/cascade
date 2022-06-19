package io.allteran.cascade.workshopservice.controller;

import io.allteran.cascade.workshopservice.domain.DeviceType;
import io.allteran.cascade.workshopservice.domain.Order;
import io.allteran.cascade.workshopservice.domain.Status;
import io.allteran.cascade.workshopservice.dto.EmployeeDTO;
import io.allteran.cascade.workshopservice.dto.OrderDTO;
import io.allteran.cascade.workshopservice.dto.PointOfSalesDTO;
import io.allteran.cascade.workshopservice.exception.WorkshopException;
import io.allteran.cascade.workshopservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RestController
@RequestMapping("api/v1/workshop/order")
@Slf4j
public class OrderController {
    private static final String MANAGE_SERVICE_EMPLOYEE_URI = "http://localhost:9091/api/v1/manage/employee/";
    public static final String MANAGE_SERVICE_POS_URI = "http://localhost:9091/ap1/v1/manage/pos/";
    private final OrderService  orderService;
    private final WebClient webClient;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderController(OrderService orderService, WebClient webClient, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.webClient = webClient;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/list")
    public ResponseEntity<List<OrderDTO> >findAll() {
        List<Order> orders = orderService.findAll();
        if(orders != null && !orders.isEmpty()) {
            List<String> employeeIdList = orders.stream().map(Order::getAuthorId).toList();
            EmployeeDTO[] employeeDTOs = webClient.get()
                    .uri(MANAGE_SERVICE_EMPLOYEE_URI + "query/",
                            uriBuilder -> uriBuilder.queryParam("id",employeeIdList).build())
                    .retrieve()
                    .bodyToMono(EmployeeDTO[].class)
                    .block();

            List<String> posIdList = orders.stream().map(Order::getPosId).toList();
            PointOfSalesDTO[] posDTOs = webClient.get()
                    .uri(MANAGE_SERVICE_POS_URI + "query/",
                            uriBuilder -> uriBuilder.queryParam("id", posIdList).build())
                    .retrieve()
                    .bodyToMono(PointOfSalesDTO[].class)
                    .block();

            List<String> engineerIdList = orders.stream().map(Order::getEngineerId).toList();
            EmployeeDTO[] engineerDTOs = webClient.get()
                    .uri(MANAGE_SERVICE_EMPLOYEE_URI + "query/",
                            uriBuilder -> uriBuilder.queryParam("id", engineerIdList).build())
                    .retrieve()
                    .bodyToMono(EmployeeDTO[].class)
                    .block();
            if(engineerDTOs == null) {
                log.debug("Database doesn't recognize engineers with ID from orders");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            if(employeeDTOs == null) {
                log.error("Database doesn't recognize users ID from orders");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            if(posDTOs == null) {
                log.error("Database doesn't recognize POS ID from orders");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            List<OrderDTO> dtos = orders.stream().map(this::convertToDTO).toList()
                    .stream()
                    .peek(dto -> {
                        for (EmployeeDTO e : employeeDTOs) {
                            if(e.getId().equals(dto.getAuthorId())) {
                                dto.setAuthorName(e.getFirstName() + " " + e.getLastName());
                            }
                        }
                        for(EmployeeDTO engineer : engineerDTOs) {
                            if(engineer.getId().equals(dto.getEngineerId())) {
                                dto.setEngineerName(engineer.getFirstName() + " " + engineer.getLastName());
                                dto.setEngineerRoles(engineer.getRoles());
                            }
                        }
                        for(PointOfSalesDTO p : posDTOs) {
                            if(p.getId().equals(dto.getPosId())) {
                                dto.setPosShortName(p.getShortName());
                            }
                        }
                    }).toList();
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{orderId}")
    public ResponseEntity<OrderDTO> findById(@PathVariable("orderId") String orderId) {
        Order order = orderService.findById(orderId);
        if(order != null && !order.getId().isEmpty()) {
            EmployeeDTO employee = webClient.get()
                    .uri(MANAGE_SERVICE_EMPLOYEE_URI + order.getAuthorId())
                    .retrieve()
                    .bodyToMono(EmployeeDTO.class)
                    .block();
            PointOfSalesDTO pos = webClient.get()
                    .uri(MANAGE_SERVICE_POS_URI + order.getPosId())
                    .retrieve()
                    .bodyToMono(PointOfSalesDTO.class)
                    .block();
            EmployeeDTO engineer = webClient.get()
                    .uri(MANAGE_SERVICE_EMPLOYEE_URI + order.getEngineerId())
                    .retrieve()
                    .bodyToMono(EmployeeDTO.class)
                    .block();

            if(employee == null) {
                log.error("User with id {} not found in database", order.getAuthorId());
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            if(pos == null) {
                log.error("Point Of Sales with ID {} not found in database", order.getPosId());
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            if(engineer == null) {
                log.error("Engineer with id {} not found in database", order.getEngineerId());
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            OrderDTO dto = convertToDTO(order);
            dto.setAuthorName(employee.getFirstName() + " " + employee.getLastName());
            dto.setPosShortName(pos.getShortName());
            dto.setEngineerName(engineer.getFirstName() + " " + engineer.getLastName());
            dto.setEngineerRoles(engineer.getRoles());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{orderId}")
    public ResponseEntity<OrderDTO> update(@PathVariable("orderId") String orderFromDbId,
                                           @RequestBody OrderDTO orderDTO) {
        //TODO: REPLACE WITH CALL TO ANOTHER SERVICE TO GET USER BY ID
        Order order = convertToEntity(orderDTO);
        Order updatedOrder;
        try {
            updatedOrder = orderService.update(orderService.findById(orderFromDbId), order);
            if(updatedOrder != null && !updatedOrder.getId().isEmpty()) {
                EmployeeDTO employee = webClient.get()
                        .uri(MANAGE_SERVICE_EMPLOYEE_URI + order.getAuthorId())
                        .retrieve()
                        .bodyToMono(EmployeeDTO.class)
                        .block();
                PointOfSalesDTO pos = webClient.get()
                        .uri(MANAGE_SERVICE_POS_URI + order.getPosId())
                        .retrieve()
                        .bodyToMono(PointOfSalesDTO.class)
                        .block();
                EmployeeDTO engineer = webClient.get()
                        .uri(MANAGE_SERVICE_EMPLOYEE_URI + order.getEngineerId())
                        .retrieve()
                        .bodyToMono(EmployeeDTO.class)
                        .block();
                if(employee == null) {
                    log.error("User with id {} not found in database", order.getAuthorId());
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                if(pos == null) {
                    log.error("Point Of Sales with ID {} not found in database", order.getPosId());
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                if(engineer == null) {
                    log.error("Engineer with id {} not found in database", order.getEngineerId());
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                OrderDTO updatedDTO = convertToDTO(updatedOrder);
                updatedDTO.setAuthorName(employee.getFirstName() + " " + employee.getLastName());
                updatedDTO.setPosShortName(pos.getShortName());
                updatedDTO.setEngineerName(engineer.getFirstName() + " " + engineer.getLastName());
                updatedDTO.setEngineerRoles(engineer.getRoles());

                return new ResponseEntity<>(updatedDTO, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
        } catch (WorkshopException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("orderId") String orderId) {
        orderService.delete(orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Order convertToEntity(OrderDTO orderDTO) {
        Order order = new Order();
        order.setId(orderDTO.getId());
        order.setDeviceName(orderDTO.getDeviceName());

        DeviceType type = new DeviceType();
        type.setId(orderDTO.getDeviceTypeId());
        type.setName(orderDTO.getDeviceTypeName());
        order.setDeviceType(type);

        Status status = new Status();
        status.setId(orderDTO.getStatusId());
        status.setName(orderDTO.getStatusName());
        order.setStatus(status);

        order.setSerialNumber(orderDTO.getSerialNumber());
        order.setDefect(orderDTO.getDefect());
        order.setEquipSet(orderDTO.getEquipSet());
        order.setAppearance(orderDTO.getAppearance());
        order.setWarranty(orderDTO.getWarranty());
        order.setPerformedActions(orderDTO.getPerformedActions());
        order.setCreationDate(orderDTO.getCreationDate());
        order.setIssueDate(orderDTO.getIssueDate());
        order.setPreliminaryPrice(orderDTO.getPreliminaryPrice());
        order.setServicePrice(orderDTO.getServicePrice());
        order.setComponentPrice(orderDTO.getComponentPrice());
        order.setMarginPrice(orderDTO.getMarginPrice());
        order.setTotalPrice(orderDTO.getTotalPrice());
        order.setEngineerRate(orderDTO.getEngineerRate());

        order.setDirectorProfit(orderDTO.getDirectorProfit());
        order.setHeadEngineerProfit(orderDTO.getHeadEngineerProfit());
        order.setEngineerProfit(order.getEngineerProfit());
        order.setManagerProfit(order.getManagerProfit());
        order.setEmployeeProfit(orderDTO.getEmployeeProfit());

        order.setPosId(orderDTO.getPosId());

        order.setCustomerName(orderDTO.getCustomerName());
        order.setCustomerPhone(orderDTO.getCustomerPhone());

        order.setAuthorId(orderDTO.getAuthorId());

        order.setEngineerId(orderDTO.getEngineerId());
        order.setEngineerRoles(orderDTO.getEngineerRoles());

        return order;
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setId(order.getId());
        orderDTO.setDeviceName(order.getDeviceName());
        orderDTO.setDeviceTypeId(order.getDeviceType().getId());
        orderDTO.setDeviceTypeName(order.getDeviceType().getName());
        orderDTO.setStatusId(order.getStatus().getId());
        orderDTO.setStatusName(order.getStatus().getName());

        orderDTO.setSerialNumber(order.getSerialNumber());
        orderDTO.setDefect(order.getDefect());
        orderDTO.setEquipSet(order.getEquipSet());
        orderDTO.setWarranty(order.getWarranty());
        orderDTO.setPerformedActions(order.getPerformedActions());
        orderDTO.setCreationDate(order.getCreationDate());
        orderDTO.setIssueDate(order.getIssueDate());
        orderDTO.setPreliminaryPrice(order.getPreliminaryPrice());
        orderDTO.setServicePrice(order.getServicePrice());
        orderDTO.setComponentPrice(order.getComponentPrice());
        orderDTO.setMarginPrice(order.getMarginPrice());
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setEngineerRate(order.getEngineerRate());
        orderDTO.setDirectorProfit(order.getDirectorProfit());
        orderDTO.setHeadEngineerProfit(order.getHeadEngineerProfit());
        orderDTO.setManagerProfit(order.getManagerProfit());
        orderDTO.setEmployeeProfit(order.getEmployeeProfit());

        orderDTO.setCustomerName(order.getCustomerName());
        orderDTO.setCustomerPhone(order.getCustomerPhone());
        orderDTO.setPosId(order.getPosId());

        orderDTO.setAuthorId(order.getAuthorId());

        orderDTO.setEngineerId(order.getEngineerId());
        orderDTO.setEngineerRoles(order.getEngineerRoles());

        return orderDTO;

    }


}
