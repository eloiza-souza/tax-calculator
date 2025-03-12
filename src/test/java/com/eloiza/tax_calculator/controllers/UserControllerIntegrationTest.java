package com.eloiza.tax_calculator.controllers;

import com.eloiza.tax_calculator.controllers.dtos.LoginRequest;
import com.eloiza.tax_calculator.controllers.dtos.LoginResponse;
import com.eloiza.tax_calculator.controllers.dtos.UserRequest;
import com.eloiza.tax_calculator.controllers.dtos.UserResponse;
import com.eloiza.tax_calculator.infra.jwt.JwtTokenProvider;
import com.eloiza.tax_calculator.repositories.UserRepository;
import com.eloiza.tax_calculator.services.CustomUserDetailsService;
import com.eloiza.tax_calculator.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(UserControllerIntegrationTest.TestConfig.class)
public class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public JwtTokenProvider jwtTokenProvider() {
            return Mockito.mock(JwtTokenProvider.class);
        }

        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }

        @Bean
        public CustomUserDetailsService customUserDetailsService() {
            return Mockito.mock(CustomUserDetailsService.class);
        }

        @Bean
        public UserRepository userRepository() {
            return Mockito.mock(UserRepository.class);
        }
    }

    @Test
    void registerUser_Success() throws Exception {
        String userRequestJson = """
                    {
                        "username": "user_test",
                        "password": "senhaSegura",
                        "role": ["ROLE_USER"]
                    }
                """;
        UserResponse userResponse = new UserResponse(1L, "user_test", Set.of("ROLE_USER"));
        when(userService.createUser(any(UserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tax/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("user_test"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"));
    }

    @Test
    void registerUser_InvalidData() throws Exception {

        String userRequestJson = """
                    {
                        "username": "",
                        "password": "",
                        "role": ["ROLE_INVALID"]
                    }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tax/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").value("A senha é obrigatória."))
                .andExpect(jsonPath("$.username").value("O nome de usuário é obrigatório."))
                .andExpect(jsonPath("$['role[]']").value("Valor inválido para o campo role. Valores permitidos: [ROLE_ADMIN, ROLE_USER]"));
    }


    @Test
    void login_Success() throws Exception {
        String userLogin = """
                    {
                        "username": "test_user",
                        "password": "password"
                    }
                """;
        LoginResponse loginResponse = new LoginResponse("valid_token");
        when(userService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        mockMvc.perform(post("/api/tax/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userLogin))
                .andExpect(status().isOk())
                .andExpect(content().string("valid_token"));
    }

    @Test
    void login_InvalidUsername() throws Exception {
        String userLogin = """
                    {
                        "username": "invalid",
                        "password": "password"
                    }
                """;
        when(userService.login(any(LoginRequest.class))).thenThrow(new UsernameNotFoundException("Usuário não encontrado!"));

        mockMvc.perform(post("/api/tax/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userLogin))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuário não encontrado!"));
    }

    @Test
    void login_WrongPassword() throws Exception {
        String userLogin = """
                    {
                        "username": "test_user",
                        "password": "wrong_password"
                    }
                """;
        when(userService.login(any(LoginRequest.class))).thenThrow(new UsernameNotFoundException("Senha inválida!"));

        mockMvc.perform(post("/api/tax/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userLogin))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Senha inválida!"));
    }
}

