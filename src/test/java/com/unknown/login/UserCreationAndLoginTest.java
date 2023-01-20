package com.unknown.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unknown.dto.CreateUserRequest;
import com.unknown.dto.LoginUserRequest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class UserCreationAndLoginTest {
    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    @Test
    @Order(1)
    public void userShouldBeCreated() throws Exception {
        var createUserRequest = new CreateUserRequest("Bill","Testing","test@email.com","Password@123","254708123456");

     mockMvc.perform( post("/api/auth/signup")
             .contentType(MediaType.APPLICATION_JSON)
             .content(objectMapper.writeValueAsString(createUserRequest))
     ).andExpect(status().isCreated());
    }
    @Test
    @Order(2)
    public void userShouldLogin() throws Exception {
        var loginUserRequest = new LoginUserRequest("test@email.com","Password@123");

        mockMvc.perform( post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginUserRequest))
        ).andExpect(status().isOk());
    }
}
