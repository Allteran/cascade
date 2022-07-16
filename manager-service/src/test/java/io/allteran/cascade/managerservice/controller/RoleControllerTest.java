package io.allteran.cascade.managerservice.controller;

import io.allteran.cascade.managerservice.dto.RoleDTO;
import io.allteran.cascade.managerservice.dto.RoleResponse;
import io.allteran.cascade.managerservice.service.RoleService;
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
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@Slf4j
class RoleControllerTest {
    @Container
    static PostgreSQLContainer dbContainer = new PostgreSQLContainer("postgres:12.11");
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RoleService roleService;
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", dbContainer::getJdbcUrl);
        registry.add("spring.datasource.username", dbContainer::getUsername);
        registry.add("spring.datasource.password", dbContainer::getPassword);
    }

    @Test
    void create() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        RoleDTO role = getRoleDTO();
        String roleString = mapper.writeValueAsString(role);

        MvcResult createdResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/manage/employee-role/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(roleString))
                .andExpect(status().isCreated())
                .andReturn();

        RoleResponse createdResponse = mapper.readValue(createdResult.getResponse().getContentAsString(), RoleResponse.class);
        RoleDTO createdRole = createdResponse.getRoles().get(0);

        Assertions.assertEquals(role.getName(), createdRole.getName());
        Assertions.assertEquals(1, roleService.findAll().size());
        Assertions.assertEquals(createdRole.getId(), roleService.findById(createdRole.getId()).getId());
    }

    @Test
    void findAll() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        RoleDTO role = getRoleDTO();
        String roleString = mapper.writeValueAsString(role);

        MvcResult createdResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/manage/employee-role/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(roleString))
                .andExpect(status().isCreated())
                .andReturn();

        RoleResponse createdResponse = mapper.readValue(createdResult.getResponse().getContentAsString(), RoleResponse.class);
        RoleDTO createdRole = createdResponse.getRoles().get(0);

        Assertions.assertEquals(role.getName(), createdRole.getName());
        Assertions.assertEquals(1, roleService.findAll().size());
        Assertions.assertEquals(createdRole.getId(), roleService.findById(createdRole.getId()).getId());

        MvcResult findAllResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/manage/employee-role/list"))
                .andExpect(status().isOk())
                .andReturn();
        RoleResponse findAllResponse = mapper.readValue(findAllResult.getResponse().getContentAsString(), RoleResponse.class);
        //check if we got find anything
        Assertions.assertEquals(1, findAllResponse.getRoles().size());

        RoleDTO findAllRole = findAllResponse.getRoles().get(0);

        Assertions.assertEquals(findAllRole.getId(), createdRole.getId(), "Created role's ID and found role's id DONT matches");
        Assertions.assertEquals(1, roleService.findAll().size(), "Wrong number of roles in DB");

    }

    @Test
    void findById() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        RoleDTO role = getRoleDTO();
        String roleString = mapper.writeValueAsString(role);

        MvcResult createdResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/manage/employee-role/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(roleString))
                .andExpect(status().isCreated())
                .andReturn();

        RoleResponse createdResponse = mapper.readValue(createdResult.getResponse().getContentAsString(), RoleResponse.class);
        RoleDTO createdRole = createdResponse.getRoles().get(0);

        Assertions.assertEquals(role.getName(), createdRole.getName());
        Assertions.assertEquals(1, roleService.findAll().size());
        Assertions.assertEquals(createdRole.getId(), roleService.findById(createdRole.getId()).getId());

        MvcResult findByIdResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/manage/employee-role/{id}", createdRole.getId()))
                .andExpect(status().isOk())
                .andReturn();
        RoleResponse findByIdResponse = mapper.readValue(findByIdResult.getResponse().getContentAsString(), RoleResponse.class);
        //check if we got find anything
        Assertions.assertEquals(1, findByIdResponse.getRoles().size());

        RoleDTO findAllRole = findByIdResponse.getRoles().get(0);

        Assertions.assertEquals(findAllRole.getId(), createdRole.getId(), "Created role's ID and found role's id DONT matches");
        Assertions.assertEquals(1, roleService.findAll().size(), "Wrong number of roles in DB");
    }

    @Test
    void update() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        RoleDTO role = getRoleDTO();
        String roleString = mapper.writeValueAsString(role);

        MvcResult createdResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/manage/employee-role/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(roleString))
                .andExpect(status().isCreated())
                .andReturn();

        RoleResponse createdResponse = mapper.readValue(createdResult.getResponse().getContentAsString(), RoleResponse.class);
        RoleDTO createdRole = createdResponse.getRoles().get(0);

        Assertions.assertEquals(role.getName(), createdRole.getName());
        Assertions.assertEquals(1, roleService.findAll().size());
        Assertions.assertEquals(createdRole.getId(), roleService.findById(createdRole.getId()).getId());

        createdRole.setName("changed_name");

        String createdRoleString = mapper.writeValueAsString(createdRole);

        MvcResult updateResult = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/manage/employee-role/update/{id}", createdRole.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(createdRoleString))
                .andExpect(status().isOk())
                .andReturn();

        RoleResponse updateResponse = mapper.readValue(updateResult.getResponse().getContentAsString(), RoleResponse.class);
        RoleDTO updatedRole = updateResponse.getRoles().get(0);

        Assertions.assertEquals(1, roleService.findAll().size(), "Number of roles in DB isn't 1, but should be");
        Assertions.assertEquals(updatedRole.getId(), createdRole.getId(), "ID of updatedRole and ID of createdRole dont match");
        Assertions.assertNotEquals(updatedRole.getName(), role.getName(), "Role hasn't been updated");
    }

    @Test
    void delete() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        RoleDTO role = getRoleDTO();
        String roleString = mapper.writeValueAsString(role);

        MvcResult createdResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/manage/employee-role/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(roleString))
                .andExpect(status().isCreated())
                .andReturn();

        RoleResponse createdResponse = mapper.readValue(createdResult.getResponse().getContentAsString(), RoleResponse.class);
        RoleDTO createdRole = createdResponse.getRoles().get(0);

        Assertions.assertEquals(role.getName(), createdRole.getName());
        Assertions.assertEquals(1, roleService.findAll().size());
        Assertions.assertEquals(createdRole.getId(), roleService.findById(createdRole.getId()).getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/manage/employee-role/delete/{id}", createdRole.getId()))
                .andExpect(status().isOk());

        Assertions.assertEquals(0, roleService.findAll().size());
    }

    private RoleDTO getRoleDTO() {
        return new RoleDTO("test_id", "test_name");
    }
}