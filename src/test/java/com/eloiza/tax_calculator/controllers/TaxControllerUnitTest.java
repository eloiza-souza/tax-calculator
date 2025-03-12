package com.eloiza.tax_calculator.controllers;

import com.eloiza.tax_calculator.controllers.dtos.CalculateTaxRequest;
import com.eloiza.tax_calculator.controllers.dtos.CalculateTaxResponse;
import com.eloiza.tax_calculator.controllers.dtos.TaxRequest;
import com.eloiza.tax_calculator.controllers.dtos.TaxResponse;
import com.eloiza.tax_calculator.models.Tax;
import com.eloiza.tax_calculator.services.TaxService;
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
    void getAllTaxes_Success() {
        List<TaxResponse> taxes = new ArrayList<>();
        when(taxService.findAll()).thenReturn(taxes);

        ResponseEntity<List<TaxResponse>> response = taxController.getAllTaxes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taxes, response.getBody());
        verify(taxService).findAll();
    }

    @Test
    void getTaxById_Success() {
        Long id = 1L;
        TaxResponse taxResponse = new TaxResponse(id, "test_tax", "description_tax", 10.0);
        when(taxService.findById(id)).thenReturn(taxResponse);

        ResponseEntity<TaxResponse> response = taxController.getTaxById(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taxResponse, response.getBody());
        verify(taxService).findById(id);

    }

    @Test
    void addTax_Success() {
        TaxRequest taxRequest = new TaxRequest("test_tax", "description", 10.0);
        TaxResponse taxResponse = new TaxResponse(1L, "test_tax", "description", 10.0);

        when(taxService.addTax(taxRequest)).thenReturn(taxResponse);

        ResponseEntity<TaxResponse> response = taxController.addTax(taxRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(taxResponse, response.getBody());
        verify(taxService).addTax(taxRequest);
    }

    @Test
    void calculateTax() {
        Long taxId = 1L;
        Double baseValue = 100.0;
        Tax tax = new Tax();
        tax.setName("test_tax");
        tax.setRate(10.0);

        CalculateTaxRequest calculateTaxRequest = new CalculateTaxRequest(taxId, baseValue);
        CalculateTaxResponse calculateTaxResponse = new CalculateTaxResponse(tax.getName(), baseValue, tax.getRate(), baseValue * tax.getRate() / 100.0);

        when(taxService.calculateTax(calculateTaxRequest)).thenReturn(calculateTaxResponse);

        ResponseEntity<CalculateTaxResponse> response = taxController.calculateTax(calculateTaxRequest);
        assertEquals(10.0, calculateTaxResponse.taxCalculated());

    }


}
