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

class TaxRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validTaxRequest() {
        TaxRequest request = new TaxRequest("Imposto A", "Descrição do Imposto A", 10.0);

        Set<ConstraintViolation<TaxRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty(), "Não deve haver violações para um objeto válido.");
    }

    @Test
    void invalidName_Null() {
        TaxRequest request = new TaxRequest(null, "Descrição do Imposto A", 10.0);

        Set<ConstraintViolation<TaxRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size(), "Deve haver uma violação para name nulo.");
        assertEquals("O nome do imposto é obrigatório.", violations.iterator().next().getMessage());
    }

    @Test
    void invalidName_Blank() {
        TaxRequest request = new TaxRequest("", "Descrição do Imposto A", 10.0);

        Set<ConstraintViolation<TaxRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size(), "Deve haver uma violação para name vazio.");
        assertEquals("O nome do imposto é obrigatório.", violations.iterator().next().getMessage());
    }

    @Test
    void invalidDescription_Null() {
        TaxRequest request = new TaxRequest("Imposto A", null, 10.0);

        Set<ConstraintViolation<TaxRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size(), "Deve haver uma violação para description nulo.");
        assertEquals("A descrição do imposto é obrigatória.", violations.iterator().next().getMessage());
    }

    @Test
    void invalidDescription_Blank() {
        TaxRequest request = new TaxRequest("Imposto A", "", 10.0);

        Set<ConstraintViolation<TaxRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size(), "Deve haver uma violação para description vazio.");
        assertEquals("A descrição do imposto é obrigatória.", violations.iterator().next().getMessage());
    }

    @Test
    void invalidRate_Null() {
        TaxRequest request = new TaxRequest("Imposto A", "Descrição do Imposto A", null);

        Set<ConstraintViolation<TaxRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size(), "Deve haver uma violação para rate nulo.");
        assertEquals("A alíquota do imposto é obrigatória", violations.iterator().next().getMessage());
    }

    @Test
    void invalidRate_Negative() {
        TaxRequest request = new TaxRequest("Imposto A", "Descrição do Imposto A", -5.0);

        Set<ConstraintViolation<TaxRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size(), "Deve haver uma violação para rate negativo.");
        assertEquals("A alíquota do imposto deve ser maior que zero", violations.iterator().next().getMessage());
    }

    @Test
    void invalidRate_Zero() {
        TaxRequest request = new TaxRequest("Imposto A", "Descrição do Imposto A", 0.0);

        Set<ConstraintViolation<TaxRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size(), "Deve haver uma violação para rate igual a zero.");
        assertEquals("A alíquota do imposto deve ser maior que zero", violations.iterator().next().getMessage());
    }

    @Test
    void invalidAllFields() {
        TaxRequest request = new TaxRequest("", "", -5.0);

        Set<ConstraintViolation<TaxRequest>> violations = validator.validate(request);

        assertEquals(3, violations.size(), "Deve haver três violações para todos os campos inválidos.");
    }
}