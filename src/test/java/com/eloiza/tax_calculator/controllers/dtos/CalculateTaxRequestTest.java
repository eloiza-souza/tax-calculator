package com.eloiza.tax_calculator.controllers.dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CalculateTaxRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validCalculateTaxRequest() {
        CalculateTaxRequest request = new CalculateTaxRequest(1L, 100.0);

        Set<ConstraintViolation<CalculateTaxRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty(), "Não deve haver violações para um objeto válido.");
    }

    @Test
    void invalidTaxId_Null() {
        CalculateTaxRequest request = new CalculateTaxRequest(null, 100.0);

        Set<ConstraintViolation<CalculateTaxRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size(), "Deve haver uma violação para taxId nulo.");
        assertEquals("O id do imposto é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    void invalidTaxId_Negative() {
        CalculateTaxRequest request = new CalculateTaxRequest(-1L, 100.0);

        Set<ConstraintViolation<CalculateTaxRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size(), "Deve haver uma violação para taxId negativo.");
        assertEquals("O id do imposto deve ser maior que zero", violations.iterator().next().getMessage());
    }

    @Test
    void invalidBaseValue_Null() {
        CalculateTaxRequest request = new CalculateTaxRequest(1L, null);

        Set<ConstraintViolation<CalculateTaxRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size(), "Deve haver uma violação para baseValue nulo.");
        assertEquals("O valor base para cálculo do imposto é obrigatória", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidBaseValue_Negative() {
        CalculateTaxRequest request = new CalculateTaxRequest(1L, -100.0);

        Set<ConstraintViolation<CalculateTaxRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size(), "Deve haver uma violação para baseValue negativo.");
        assertEquals("O valor base deve ser maior que zero", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidBothFields() {
        CalculateTaxRequest request = new CalculateTaxRequest(null, -100.0);

        Set<ConstraintViolation<CalculateTaxRequest>> violations = validator.validate(request);

        assertEquals(2, violations.size(), "Deve haver duas violações para ambos os campos inválidos.");
    }
}