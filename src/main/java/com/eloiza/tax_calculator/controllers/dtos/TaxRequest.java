package com.eloiza.tax_calculator.controllers.dtos;

import com.eloiza.tax_calculator.validation.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record TaxRequest(
        @NotBlank(message = "O nome do imposto é obrigatório.")
        String name,

        @NotBlank(message = "A descrição do imposto é obrigatória.")
        String description,

        @NotNull(message = "A alíquota do imposto é obrigatória")
        double rate
) {
}
