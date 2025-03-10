package com.eloiza.tax_calculator.services;

import com.eloiza.tax_calculator.controllers.dtos.LoginResponse;
import com.eloiza.tax_calculator.controllers.dtos.LoginRequest;
import com.eloiza.tax_calculator.controllers.dtos.UserRequest;
import com.eloiza.tax_calculator.controllers.dtos.UserResponse;
import com.eloiza.tax_calculator.exeptions.DuplicateUsernameException;
import com.eloiza.tax_calculator.infra.jwt.JwtTokenProvider;
import com.eloiza.tax_calculator.models.CustomUserDetails;
import com.eloiza.tax_calculator.models.Role;
import com.eloiza.tax_calculator.models.User;
import com.eloiza.tax_calculator.repositories.RoleRepository;
import com.eloiza.tax_calculator.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private Authentication authentication;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_success() {
        String username = "testUser";
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword";
        String roleName = "ROLE_USER";

        UserRequest userRequest = new UserRequest(username, rawPassword, Set.of(roleName));
        Role role = new Role(roleName);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRoles(Set.of(role));

        when(bCryptPasswordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(roleRepository.findByName(roleName)).thenReturn(java.util.Optional.of(role));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        UserResponse userResponse = userService.createUser(userRequest);

        assertEquals(1L, userResponse.id());
        assertEquals(username, userResponse.username());
        assertTrue(userResponse.role().contains(roleName));

        verify(userRepository).existsByUsername(username);
        verify(bCryptPasswordEncoder).encode(rawPassword);
        verify(roleRepository).findByName(roleName);
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void registerUser_existingUser() {
        String username = "testUser";
        UserRequest userRequest = new UserRequest(username, "password123", Set.of("ROLE_USER"));

        when(userRepository.existsByUsername("testUser")).thenReturn(true);

        DuplicateUsernameException exception = assertThrows(DuplicateUsernameException.class, () -> userService.createUser(userRequest));
        assertEquals("Usuário já cadastrado no sistema", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));

    }

    @Test
    public void login_Success() {
        LoginRequest loginRequest = new LoginRequest("test_user", "password");
        User user = new User();
        user.setUsername("test_user");
        user.setPassword("encodedPassword");
        user.setRoles(Set.of(new Role("ROLE_USER")));

        CustomUserDetails userDetails = new CustomUserDetails(user);

        when(userRepository.findByUsername("test_user")).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(customUserDetailsService.loadUserByUsername("test_user")).thenReturn(userDetails);
        when(jwtTokenProvider.generateAccessToken(any(Authentication.class))).thenReturn("token");

        LoginResponse response = userService.login(loginRequest);

        assertEquals("token", response.token());
        verify(userRepository).findByUsername("test_user");
        verify(bCryptPasswordEncoder).matches("password", "encodedPassword");
        verify(jwtTokenProvider).generateAccessToken(any(Authentication.class));

    }

    @Test
    public void login_InvalidUsername() {
        LoginRequest loginRequest = new LoginRequest("invalid_user", "password");

        when(userRepository.findByUsername("invalid_user")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.login(loginRequest);
        });

        assertEquals("Usuário não encontrado!", exception.getMessage());
        verify(userRepository).findByUsername("invalid_user");
        verifyNoInteractions(bCryptPasswordEncoder, jwtTokenProvider);
    }

    @Test
    void login_InvalidPassword() {
        LoginRequest loginRequest = new LoginRequest("test_user", "wrong_password");
        User user = new User();
        user.setUsername("test_user");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("test_user")).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches("wrong_password", "encodedPassword")).thenReturn(false);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.login(loginRequest);
        });

        assertEquals("Senha inválida!", exception.getMessage());
        verify(userRepository).findByUsername("test_user");
        verify(bCryptPasswordEncoder).matches("wrong_password", "encodedPassword");
        verifyNoInteractions(jwtTokenProvider);
    }

}
