package com.eloiza.tax_calculator.controllers.dtos;

public record CalculateTaxResponse(
        String taxName,
        Double baseValue,
        Double rate,
        Double taxCalculated
) {
}
