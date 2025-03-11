package com.eloiza.tax_calculator.services;

import com.eloiza.tax_calculator.models.CustomUserDetails;
import com.eloiza.tax_calculator.models.Role;
import com.eloiza.tax_calculator.models.User;
import com.eloiza.tax_calculator.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void loadUserByUsername_success() {
        User mockUser = new User();
        mockUser.setUsername("testUser");
        mockUser.setPassword("password");
        mockUser.setRoles(Set.of(new Role("ROLE_USER")));

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));

        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername("testUser");

        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
        verify(userRepository).findByUsername("testUser");
    }


    @Test
    void loadUserByUsername_UserNotFound() {
        String nonExistentUsername = "nonExistentUser";
        when(userRepository.findByUsername(nonExistentUsername)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(nonExistentUsername);
        });

        assertEquals("Usuário não encontrado!", exception.getMessage());
        verify(userRepository).findByUsername(nonExistentUsername);
    }

    @Test
    @SuppressWarnings("unchecked")
    void getAuthorities_success() throws Exception {
        User mockUser = new User();
        mockUser.setRoles(Set.of(new Role("ROLE_ADMIN"), new Role("ROLE_USER")));

        Method getAuthoritiesMethod = CustomUserDetailsService.class.getDeclaredMethod("getAuthorities", User.class);
        getAuthoritiesMethod.setAccessible(true);

        Collection<? extends GrantedAuthority> authorities = (Collection<? extends GrantedAuthority>) getAuthoritiesMethod.invoke(customUserDetailsService, mockUser);

        assertNotNull(authorities);
        assertEquals(2, authorities.size());
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }


    @Test
    @SuppressWarnings("unchecked")
    void getAuthorities_NoRoles() throws Exception {
        User mockUser = new User();
        mockUser.setRoles(Set.of());

        Method getAuthoritiesMethod = CustomUserDetailsService.class.getDeclaredMethod("getAuthorities", User.class);
        getAuthoritiesMethod.setAccessible(true);

        Collection<? extends GrantedAuthority> authorities = (Collection<? extends GrantedAuthority>) getAuthoritiesMethod.invoke(customUserDetailsService, mockUser);

        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }
}