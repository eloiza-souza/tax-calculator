package com.eloiza.tax_calculator.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    private String[] enumValues;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        enumValues = Arrays.stream(constraintAnnotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .toArray(String[]::new);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        boolean isValid = Arrays.asList(enumValues).contains(value);
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Valor inválido para o campo role. Valores permitidos: " + Arrays.toString(enumValues)
            ).addConstraintViolation();
        }
        return isValid;
    }
}