package com.eloiza.tax_calculator.auth;

import com.eloiza.tax_calculator.auth.jwt.JwtAuthenticationEntryPoint;
import com.eloiza.tax_calculator.auth.jwt.JwtAuthenticationFilter;
import com.eloiza.tax_calculator.auth.jwt.JwtTokenProvider;
import com.eloiza.tax_calculator.services.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SecurityConfigIntegrationTest {

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Test
    void passwordEncoder_shouldReturnValidPasswordEncoder() {
        // Testa se o PasswordEncoder é configurado corretamente
        PasswordEncoder passwordEncoder = SecurityConfig.passwordEncoder();
        assertThat(passwordEncoder).isNotNull();
        assertThat(passwordEncoder).isInstanceOf(PasswordEncoder.class);
    }

    @Test
    void securityFilterChain_shouldReturnValidSecurityFilterChain() throws Exception {
        // Testa se o SecurityFilterChain é configurado corretamente
        SecurityFilterChain securityFilterChain = securityConfig.securityFilterChain(null); // Substitua null por um HttpSecurity válido, se necessário
        assertThat(securityFilterChain).isNotNull();
    }

    @Test
    void authenticationManager_shouldReturnValidAuthenticationManager() throws Exception {
        // Testa se o AuthenticationManager é configurado corretamente
        AuthenticationManager authenticationManager = securityConfig.authenticationManager(authenticationConfiguration);
        assertThat(authenticationManager).isNotNull();
    }

    @Configuration
    static class TestConfig {

        @Bean
        public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
            return new JwtAuthenticationEntryPoint();
        }

        @Bean
        public JwtTokenProvider jwtTokenProvider() {
            // Retorne uma instância válida de JwtTokenProvider
            return new JwtTokenProvider("secretKey", 3600000); // Substitua pelos valores reais
        }

        @Bean
        public CustomUserDetailsService customUserDetailsService() {
            // Retorne uma instância válida de CustomUserDetailsService
            return new CustomUserDetailsService();
        }

        @Bean
        public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, CustomUserDetailsService customUserDetailsService) {
            return new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService);
        }

        @Bean
        public SecurityConfig securityConfig(JwtAuthenticationEntryPoint authenticationEntryPoint, JwtAuthenticationFilter authenticationFilter) {
            return new SecurityConfig(authenticationEntryPoint, authenticationFilter);
        }
    }
}