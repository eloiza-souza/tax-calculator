package com.eloiza.tax_calculator.controllers;

import com.eloiza.tax_calculator.infra.jwt.JwtTokenProvider;
import com.eloiza.tax_calculator.repositories.TaxRepository;
import com.eloiza.tax_calculator.repositories.UserRepository;
import com.eloiza.tax_calculator.services.CustomUserDetailsService;
import com.eloiza.tax_calculator.services.TaxService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaxController.class)
@AutoConfigureMockMvc(addFilters = false) // Desabilita os filtros de segurança
public class TaxControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaxService userService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public JwtTokenProvider jwtTokenProvider() {
            return Mockito.mock(JwtTokenProvider.class);
        }

        @Bean
        public CustomUserDetailsService customUserDetailsService() {
            return Mockito.mock(CustomUserDetailsService.class);
        }

        @Bean
        public TaxService  taxService() {
            return Mockito.mock(TaxService.class);
        }

        @Bean
        public UserRepository userRepository() {
            return Mockito.mock(UserRepository.class);
        }

        @Bean
        public TaxRepository taxRepository() {
            return Mockito.mock(TaxRepository.class);
        }
    }

    @Test
    void whenGetAllTaxes_shouldReturnListOfTaxes() throws Exception {
        mockMvc.perform(get("/api/tax/tipos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void whenGetTaxById_withExistingId_shouldReturnTax() throws Exception {
        mockMvc.perform(get("/api/tax/tipos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

}