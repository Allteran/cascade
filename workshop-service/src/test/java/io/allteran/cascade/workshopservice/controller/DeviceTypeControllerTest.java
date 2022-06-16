package io.allteran.cascade.workshopservice.controller;

import io.allteran.cascade.workshopservice.domain.DeviceType;
import io.allteran.cascade.workshopservice.dto.DeviceTypeDTO;
import io.allteran.cascade.workshopservice.service.DeviceTypeService;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@Slf4j
class DeviceTypeControllerTest {
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DeviceTypeService deviceTypeService;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void createAndCheck() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeviceTypeDTO typeDTO = getDeviceTypeDTO();
        String typeDTOString = mapper.writeValueAsString(typeDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/workshop/device-type/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(typeDTOString))
                .andExpect(status().isCreated());

        Assertions.assertEquals(1, deviceTypeService.findAll().size());
    }

    @Test
    void findAll() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeviceTypeDTO type = getDeviceTypeDTO();
        String typeString = mapper.writeValueAsString(type);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/workshop/device-type/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(typeString))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/workshop/device-type/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(typeString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        Assertions.assertEquals(1, deviceTypeService.findAll().size());

    }

    @Test
    void findById() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeviceTypeDTO type = getDeviceTypeDTO();
        String typeString = mapper.writeValueAsString(type);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/workshop/device-type/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(typeString))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        log.debug("POST request to device-type/new done, response is {}", responseString);

        DeviceTypeDTO responseDTO = mapper.readValue(responseString, DeviceTypeDTO.class);


        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/workshop/device-type/{id}", responseDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(typeString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void update() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeviceTypeDTO type = getDeviceTypeDTO();
        String typeString = mapper.writeValueAsString(type);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/workshop/device-type/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(typeString))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        log.debug("POST request to device-type/new done, response is {}", responseString);

        DeviceTypeDTO createResponseDTO = mapper.readValue(responseString, DeviceTypeDTO.class);
        
        DeviceTypeDTO updateDTO = new DeviceTypeDTO();
        updateDTO.setId(createResponseDTO.getId());
        updateDTO.setName("changed name");
        
        String updateStringDTO = mapper.writeValueAsString(updateDTO);

        MvcResult updateMvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/workshop/device-type/update/{id}", createResponseDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateStringDTO))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        DeviceTypeDTO updateResponseDTO = mapper.readValue(updateMvcResult.getResponse().getContentAsString(), DeviceTypeDTO.class);

        Assertions.assertEquals(createResponseDTO.getId(), updateResponseDTO.getId());
        Assertions.assertNotEquals(createResponseDTO.getName(), updateResponseDTO.getName());

        Assertions.assertEquals(1, deviceTypeService.findAll().size());
    }

    @Test
    void delete() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DeviceTypeDTO type = getDeviceTypeDTO();
        String typeString = mapper.writeValueAsString(type);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/workshop/device-type/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(typeString))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        log.debug("POST request to device-type/new done, response is {}", responseString);

        DeviceTypeDTO createResponseDTO = mapper.readValue(responseString, DeviceTypeDTO.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/workshop/device-type/delete/{id}", createResponseDTO.getId()))
                .andExpect(status().isOk());

        Assertions.assertEquals(0, deviceTypeService.findAll().size());
    }

    private DeviceTypeDTO getDeviceTypeDTO() {
        DeviceTypeDTO typeDTO = new DeviceTypeDTO();
        typeDTO.setId("testId");
        typeDTO.setName("TestName");
        return typeDTO;
    }
}