package com.eloiza.tax_calculator.services;

import com.eloiza.tax_calculator.controllers.dtos.CalculateTaxRequest;
import com.eloiza.tax_calculator.controllers.dtos.CalculateTaxResponse;
import com.eloiza.tax_calculator.controllers.dtos.TaxRequest;
import com.eloiza.tax_calculator.controllers.dtos.TaxResponse;
import com.eloiza.tax_calculator.exceptions.TaxNotFoundException;
import com.eloiza.tax_calculator.mappers.TaxMapper;
import com.eloiza.tax_calculator.models.Tax;
import com.eloiza.tax_calculator.repositories.TaxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaxServiceTest {

    @Mock
    private TaxRepository taxRepository;

    @Mock
    private TaxMapper taxMapper;

    @InjectMocks
    private TaxServiceImpl taxService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllTaxes_TaxExist() {
        Tax tax1 = new Tax();
        tax1.setId(1L);
        tax1.setName("test_tax1");
        tax1.setDescription("description_1");
        tax1.setRate(0.1);

        Tax tax2 = new Tax();
        tax2.setId(2L);
        tax2.setName("test_tax2");
        tax2.setDescription("description_2");
        tax2.setRate(0.2);

        TaxResponse taxResponse1 = new TaxResponse(1L, "test_tax1", "description_1", 0.1);
        TaxResponse taxResponse2 = new TaxResponse(2L, "test_tax2", "description_2", 0.15);
        when(taxRepository.findAll()).thenReturn(List.of(tax1, tax2));
        when(taxMapper.toResponse(tax1)).thenReturn(taxResponse1);
        when(taxMapper.toResponse(tax2)).thenReturn(taxResponse2);

        List<TaxResponse> response = taxService.findAll();

        assertEquals(2, response.size());
        assertEquals(taxResponse1, response.get(0));
        assertEquals(taxResponse2, response.get(1));

        verify(taxRepository).findAll();
    }

    @Test
    void findAllTaxes_NoTaxExist() {
        when(taxRepository.findAll()).thenReturn(Collections.emptyList());

        List<TaxResponse> result = taxService.findAll();

        assertTrue(result.isEmpty());

        verify(taxRepository).findAll();
        verify(taxMapper, never()).toResponse(any(Tax.class));
    }

    @Test
    void findById_ExistingId() {
        Long id = 1L;
        Tax tax1 = new Tax();
        tax1.setId(1L);
        tax1.setName("test_tax1");
        tax1.setDescription("description_1");
        tax1.setRate(0.1);
        TaxResponse taxResponse1 = new TaxResponse(1L, "test_tax1", "description_1", 0.1);

        when(taxRepository.findById(id)).thenReturn(Optional.of(tax1));
        when(taxMapper.toResponse(tax1)).thenReturn(taxResponse1);

        TaxResponse response = taxService.findById(id);

        assertEquals(taxResponse1, response);
        verify(taxRepository).findById(id);
        verify(taxMapper).toResponse(tax1);
    }

    @Test
    void findById_NonExistentId() {
        Long id = 4L;
        when(taxRepository.findById(id)).thenThrow(new TaxNotFoundException("Imposto não encontrado"));

        TaxNotFoundException exception = assertThrows(TaxNotFoundException.class, () -> taxService.findById(id));

        verify(taxRepository).findById(id);
        verify(taxMapper, never()).toResponse(any(Tax.class));
    }

    @Test
    void addTax_success() {

        String name = "testTax";
        String description = "description";
        double rate = 0.1;

        TaxRequest taxRequest = new TaxRequest(name, description, rate);

        Tax tax = new Tax();
        tax.setName(name);
        tax.setDescription(description);
        tax.setRate(rate);

        Tax savedTax = new Tax();
        savedTax.setId(1L);
        savedTax.setName(name);
        savedTax.setDescription(description);
        savedTax.setRate(rate);

        TaxResponse taxResponse = new TaxResponse(1L, name, description, rate);

        when(taxMapper.toEntity(taxRequest)).thenReturn(tax);
        when(taxRepository.save(any(Tax.class))).thenReturn(savedTax);
        when(taxMapper.toResponse(savedTax)).thenReturn(taxResponse);

        TaxResponse response = taxService.addTax(taxRequest);

        assertEquals(1L, response.id());
        assertEquals(name, response.name());
        assertEquals(description, response.description());
        assertEquals(rate, response.rate());

        verify(taxMapper).toEntity(taxRequest);
        verify(taxRepository).save(tax);
        verify(taxMapper).toResponse(savedTax);
    }

    @Test
    void calculateTax_success() {
        Long id = 1L;
        CalculateTaxRequest calculateTaxRequest = new CalculateTaxRequest(id, 100.0);
        Tax tax = new Tax();
        tax.setId(1L);
        tax.setName("Test_Tax");
        tax.setDescription("description_tax");
        tax.setRate(0.1);

        when(taxRepository.findById(id)).thenReturn(Optional.of(tax));

        CalculateTaxResponse calculateTaxResponse = taxService.calculateTax(calculateTaxRequest);

        assertEquals(100 * 0.1, calculateTaxResponse.taxCalculated());
        assertEquals("Test_Tax", calculateTaxResponse.taxName());

    }

    @Test
    void calculateTax_invalidTax() {
        Long id = 1L;
        CalculateTaxRequest calculateTaxRequest = new CalculateTaxRequest(id, 100.0);

        when(taxRepository.findById(id)).thenThrow(new TaxNotFoundException("Imposto não encontrado"));

        TaxNotFoundException exception = assertThrows(TaxNotFoundException.class, () -> taxService.calculateTax(calculateTaxRequest));

        verify(taxRepository).findById(id);
    }

    @Test
    void deleteTaxById_success() {
        Long id = 1L;
        when(taxRepository.existsById(id)).thenReturn(true);

        taxService.deleteTaxById(id);

        verify(taxRepository).deleteById(id);
    }

    @Test
    void deleteTaxById_notFound() {
        Long id = 1L;
        when(taxRepository.existsById(id)).thenReturn(false);

        assertThrows(TaxNotFoundException.class, () -> taxService.deleteTaxById(id));
        verify(taxRepository, never()).deleteById(id);
    }

}
