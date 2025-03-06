package com.eloiza.tax_calculator.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenRegisterUser_shouldReturnCreatedUser() throws Exception {
        String userRequestJson = """
                    {
                        "username": "user_test",
                        "password": "senhaSegura",
                        "role": "USER"
                    }
                """;
        mockMvc.perform(post("/api/tax/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("user_test"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void whenUserLogin_shouldReturnToken() throws Exception {
        String userLogin = """
                    {
                        "username": "test_user";
                        "password": "password";
                    }
                """;
        mockMvc.perform(post("/api/tax/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userLogin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(any(String.class)));
    }

}

