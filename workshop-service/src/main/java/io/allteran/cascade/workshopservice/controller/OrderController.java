package io.allteran.cascade.workshopservice.controller;

import io.allteran.cascade.workshopservice.domain.Order;
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
        return modelMapper.map(orderDTO, Order.class);
    }

    private OrderDTO convertToDTO(Order order) {
        return modelMapper.map(order, OrderDTO.class);
    }
}
