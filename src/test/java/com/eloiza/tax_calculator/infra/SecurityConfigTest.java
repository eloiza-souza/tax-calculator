package com.eloiza.tax_calculator.infra;

import com.eloiza.tax_calculator.infra.jwt.JwtAuthenticationEntryPoint;
import com.eloiza.tax_calculator.infra.jwt.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SecurityConfigTest {

    private SecurityConfig securityConfig;

    @Mock
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Mock
    private JwtAuthenticationFilter authenticationFilter;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityConfig = new SecurityConfig(authenticationEntryPoint, authenticationFilter);
    }

    @Test
    void passwordEncoder_success() {
        PasswordEncoder passwordEncoder = SecurityConfig.passwordEncoder();

        assertThat(passwordEncoder).isNotNull();
        assertThat(passwordEncoder).isInstanceOf(PasswordEncoder.class);
    }

    @Test
    void securityFilterChain_success() throws Exception {
        HttpSecurity httpSecurity = mock(HttpSecurity.class);
        DefaultSecurityFilterChain mockFilterChain = mock(DefaultSecurityFilterChain.class);

        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(mockFilterChain);

        SecurityFilterChain securityFilterChain = securityConfig.securityFilterChain(httpSecurity);

        assertThat(securityFilterChain).isNotNull();
    }

    @Test
    void authenticationManager() throws Exception {
        AuthenticationManager mockAuthenticationManager = mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(mockAuthenticationManager);

        AuthenticationManager authenticationManager = securityConfig.authenticationManager(authenticationConfiguration);

        assertThat(authenticationManager).isNotNull();
        assertThat(authenticationManager).isEqualTo(mockAuthenticationManager);
    }
}