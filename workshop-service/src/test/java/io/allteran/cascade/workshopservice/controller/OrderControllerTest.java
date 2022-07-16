package io.allteran.cascade.workshopservice.controller;

import io.allteran.cascade.workshopservice.dto.OrderDTO;
import io.allteran.cascade.workshopservice.dto.OrderResponse;
import io.allteran.cascade.workshopservice.service.OrderService;
import io.allteran.cascade.workshopservice.service.StatusService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@Slf4j
class OrderControllerTest {
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private OrderService orderService;


    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void findAll() throws Exception {
//        ObjectMapper mapper = new ObjectMapper();
//        OrderDTO order = getOrderDTO();
//        String orderString = mapper.writeValueAsString(order);
//        MvcResult creationResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/workshop/order/new")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(orderString))
//                .andExpect(status().isCreated())
//                .andReturn();
//        OrderResponse creationResponse = mapper.readValue(creationResult.getResponse().getContentAsString(),
//                OrderResponse.class);
//        OrderDTO createdOrder = creationResponse.getOrderList().get(0);
//
//        Assertions.assertEquals(1, orderService.findAll().size());
//        Assertions.assertEquals(1, creationResponse.getOrderList().size());
//
//        MvcResult findAllResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/workshop/order/{id}", createdOrder.getId()))
//                .andExpect(status().isOk())
//                .andReturn();
//        OrderResponse findAllResponse = mapper.readValue(findAllResult.getResponse().getContentAsString(),
//                OrderResponse.class);
//        Assertions.assertEquals(1, findAllResponse.getOrderList().size());
//
//        OrderDTO findAllOrder = findAllResponse.getOrderList().get(0);
//
//        Assertions.assertEquals(createdOrder.getId(), findAllOrder.getId());
//        Assertions.assertEquals(1, orderService.findAll().size());
    }

    @Test
    void findById() {
    }

    @Test
    void create() {
    }

    @Test
    void generateAcceptanceCertificate() {
    }

    @Test
    void generateRepairCertificate() {
    }

    @Test
    void downloadAcceptanceCertificate() {
    }

    @Test
    void downloadRepairCertificate() {
    }

    @Test
    void getEmployee() {
    }

    @Test
    void getPos() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    private OrderDTO getOrderDTO() {
        OrderDTO order = new OrderDTO();
        order.setId("test_id");
        order.setDeviceName("test device");
        order.setDeviceTypeId("test_type_id");
        order.setDeviceTypeName("test_type_name");
        order.setStatusId("d179baa7-91b5-4937-b03f-4cb88b68f63c");
        order.setStatusName("Принят");
        order.setSerialNumber("test_sn");
        order.setDefect("test_defect");
        order.setEquipSet("test_equip_set");
        order.setAppearance("test_appearance");
        order.setWarranty("test_warranty");
        order.setPerformedActions("test_actions");
//        order.setCreationDate(LocalDate.now());
//        order.setIssueDate(LocalDate.now());
        order.setPreliminaryPrice(2000);
        order.setServicePrice(1500);
        order.setComponentPrice(100);
        order.setMarginPrice(400);
        order.setTotalPrice(2000);
        order.setCustomerName("customer_name");
        order.setCustomerPhone("customer_phone");
        order.setEngineerId("eng_id");
        order.setEngineerRoles(Collections.singleton("simple_engineer"));
        order.setEngineerName("simple_engineer_test");
        order.setAuthorId("author_id_test");
        order.setAuthorName("test_author");

        return order;
    }
}