package com.eloiza.tax_calculator.controllers.dtos;

import java.util.Set;

public record UserRequest(String username, String password, Set<String> role) {
}
