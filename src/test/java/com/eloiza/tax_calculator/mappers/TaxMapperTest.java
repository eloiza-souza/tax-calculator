package com.eloiza.tax_calculator.mappers;

import com.eloiza.tax_calculator.controllers.dtos.TaxRequest;
import com.eloiza.tax_calculator.controllers.dtos.TaxResponse;
import com.eloiza.tax_calculator.models.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaxMapperTest {

    private TaxMapper taxMapper;

    @BeforeEach
    void setUp() {
        // Inicializa o TaxMapper antes de cada teste
        taxMapper = new TaxMapper();
    }

    @Test
    void toResponse_shouldMapTaxToTaxResponse() {
        // Cenário positivo: Verifica se o Tax é mapeado corretamente para TaxResponse
        Tax tax = new Tax();
        tax.setId(1L);
        tax.setName("Tax Name");
        tax.setDescription("Tax Description");
        tax.setRate(15.5);

        TaxResponse taxResponse = taxMapper.toResponse(tax);

        assertNotNull(taxResponse, "TaxResponse não deve ser nulo");
        assertEquals(tax.getId(), taxResponse.id(), "O ID deve ser igual");
        assertEquals(tax.getName(), taxResponse.name(), "O nome deve ser igual");
        assertEquals(tax.getDescription(), taxResponse.description(), "A descrição deve ser igual");
        assertEquals(tax.getRate(), taxResponse.rate(), "A taxa deve ser igual");
    }

    @Test
    void toResponse_shouldThrowExceptionWhenTaxIsNull() {
        // Cenário de exceção: Verifica se uma exceção é lançada ao passar um Tax nulo
        assertThrows(NullPointerException.class, () -> taxMapper.toResponse(null), "Deve lançar NullPointerException");
    }

    @Test
    void toEntity_shouldMapTaxRequestToTax() {
        // Cenário positivo: Verifica se o TaxRequest é mapeado corretamente para Tax
        TaxRequest taxRequest = new TaxRequest("Tax Name", "Tax Description", 15.5);

        Tax tax = taxMapper.toEntity(taxRequest);

        assertNotNull(tax, "Tax não deve ser nulo");
        assertEquals(taxRequest.name(), tax.getName(), "O nome deve ser igual");
        assertEquals(taxRequest.description(), tax.getDescription(), "A descrição deve ser igual");
        assertEquals(taxRequest.rate(), tax.getRate(), "A taxa deve ser igual");
    }

    @Test
    void toEntity_shouldThrowExceptionWhenTaxRequestIsNull() {
        // Cenário de exceção: Verifica se uma exceção é lançada ao passar um TaxRequest nulo
        assertThrows(NullPointerException.class, () -> taxMapper.toEntity(null), "Deve lançar NullPointerException");
    }
}