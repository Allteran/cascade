package io.allteran.cascade.workshopservice.service;

import io.allteran.cascade.workshopservice.domain.Order;
import io.allteran.cascade.workshopservice.domain.Status;
import io.allteran.cascade.workshopservice.exception.NotFoundException;
import io.allteran.cascade.workshopservice.repo.OrderRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private static final LocalDate DEFAULT_DATE = LocalDate.of(1,1,1);
    @Value("${order.status.unpaid}")
    private String STATUS_UNPAID_ID;
    @Value("${order.status.created}")
    private String STATUS_CREATED_ID;
    @Value("${order.status.paid}")
    private String STATUS_PAID_ID;
    @Value("${role.head-engineer}")
    private String ROLE_HEAD_ENGINEER;

    private final OrderRepository orderRepository;
    private final StatusService statusService;

    @Autowired
    public OrderService(OrderRepository orderRepository, StatusService statusService) {
        this.orderRepository = orderRepository;
        this.statusService = statusService;
    }

    public List<Order> findAll() {
        return orderRepository.findAll(Sort.by("issueDate").descending());
    }

    public Order findById(String id) {
        if(orderRepository.findById(id).isPresent()) {
            return orderRepository.findById(id).get();
        } else {
            throw new NotFoundException("Order with id " + id + " not found in database");
        }
    }

    public Order create(Order order) {
        order.setId(UUID.randomUUID().toString());

        order.setCreationDate(LocalDate.now());
        order.setIssueDate(DEFAULT_DATE);

        Status statusCreated = statusService.findById(STATUS_CREATED_ID);
        order.setStatus(statusCreated);
        return orderRepository.save(order);
    }

    public Order update (Order orderFromDb, Order order) {
        BeanUtils.copyProperties(order, orderFromDb, "id");

        orderFromDb.setServicePrice(orderFromDb.getTotalPrice() - orderFromDb.getComponentPrice() - orderFromDb.getMarginPrice());
        double directorProfit;
        double headEngineerProfit;
        double engineerProfit;
        double managerProfit;
        if(order.getEngineerRoles().contains(ROLE_HEAD_ENGINEER)) {
            directorProfit = orderFromDb.getServicePrice() * 0.45 + orderFromDb.getMarginPrice() / 2;
            headEngineerProfit = directorProfit + orderFromDb.getComponentPrice();
            engineerProfit = 0;
            managerProfit = orderFromDb.getServicePrice() + 0.1;
        } else {
            directorProfit = orderFromDb.getServicePrice() * 0.34 + orderFromDb.getMarginPrice() / 2;
            headEngineerProfit = directorProfit + orderFromDb.getComponentPrice();
            engineerProfit = orderFromDb.getServicePrice() * 0.3;
            managerProfit = orderFromDb.getServicePrice() * 0.01;
        }
        orderFromDb.setDirectorProfit(directorProfit);
        orderFromDb.setHeadEngineerProfit(headEngineerProfit);
        orderFromDb.setEngineerProfit(engineerProfit);
        orderFromDb.setManagerProfit(managerProfit);

        if(orderFromDb.getIssueDate() == null) {
            orderFromDb.setIssueDate(DEFAULT_DATE);
        }
        if (orderFromDb.getIssueDate().isAfter(DEFAULT_DATE)) {
            Status paid = statusService.findById(STATUS_PAID_ID);
            orderFromDb.setStatus(paid);
        }
        return orderRepository.save(orderFromDb);
    }

    public void delete(String id) {
        orderRepository.delete(findById(id));
    }
}
