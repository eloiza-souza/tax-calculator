package com.eloiza.tax_calculator.services;

import com.eloiza.tax_calculator.controllers.dtos.TaxResponse;
import com.eloiza.tax_calculator.exeptions.TaxNotFoundException;
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
}
