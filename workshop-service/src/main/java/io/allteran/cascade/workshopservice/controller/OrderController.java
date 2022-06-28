package io.allteran.cascade.workshopservice.controller;

import io.allteran.cascade.workshopservice.domain.DeviceType;
import io.allteran.cascade.workshopservice.domain.Order;
import io.allteran.cascade.workshopservice.domain.Status;
import io.allteran.cascade.workshopservice.dto.*;
import io.allteran.cascade.workshopservice.exception.WorkshopException;
import io.allteran.cascade.workshopservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/v1/workshop/order")
@CrossOrigin(origins = "http://localhost:8100")
@Slf4j
public class OrderController {
    private static final String MANAGE_SERVICE_EMPLOYEE_URI = "/api/v1/manage/employee/";
    public static final String MANAGE_SERVICE_POS_URI = "/api/v1/manage/pos/";
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
    public ResponseEntity<OrderResponse> findAll() {
        OrderResponse response = new OrderResponse();
        List<Order> orders = orderService.findAll();
        if(orders != null && !orders.isEmpty()) {
            List<String> employeeIdList = orders.stream().map(Order::getAuthorId).toList();
            EmployeeResponse employeeResponse = webClient.get()
                    .uri(MANAGE_SERVICE_EMPLOYEE_URI + "/search/id-list/",
                            uriBuilder -> uriBuilder.queryParam("id",employeeIdList).build())
                    .retrieve()
                    .bodyToMono(EmployeeResponse.class)
                    .block();

            List<String> posIdList = orders.stream().map(Order::getPosId).toList();
            POSResponse posResponse = webClient.get()
                    .uri(MANAGE_SERVICE_POS_URI + "search/id-list/",
                            uriBuilder -> uriBuilder.queryParam("id", posIdList).build())
                    .retrieve()
                    .bodyToMono(POSResponse.class)
                    .block();

            List<String> engineerIdList = orders.stream().map(Order::getEngineerId).toList();
            EmployeeResponse engineerResponse = webClient.get()
                    .uri(MANAGE_SERVICE_EMPLOYEE_URI + "search/id-list/",
                            uriBuilder -> uriBuilder.queryParam("id", engineerIdList).build())
                    .retrieve()
                    .bodyToMono(EmployeeResponse.class)
                    .block();
            if(engineerResponse == null || engineerResponse.getEmployeeDTOList().isEmpty()) {
                log.debug("Database doesn't recognize engineers with ID from orders");
                response.setMessage("Database doesn't recognize engineers with ID from orders");
                response.setOrderList(Collections.emptyList());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            if(employeeResponse == null || employeeResponse.getEmployeeDTOList().isEmpty()) {
                log.error("Database doesn't recognize users ID from orders");
                response.setMessage("Database doesn't recognize users ID from orders");
                response.setOrderList(Collections.emptyList());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            if(posResponse == null || posResponse.getPosList().isEmpty()) {
                log.error("Database doesn't recognize POS ID from orders");
                response.setMessage("Database doesn't recognize POS ID from orders");
                response.setOrderList(Collections.emptyList());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            List<OrderDTO> dtos = orders.stream().map(this::convertToDTO).toList()
                    .stream()
                    .peek(dto -> {
                        for (EmployeeDTO e : employeeResponse.getEmployeeDTOList()) {
                            if(e.getId().equals(dto.getAuthorId())) {
                                dto.setAuthorName(e.getFirstName() + " " + e.getLastName());
                            }
                        }
                        for(EmployeeDTO engineer : engineerResponse.getEmployeeDTOList()) {
                            if(engineer.getId().equals(dto.getEngineerId())) {
                                dto.setEngineerName(engineer.getFirstName() + " " + engineer.getLastName());
                                dto.setEngineerRoles(engineer.getRoles());
                            }
                        }
                        for(PointOfSalesDTO p : posResponse.getPosList()) {
                            if(p.getId().equals(dto.getPosId())) {
                                dto.setPosShortName(p.getShortName());
                            }
                        }
                    }).toList();
            response.setMessage("OK");
            response.setOrderList(dtos);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.setMessage("List of orders is empty");
            response.setOrderList(Collections.emptyList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("{orderId}")
    public ResponseEntity<OrderResponse> findById(@PathVariable("orderId") String orderId) {
        Order order = orderService.findById(orderId);
        if(order != null && !order.getId().isEmpty()) {
            EmployeeResponse employeeResponse = webClient.get()
                    .uri(MANAGE_SERVICE_EMPLOYEE_URI + order.getAuthorId())
                    .retrieve()
                    .bodyToMono(EmployeeResponse.class)
                    .block();
            POSResponse posResponse = webClient.get()
                    .uri(MANAGE_SERVICE_POS_URI + order.getPosId())
                    .retrieve()
                    .bodyToMono(POSResponse.class)
                    .block();
            EmployeeResponse engineerResponse = webClient.get()
                    .uri(MANAGE_SERVICE_EMPLOYEE_URI + order.getEngineerId())
                    .retrieve()
                    .bodyToMono(EmployeeResponse.class)
                    .block();

            if(employeeResponse == null || employeeResponse.getEmployeeDTOList().isEmpty()) {
                log.error("User with id {} not found in database", order.getAuthorId());
                return new ResponseEntity<>(new OrderResponse("User with id [" + order.getAuthorId() + "] not found in database", Collections.emptyList()), HttpStatus.OK);
            }
            if(posResponse == null || posResponse.getPosList().isEmpty()) {
                log.error("Point Of Sales with ID {} not found in database", order.getPosId());
                return new ResponseEntity<>(new OrderResponse("Point Of Sales with ID [" + order.getPosId() + "]] not found in database", Collections.emptyList()), HttpStatus.OK);
            }
            if(engineerResponse == null || engineerResponse.getEmployeeDTOList().isEmpty()) {
                log.error("Engineer with id {} not found in database", order.getEngineerId());
                return new ResponseEntity<>(new OrderResponse("Engineer with id ["+ order.getEngineerId() +"]] not found in database", Collections.emptyList()), HttpStatus.OK);
            }
            EmployeeDTO author = employeeResponse.getEmployeeDTOList().get(0);
            EmployeeDTO engineer = engineerResponse.getEmployeeDTOList().get(0);
            PointOfSalesDTO pos = posResponse.getPosList().get(0);

            OrderDTO dto = convertToDTO(order);
            dto.setAuthorName(author.getFirstName() + " " + author.getLastName());
            dto.setPosShortName(pos.getShortName());
            dto.setEngineerName(engineer.getFirstName() + " " + engineer.getLastName());
            dto.setEngineerRoles(engineer.getRoles());
            return new ResponseEntity<>(new OrderResponse("OK", Collections.singletonList(dto)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new OrderResponse("Can't find order with id [" + orderId + "]", Collections.emptyList()), HttpStatus.OK);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<OrderResponse> create(@RequestBody OrderDTO orderDTO) {
        EmployeeResponse employeeResponse = webClient.get()
                .uri(MANAGE_SERVICE_EMPLOYEE_URI + orderDTO.getAuthorId())
                .retrieve()
                .bodyToMono(EmployeeResponse.class)
                .block();
        POSResponse posResponse = webClient.get()
                .uri(MANAGE_SERVICE_POS_URI + orderDTO.getPosId())
                .retrieve()
                .bodyToMono(POSResponse.class)
                .block();
        EmployeeResponse engineerResponse = webClient.get()
                .uri(MANAGE_SERVICE_EMPLOYEE_URI + orderDTO.getEngineerId())
                .retrieve()
                .bodyToMono(EmployeeResponse.class)
                .block();

        if(employeeResponse == null || employeeResponse.getEmployeeDTOList().isEmpty()) {
            log.error("User with id {} not found in database", orderDTO.getAuthorId());
            return new ResponseEntity<>(new OrderResponse("User with id [" + orderDTO.getAuthorId() + "] not found in database", Collections.emptyList()), HttpStatus.OK);
        }
        if(posResponse == null || posResponse.getPosList().isEmpty()) {
            log.error("Point Of Sales with ID {} not found in database", orderDTO.getPosId());
            return new ResponseEntity<>(new OrderResponse("Point Of Sales with ID [" + orderDTO.getPosId() + "] not found in database", Collections.emptyList()), HttpStatus.OK);
        }
        if(engineerResponse == null || engineerResponse.getEmployeeDTOList().isEmpty()) {
            log.error("Engineer with id {} not found in database", orderDTO.getEngineerId());
            return new ResponseEntity<>(new OrderResponse("Engineer with id ["+ orderDTO.getEngineerId() +"] not found in database", Collections.emptyList()), HttpStatus.OK);
        }
        Order order = convertToEntity(orderDTO);
        EmployeeDTO author = employeeResponse.getEmployeeDTOList().get(0);
        EmployeeDTO engineer = engineerResponse.getEmployeeDTOList().get(0);
        PointOfSalesDTO pos = posResponse.getPosList().get(0);

        order.setAuthorId(author.getId());
        order.setPosId(pos.getId());
        order.setEngineerId(engineer.getId());
        order.setEngineerRoles(engineer.getRoles());
        Order createdOrder = orderService.create(order);
        if(createdOrder != null) {
            return new ResponseEntity<>(new OrderResponse("OK", Collections.singletonList(convertToDTO(createdOrder))), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new OrderResponse("Cant create order, check logs", Collections.emptyList()), HttpStatus.OK);
        }
    }

    @GetMapping("test/get/emp")
    public ResponseEntity<EmployeeDTO> getEmployee() {
        EmployeeDTO dto = webClient.get()
                .uri(MANAGE_SERVICE_EMPLOYEE_URI + "ff80818181a5579d0181a55840580000")
                .retrieve()
                .bodyToMono(EmployeeDTO.class)
                .block();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("test/get/pos")
    public ResponseEntity<POSResponse> getPos() {
        String id = "ff80818181a553210181a556b5030003";
        POSResponse response = webClient.get()
                .uri("/api/v1/manage/pos/{id}", id)
                .retrieve()
                .bodyToMono(POSResponse.class)
                .block();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/{orderId}")
    public ResponseEntity<OrderResponse> update(@PathVariable("orderId") String orderFromDbId,
                                                @RequestBody OrderDTO orderDTO) {
        Order order = convertToEntity(orderDTO);
        Order updatedOrder;
        try {
            updatedOrder = orderService.update(orderService.findById(orderFromDbId), order);
            if(updatedOrder != null && !updatedOrder.getId().isEmpty()) {
                EmployeeResponse employeeResponse = webClient.get()
                        .uri(MANAGE_SERVICE_EMPLOYEE_URI + order.getAuthorId())
                        .retrieve()
                        .bodyToMono(EmployeeResponse.class)
                        .block();
                POSResponse posResponse = webClient.get()
                        .uri(MANAGE_SERVICE_POS_URI + order.getPosId())
                        .retrieve()
                        .bodyToMono(POSResponse.class)
                        .block();
                EmployeeResponse engineerResponse = webClient.get()
                        .uri(MANAGE_SERVICE_EMPLOYEE_URI + order.getEngineerId())
                        .retrieve()
                        .bodyToMono(EmployeeResponse.class)
                        .block();
                if(employeeResponse == null || employeeResponse.getEmployeeDTOList().isEmpty()) {
                    log.error("User with id {} not found in database", order.getAuthorId());
                    return new ResponseEntity<>(new OrderResponse("User with id [" +order.getAuthorId() + "] not found in database", Collections.emptyList()), HttpStatus.OK);
                }
                if(posResponse == null || posResponse.getPosList().isEmpty()) {
                    log.error("Point Of Sales with ID {} not found in database", order.getPosId());
                    return new ResponseEntity<>(new OrderResponse("Point Of Sales with ID [" + order.getPosId()+"] not found in database", Collections.emptyList()), HttpStatus.OK);
                }
                if(engineerResponse == null || engineerResponse.getEmployeeDTOList().isEmpty()) {
                    log.error("Engineer with id {} not found in database", order.getEngineerId());
                    return new ResponseEntity<>(new OrderResponse("Engineer with id ["+ order.getEngineerId()+"] not found in database", Collections.emptyList()), HttpStatus.OK);
                }
                EmployeeDTO author = employeeResponse.getEmployeeDTOList().get(0);
                EmployeeDTO engineer = engineerResponse.getEmployeeDTOList().get(0);
                PointOfSalesDTO pos = posResponse.getPosList().get(0);

                OrderDTO updatedDTO = convertToDTO(updatedOrder);
                updatedDTO.setAuthorName(author.getFirstName() + " " + author.getLastName());
                updatedDTO.setPosShortName(pos.getShortName());
                updatedDTO.setEngineerName(engineer.getFirstName() + " " + engineer.getLastName());
                updatedDTO.setEngineerRoles(engineer.getRoles());

                return new ResponseEntity<>(new OrderResponse("OK", Collections.singletonList(updatedDTO)), HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(new OrderResponse("Can't update order, check logs", Collections.emptyList()),HttpStatus.OK);
            }
        } catch (WorkshopException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(new OrderResponse("Cant update order due to an error", Collections.emptyList()), HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<OrderResponse> delete(@PathVariable("orderId") String orderId) {
        orderService.delete(orderId);
        return new ResponseEntity<>(new OrderResponse("Order with id [" + orderId + "] were successfully deleted from database", Collections.emptyList()), HttpStatus.OK);
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
        order.setComment(orderDTO.getComment());

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
        orderDTO.setComment(order.getComment());

        return orderDTO;

    }


}
