package com.eloiza.tax_calculator.controllers;

import com.eloiza.tax_calculator.controllers.dtos.LoginRequest;
import com.eloiza.tax_calculator.controllers.dtos.LoginResponse;
import com.eloiza.tax_calculator.controllers.dtos.UserRequest;
import com.eloiza.tax_calculator.controllers.dtos.UserResponse;
import com.eloiza.tax_calculator.exceptions.DuplicateUsernameException;
import com.eloiza.tax_calculator.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserControllerUnitTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    void registerUser_Success() {
        UserRequest userRequest = new UserRequest("user_test", "password", Set.of("ROLE_USER"));
        UserResponse userResponse = new UserResponse(1L, "user_test", Set.of("ROLE_USER"));

        when(userService.createUser(userRequest)).thenReturn(userResponse);

        ResponseEntity<UserResponse> response = userController.registerUser(userRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userResponse, response.getBody());
        verify(userService).createUser(userRequest);
    }

    @Test
    void registerUser_DuplicateUsername() {
        UserRequest userRequest = new UserRequest("user_test", "password", Set.of("ROLE_USER"));
        UserResponse userResponse = new UserResponse(1L, "user_test", Set.of("ROLE_USER"));

        when(userService.createUser(userRequest)).thenThrow(new DuplicateUsernameException("Usuário já cadastrado no sistema"));

        Exception exception = assertThrows(DuplicateUsernameException.class, () -> userController.registerUser(userRequest));

        assertEquals("Usuário já cadastrado no sistema", exception.getMessage());
        verify(userService).createUser(userRequest);
    }

    @Test
    void registerUser_InvalidRequest() {
        UserRequest invalidRequest = new UserRequest("", "", Set.of("invalid"));

        when(userService.createUser(invalidRequest)).thenThrow(new IllegalArgumentException("Invalid user data"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userController.registerUser(invalidRequest);
        });

        assertEquals("Invalid user data", exception.getMessage());
        verify(userService, times(1)).createUser(invalidRequest);
    }

    @Test
    void login_Success() {
        LoginRequest loginRequest = new LoginRequest("test_user", "password");
        LoginResponse loginResponse = new LoginResponse("valid-token");

        when(userService.login(loginRequest)).thenReturn(loginResponse);

        ResponseEntity<String> response = userController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("valid-token", response.getBody());
        verify(userService, times(1)).login(loginRequest);
    }

    @Test
    void login_InvalidUsername() {
        LoginRequest invalidRequest = new LoginRequest("invalid_user", "password");

        when(userService.login(invalidRequest)).thenThrow(new UsernameNotFoundException("Usuário não encontrado!"));

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userController.login(invalidRequest);
        });

        assertEquals("Usuário não encontrado!", exception.getMessage());
        verify(userService).login(invalidRequest);
    }

    @Test
    void login_InvalidPassword() {
        LoginRequest invalidRequest = new LoginRequest("test_user", "invalid_password");

        when(userService.login(invalidRequest)).thenThrow(new UsernameNotFoundException("Senha inválida!"));

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userController.login(invalidRequest);
        });

        assertEquals("Senha inválida!", exception.getMessage());
        verify(userService).login(invalidRequest);
    }

}
