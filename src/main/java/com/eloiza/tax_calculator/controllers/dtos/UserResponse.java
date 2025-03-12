package com.eloiza.tax_calculator.controllers.dtos;

import java.util.Set;

public record UserResponse (Long id, String username, Set<String> role){
}
