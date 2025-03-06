package com.eloiza.tax_calculator.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaxController.class)
@AutoConfigureMockMvc(addFilters = false) // Desabilita os filtros de segurança
public class TaxControllerTest {
    @Autowired
    private MockMvc mockMvc;

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
