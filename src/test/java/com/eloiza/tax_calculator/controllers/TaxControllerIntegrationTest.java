package com.eloiza.tax_calculator.controllers;

import com.eloiza.tax_calculator.controllers.dtos.TaxRequest;
import com.eloiza.tax_calculator.controllers.dtos.TaxResponse;
import com.eloiza.tax_calculator.controllers.dtos.UserRequest;
import com.eloiza.tax_calculator.controllers.dtos.UserResponse;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
    void getAllTaxes_success() throws Exception {
        mockMvc.perform(get("/api/tax/tipos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getTaxById_withExistingId() throws Exception {
        mockMvc.perform(get("/api/tax/tipos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void addTax_Success() throws Exception {
        String taxRequestJson = """
                    {
                        "name": "tax_test",
                        "description": "description_tax",
                        "rate": 0.1
                    }
                """;
        TaxResponse userResponse = new TaxResponse(1L, "tax_test", "description_tax", 0.1);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tax/tipos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taxRequestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("tax_test"))
                .andExpect(jsonPath("$.description").value("description_tax"))
                .andExpect((jsonPath("$.rate").value(0.1)));

    }

}