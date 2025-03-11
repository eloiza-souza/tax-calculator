package com.eloiza.tax_calculator.mappers;

import com.eloiza.tax_calculator.controllers.dtos.UserRequest;
import com.eloiza.tax_calculator.controllers.dtos.UserResponse;
import com.eloiza.tax_calculator.models.Role;
import com.eloiza.tax_calculator.models.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User toEntity(UserRequest userRequest, String encodedPassword, Set<Role> roles) {
        User user = new User();
        user.setUsername(userRequest.username());
        user.setPassword(encodedPassword);
        user.setRoles(roles);
        return user;
    }


    public UserResponse toResponse(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        return new UserResponse(user.getId(), user.getUsername(), roles);
    }
}