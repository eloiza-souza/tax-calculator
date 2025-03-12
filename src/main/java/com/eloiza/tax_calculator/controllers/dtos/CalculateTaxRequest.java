package com.eloiza.tax_calculator.controllers.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CalculateTaxRequest(
        @NotNull(message = "O id do imposto é obrigatório")
        @Positive(message = "O id do imposto deve ser maior que zero")
        Long taxId,

        @NotNull(message = "O valor base para cálculo do imposto é obrigatória")
        @Positive(message = "O valor base deve ser maior que zero")
        Double baseValue
) {
}
