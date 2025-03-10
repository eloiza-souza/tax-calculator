package com.eloiza.tax_calculator.controllers.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TaxRequest(
        @NotBlank(message = "O nome do imposto é obrigatório.")
        String name,

        @NotBlank(message = "A descrição do imposto é obrigatória.")
        String description,

        @NotNull(message = "A alíquota do imposto é obrigatória")
        @Positive(message = "A alíquota do imposto deve ser maior que zero")
        Double rate
) {
}
