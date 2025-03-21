package com.eloiza.tax_calculator.services;

import com.eloiza.tax_calculator.controllers.dtos.LoginRequest;
import com.eloiza.tax_calculator.controllers.dtos.LoginResponse;
import com.eloiza.tax_calculator.controllers.dtos.UserRequest;
import com.eloiza.tax_calculator.controllers.dtos.UserResponse;
import com.eloiza.tax_calculator.exceptions.DuplicateUsernameException;
import com.eloiza.tax_calculator.auth.jwt.JwtTokenProvider;
import com.eloiza.tax_calculator.mappers.UserMapper;
import com.eloiza.tax_calculator.models.Role;
import com.eloiza.tax_calculator.models.User;
import com.eloiza.tax_calculator.repositories.RoleRepository;
import com.eloiza.tax_calculator.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    private AuthenticationService authenticationService;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserMapper userMapper;

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

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername(username);
        savedUser.setPassword(encodedPassword);
        savedUser.setRoles(Set.of(role));


        UserResponse userResponse = new UserResponse(1L, username, userRequest.role());

        when(bCryptPasswordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(roleRepository.findByName(roleName)).thenReturn(java.util.Optional.of(role));
        when(userMapper.toEntity(userRequest, encodedPassword, user.getRoles())).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toResponse(savedUser)).thenReturn(userResponse);

        UserResponse response = userService.createUser(userRequest);

        assertEquals(1L, response.id());
        assertEquals(username, response.username());
        assertTrue(response.role().contains(roleName));

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
        LoginResponse expectedResponse = new LoginResponse("token");

        when(authenticationService.authenticate(loginRequest)).thenReturn(expectedResponse);

        LoginResponse response = userService.login(loginRequest);

        assertEquals("token", response.token());
        verify(authenticationService).authenticate(loginRequest);
    }

    @Test
    public void login_InvalidUsername() {
        LoginRequest loginRequest = new LoginRequest("invalid_user", "password");

        when(authenticationService.authenticate(loginRequest))
                .thenThrow(new UsernameNotFoundException("Usuário não encontrado!"));

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.login(loginRequest);
        });

        assertEquals("Usuário não encontrado!", exception.getMessage());
        verify(authenticationService).authenticate(loginRequest);
    }

    @Test
    void login_InvalidPassword() {
        LoginRequest loginRequest = new LoginRequest("test_user", "wrong_password");

        when(authenticationService.authenticate(loginRequest))
                .thenThrow(new UsernameNotFoundException("Senha inválida!"));

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.login(loginRequest);
        });

        assertEquals("Senha inválida!", exception.getMessage());
        verify(authenticationService).authenticate(loginRequest);
    }
}
