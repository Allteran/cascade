package io.allteran.cascade.workshopservice.controller;

import io.allteran.cascade.workshopservice.dto.StatusDTO;
import io.allteran.cascade.workshopservice.dto.StatusResponse;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@Slf4j
class StatusControllerTest {
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StatusService statusService;


    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }


    @Test
    void findById() {

    }

    @Test
    void findAll() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        StatusDTO statusDTO = getStatusDTO();
        String statusString = mapper.writeValueAsString(statusDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/workshop/status/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(statusString))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    StatusResponse response = mapper.readValue(result.getResponse().getContentAsString(),
                            StatusResponse.class);
                    Assertions.assertEquals(1, response.getStatusList().size());
                    Assertions.assertEquals(statusDTO.getName(), response.getStatusList().get(0).getName());
                });
        Assertions.assertEquals(1, statusService.findAll().size());
    }

    @Test
    void create() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        StatusDTO statusDTO = getStatusDTO();
        String statusDTOString = mapper.writeValueAsString(statusDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/workshop/status/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(statusDTOString))
                .andExpect(result -> {
                    StatusResponse response = mapper.readValue(result.getResponse().getContentAsString(),
                            StatusResponse.class);
                    status().isCreated();
                    Assertions.assertEquals(statusDTO.getName(), response.getStatusList().get(0).getName());
                });
        Assertions.assertEquals(1, statusService.findAll().size());
    }

    @Test
    void update() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        StatusDTO statusDTO = getStatusDTO();
        String statusString = mapper.writeValueAsString(statusDTO);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/workshop/status/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(statusString))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    StatusResponse response = mapper.readValue(result.getResponse().getContentAsString(),
                            StatusResponse.class);
                    assertEquals(1, response.getStatusList().size());
                    assertEquals(statusDTO.getName(), response.getStatusList().get(0).getName());
                    assertNotEquals(statusDTO.getId(), response.getStatusList().get(0).getId());
                })
                .andReturn();
        Assertions.assertEquals(1, statusService.findAll().size());
        log.debug("POST request created STATUS, response is {}", mvcResult.getResponse().getContentAsString());

        StatusResponse createdResponse = mapper.readValue(mvcResult.getResponse().getContentAsString(),
                StatusResponse.class);

        StatusDTO updatedStatus = new StatusDTO();
        updatedStatus.setName("status_changed_name");
        updatedStatus.setId(createdResponse.getStatusList().get(0).getId());

        String updatedStatusString = mapper.writeValueAsString(updatedStatus);

        MvcResult updatedMvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/workshop/status/update/{id}",
                                createdResponse.getStatusList().get(0).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedStatusString))
                .andExpect(status().isAccepted())
                .andExpect(result -> {
                    StatusResponse response = mapper.readValue(result.getResponse().getContentAsString(),
                            StatusResponse.class);
                    assertEquals(updatedStatus.getName(), response.getStatusList().get(0).getName());
                    assertEquals(1, response.getStatusList().size());
                })
                .andReturn();

        StatusResponse updatedResponse = mapper.readValue(updatedMvcResult.getResponse().getContentAsString(),
                StatusResponse.class);

        Assertions.assertEquals(createdResponse.getStatusList().get(0).getId(), updatedResponse.getStatusList().get(0).getId());
        Assertions.assertNotEquals(createdResponse.getStatusList().get(0).getName(), updatedResponse.getStatusList().get(0).getName());

        Assertions.assertEquals(1, statusService.findAll().size());
    }

    @Test
    void delete() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        StatusDTO status = getStatusDTO();
        String statusString = mapper.writeValueAsString(status);


        MvcResult createResults = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/workshop/status/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(statusString))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    StatusResponse response = mapper.readValue(result.getResponse().getContentAsString(),
                            StatusResponse.class);
                    assertEquals(1, response.getStatusList().size());
                    assertEquals(status.getName(), response.getStatusList().get(0).getName());
                    assertNotEquals(status.getId(), response.getStatusList().get(0).getId());
                })
                .andReturn();

        Assertions.assertEquals(1, statusService.findAll().size());

        StatusResponse createdStatusResponse = mapper.readValue(createResults.getResponse().getContentAsString(), StatusResponse.class);
        StatusDTO createdStatus = createdStatusResponse.getStatusList().get(0);
        String createdStatusString = mapper.writeValueAsString(createdStatus);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/workshop/status/delete/{id}", createdStatus.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(createdStatusString))
                .andExpect(status().isOk());
        Assertions.assertEquals(0, statusService.findAll().size());
    }

    private StatusDTO getStatusDTO() {
        StatusDTO status = new StatusDTO();
        status.setId("test_id");
        status.setName("test_name");
        return status;
    }
}