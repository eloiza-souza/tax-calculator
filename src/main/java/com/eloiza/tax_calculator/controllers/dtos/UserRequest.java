package com.eloiza.tax_calculator.controllers.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record UserRequest(

        @NotBlank(message = "O nome de usuário é obrigatório.")
        String username,

        @NotBlank(message = "A senha é obrigatória.")
        String password,

        @NotEmpty(message = "O campo de roles não pode ser vazio.")
        Set<String> role
) {
}
