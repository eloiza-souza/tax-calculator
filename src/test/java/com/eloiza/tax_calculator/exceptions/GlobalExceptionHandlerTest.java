package com.eloiza.tax_calculator.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private HttpServletRequest mockRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleDuplicateUsernameException() {
        DuplicateUsernameException exception = new DuplicateUsernameException("Username already exists");
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleDuplicateUsernameException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Conflict", Objects.requireNonNull(response.getBody()).get("error"));
        assertEquals("Username already exists", response.getBody().get("message"));
    }

    @Test
    void testHandleDuplicateTaxNameException() {
        DuplicateTaxNameException exception = new DuplicateTaxNameException("Tax name already exists");
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleDuplicateTaxNameException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Conflict", Objects.requireNonNull(response.getBody()).get("error"));
        assertEquals("Tax name already exists", response.getBody().get("message"));
    }

    @Test
    void testHandleTaxNotFoundException() {
        TaxNotFoundException exception = new TaxNotFoundException("Tax not found");
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleTaxNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", Objects.requireNonNull(response.getBody()).get("error"));
        assertEquals("Tax not found", response.getBody().get("message"));
    }

    @Test
    void testHandleUsernameNotFoundException() {
        UsernameNotFoundException exception = new UsernameNotFoundException("User not found");
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleUsernameNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", response.getBody().get("error"));
        assertEquals("User not found", response.getBody().get("message"));
    }

    @Test
    void testHandleValidationExceptions() {
        FieldError fieldError = new FieldError("objectName", "fieldName", "Invalid value");

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "objectName");
        bindingResult.addError(fieldError);

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid value", response.getBody().get("fieldName"));
    }

    @Test
    void testHandleConstraintViolationException() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Invalid value");
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(violation);

        ConstraintViolationException exception = new ConstraintViolationException(violations);
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleConstraintViolationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation Error", Objects.requireNonNull(response.getBody()).get("error"));
        assertEquals("Invalid value", response.getBody().get("message"));
    }

    @Test
    void testHandleUnauthorized() {
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleUnauthorized(mockRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Unauthorized", Objects.requireNonNull(response.getBody()).get("error"));
        assertEquals("Usuário não autorizado", response.getBody().get("message"));
    }

    @Test
    void testHandleGenericException() {
        Exception exception = new Exception("Unexpected error");
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal Server Error", Objects.requireNonNull(response.getBody()).get("error"));
        assertEquals("Ocorreu um erro inesperado. Por favor, tente novamente mais tarde.", response.getBody().get("message"));
    }
}