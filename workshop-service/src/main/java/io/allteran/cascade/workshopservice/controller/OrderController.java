package io.allteran.cascade.workshopservice.controller;

import io.allteran.cascade.workshopservice.domain.DeviceType;
import io.allteran.cascade.workshopservice.domain.Order;
import io.allteran.cascade.workshopservice.domain.Status;
import io.allteran.cascade.workshopservice.dto.OrderDTO;
import io.allteran.cascade.workshopservice.dto.UserDTO;
import io.allteran.cascade.workshopservice.exception.WorkshopException;
import io.allteran.cascade.workshopservice.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/workshop/order")
public class OrderController {
    private final OrderService  orderService;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderController(OrderService orderService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/list")
    public ResponseEntity<List<OrderDTO> >findAll() {
        List<Order> orders = orderService.findAll();
        if(orders != null && !orders.isEmpty()) {
            List<OrderDTO> dtos = orders.stream().map(this::convertToDTO).toList();
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{orderId}")
    public ResponseEntity<OrderDTO> findById(@PathVariable("orderId") String orderId) {
        Order order = orderService.findById(orderId);
        if(order != null && !order.getId().isEmpty()) {
            OrderDTO dto = convertToDTO(order);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{orderId}")
    public ResponseEntity<OrderDTO> update(@PathVariable("orderId") String orderFromDbId,
                                           @RequestBody OrderDTO orderDTO) {
        //TODO: REPLACE WITH CALL TO ANOTHER SERVICE TO GET USER BY ID
        UserDTO user = new UserDTO();
        Order order = convertToEntity(orderDTO);
        Order updatedOrder;
        try {
            updatedOrder = orderService.update(orderService.findById(orderFromDbId), order, user);
            if(updatedOrder != null && !updatedOrder.getId().isEmpty()) {
                OrderDTO updatedDTO = convertToDTO(updatedOrder);
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
//        order.setPosNickname(orderDTO.getPosNickname());

        order.setCustomerName(orderDTO.getCustomerName());
        order.setCustomerPhone(orderDTO.getCustomerPhone());

        order.setAuthorId(orderDTO.getAuthorId());
//        order.setAuthorName(orderDTO.getAuthorName());

        order.setEngineerId(orderDTO.getEngineerId());
//        order.setEngineerName(orderDTO.getEngineerName());
        order.setEngineerRole(orderDTO.getEngineerRole());

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
//        orderDTO.setPosNickname(order.getPosNickname());

        orderDTO.setAuthorId(order.getAuthorId());
//        orderDTO.setAuthorName(order.getAuthorName());

        orderDTO.setEngineerId(order.getEngineerId());
//        orderDTO.setEngineerName(order.getEngineerName());
        orderDTO.setEngineerRole(order.getEngineerRole());

        return orderDTO;

    }


}
