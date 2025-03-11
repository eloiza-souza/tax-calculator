package com.eloiza.tax_calculator.services;

import com.eloiza.tax_calculator.controllers.dtos.LoginRequest;
import com.eloiza.tax_calculator.controllers.dtos.LoginResponse;
import com.eloiza.tax_calculator.infra.jwt.JwtTokenProvider;
import com.eloiza.tax_calculator.models.CustomUserDetails;
import com.eloiza.tax_calculator.models.Role;
import com.eloiza.tax_calculator.models.User;
import com.eloiza.tax_calculator.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticate_Success() {
        LoginRequest loginRequest = new LoginRequest("test_user", "password");

        User user = new User();
        user.setUsername("test_user");
        user.setPassword("encodedPassword");
        Role role = new Role("ROLE_USER");
        user.setRoles(Set.of(role));

        CustomUserDetails userDetails = new CustomUserDetails(user);

        when(userRepository.findByUsername("test_user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(customUserDetailsService.loadUserByUsername("test_user")).thenReturn(userDetails);
        when(jwtTokenProvider.generateAccessToken(any())).thenReturn("token");

        LoginResponse response = authenticationService.authenticate(loginRequest);

        assertEquals("token", response.token());
        verify(userRepository).findByUsername("test_user");
        verify(passwordEncoder).matches("password", "encodedPassword");
        verify(jwtTokenProvider).generateAccessToken(any());
    }

    @Test
    void authenticate_InvalidUsername() {
        LoginRequest loginRequest = new LoginRequest("invalid_user", "password");

        when(userRepository.findByUsername("invalid_user")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            authenticationService.authenticate(loginRequest);
        });

        assertEquals("Usuário não encontrado!", exception.getMessage());
        verify(userRepository).findByUsername("invalid_user");
        verifyNoInteractions(passwordEncoder, jwtTokenProvider);
    }

    @Test
    void authenticate_InvalidPassword() {
        LoginRequest loginRequest = new LoginRequest("test_user", "wrong_password");
        User user = new User();
        user.setUsername("test_user");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("test_user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong_password", "encodedPassword")).thenReturn(false);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            authenticationService.authenticate(loginRequest);
        });

        assertEquals("Senha inválida!", exception.getMessage());
        verify(userRepository).findByUsername("test_user");
        verify(passwordEncoder).matches("wrong_password", "encodedPassword");
        verifyNoInteractions(jwtTokenProvider);
    }
}