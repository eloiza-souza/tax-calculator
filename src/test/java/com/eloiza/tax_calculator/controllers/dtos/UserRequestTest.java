package com.eloiza.tax_calculator.controllers.dtos;

import com.eloiza.tax_calculator.validation.ValidEnum;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validUserRequest() {
        UserRequest request = new UserRequest("user123", "password123", Set.of("ROLE_ADMIN", "ROLE_USER"));

        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty(), "Não deve haver violações para um objeto válido.");
    }

    @Test
    void invalidUsername_Null() {
        UserRequest request = new UserRequest(null, "password123", Set.of("ROLE_ADMIN"));

        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size(), "Deve haver uma violação para username nulo.");
        assertEquals("O nome de usuário é obrigatório.", violations.iterator().next().getMessage());
    }

    @Test
    void invalidUsername_Blank() {
        UserRequest request = new UserRequest("", "password123", Set.of("ROLE_ADMIN"));

        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size(), "Deve haver uma violação para username vazio.");
        assertEquals("O nome de usuário é obrigatório.", violations.iterator().next().getMessage());
    }

    @Test
    void invalidPassword_Null() {
        UserRequest request = new UserRequest("user123", null, Set.of("ROLE_ADMIN"));

        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size(), "Deve haver uma violação para password nulo.");
        assertEquals("A senha é obrigatória.", violations.iterator().next().getMessage());
    }

    @Test
    void invalidPassword_Blank() {
        UserRequest request = new UserRequest("user123", "", Set.of("ROLE_ADMIN"));

        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size(), "Deve haver uma violação para password vazio.");
        assertEquals("A senha é obrigatória.", violations.iterator().next().getMessage());
    }

    @Test
    void invalidRole_Empty() {
        UserRequest request = new UserRequest("user123", "password123", Set.of());

        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size(), "Deve haver uma violação para role vazio.");
        assertEquals("O campo de roles não pode ser vazio.", violations.iterator().next().getMessage());
    }

    @Test
    void invalidRole_InvalidValue() {
        UserRequest request = new UserRequest("user123", "password123", Set.of("INVALID_ROLE"));

        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size(), "Deve haver uma violação para role com valor inválido.");
        assertEquals("Valor inválido para o campo role. Valores permitidos: [ROLE_ADMIN, ROLE_USER]", violations.iterator().next().getMessage());
    }

    @Test
    void invalidAllFields() {
        UserRequest request = new UserRequest("", "", Set.of("INVALID_ROLE"));

        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);

        assertEquals(3, violations.size(), "Deve haver três violações para todos os campos inválidos.");
    }
}