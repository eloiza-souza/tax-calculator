package com.eloiza.tax_calculator.auth;

import com.eloiza.tax_calculator.auth.jwt.JwtTokenProvider;
import com.eloiza.tax_calculator.controllers.TaxController;
import com.eloiza.tax_calculator.controllers.UserController;
import com.eloiza.tax_calculator.controllers.dtos.CalculateTaxRequest;
import com.eloiza.tax_calculator.controllers.dtos.CalculateTaxResponse;
import com.eloiza.tax_calculator.controllers.dtos.TaxRequest;
import com.eloiza.tax_calculator.services.TaxService;
import com.eloiza.tax_calculator.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SecurityConfigIntegrationTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private TaxService taxService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserController userController;

    @InjectMocks
    private TaxController taxController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController, taxController)
                .build();
    }

    @Test
    void whenRegisterUser_thenShouldReturnCreated() throws Exception {
        when(userService.createUser(any())).thenReturn(null);

        String userRequest = """
                {
                    "username": "uniqueuser",
                    "password": "password123",
                    "role": ["ROLE_USER"]
                }
                """;

        mockMvc.perform(post("/api/tax/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequest))
                .andExpect(status().isCreated());
    }

    @Test
    void whenLoginUser_thenShouldReturnOk() throws Exception {
        when(userService.login(any())).thenReturn(null);

        String loginRequest = """
                {
                    "username": "uniqueuser",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/tax/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk());
    }

    @Test
    void whenAddTax_thenShouldReturnCreated() throws Exception {
        when(taxService.addTax(any(TaxRequest.class))).thenReturn(null);

        String taxRequest = """
                {
                    "name": "UniqueTaxType",
                    "description": "Test Tax",
                    "rate": 10.0
                }
                """;

        mockMvc.perform(post("/api/tax/tipos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taxRequest))
                .andExpect(status().isCreated());
    }

    @Test
    void whenCalculateTax_thenShouldReturnOk() throws Exception {
        when(taxService.calculateTax(any(CalculateTaxRequest.class)))
                .thenReturn(new CalculateTaxResponse("TaxType", 100.0, 10.0, 10.0));

        String calculateTaxRequest = """
                {
                    "taxId": 1,
                    "baseValue": 100.0
                }
                """;

        mockMvc.perform(post("/api/tax/calculo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(calculateTaxRequest))
                .andExpect(status().isOk());
    }
}