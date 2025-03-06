package com.eloiza.tax_calculator.services;

import com.eloiza.tax_calculator.controllers.dtos.UserRequest;
import com.eloiza.tax_calculator.controllers.dtos.UserResponse;
import com.eloiza.tax_calculator.exeptions.DuplicateUsernameException;
import com.eloiza.tax_calculator.models.Role;
import com.eloiza.tax_calculator.models.User;
import com.eloiza.tax_calculator.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_ShouldReturnUserResponse_WhenUserIsValid() {
        String username = "testUser";
        UserRequest userRequest = new UserRequest(username, "password123", "ROLE_USER");
        User user = new User();
        user.setUsername(username);
        user.setPassword("password123");
        user.setRoles(Set.of(new Role("ROLE_USER")));

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        UserResponse userResponse = userService.createUser(userRequest);

        assertEquals(1L, userResponse.id());
        assertEquals(username, userResponse.username());
        assertEquals("ROLE_USER", userResponse.role());

        verify(userRepository).existsByUsername(username);
        verify(userRepository).save(user);
    }

    @Test
    public void registerUser_ShouldReturnException_whenExistingUser() {
        String username = "testUser";
        UserRequest userRequest = new UserRequest(username, "password123", "ROLE_USER");

        when(userRepository.existsByUsername("testUser")).thenReturn(true);

        RuntimeException exception = assertThrows(DuplicateUsernameException.class, () -> userService.createUser(userRequest));
        assertEquals("Usuário já cadastrado no sistema", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));

    }
}
