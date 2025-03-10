package com.eloiza.tax_calculator.controllers;

import com.eloiza.tax_calculator.controllers.dtos.TaxRequest;
import com.eloiza.tax_calculator.controllers.dtos.TaxResponse;
import com.eloiza.tax_calculator.controllers.dtos.UserResponse;
import com.eloiza.tax_calculator.services.TaxService;
import com.eloiza.tax_calculator.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TaxControllerUnitTest {

    private TaxService taxService;
    private TaxController taxController;

    @BeforeEach
    void setUp() {
        taxService = mock(TaxService.class);
        taxController = new TaxController(taxService);
    }
    @Test
    void getAllTaxes_Success(){
        List<TaxResponse> taxes = new ArrayList<>();
        when(taxService.findAll()).thenReturn(taxes);

        ResponseEntity<List<TaxResponse>> response = taxController.getAllTaxes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taxes, response.getBody());
        verify(taxService).findAll();
    }

    @Test
    void getTaxById_Success(){
        Long id = 1L;
        TaxResponse taxResponse = new TaxResponse(id, "test_tax", "description_tax", 0.1);
        when(taxService.findById(id)).thenReturn(taxResponse);

        ResponseEntity<TaxResponse> response = taxController.getTaxById(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taxResponse, response.getBody());
        verify(taxService).findById(id);

    }

    @Test
    void addTax_Success(){
        TaxRequest taxRequest = new TaxRequest("test_tax", "description", 0.1);
        TaxResponse taxResponse = new TaxResponse(1L,"test_tax", "description", 0.1);

        when(taxService.addTax(taxRequest)).thenReturn(taxResponse);

        ResponseEntity<TaxResponse> response = taxController.addTax(taxRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(taxResponse, response.getBody());
        verify(taxService).addTax(taxRequest);
    }




}
