package com.eloiza.tax_calculator.services;

import com.eloiza.tax_calculator.controllers.dtos.AuthResponse;
import com.eloiza.tax_calculator.controllers.dtos.Login;
import com.eloiza.tax_calculator.controllers.dtos.UserRequest;
import com.eloiza.tax_calculator.controllers.dtos.UserResponse;

public interface UserService {

    UserResponse createUser(UserRequest userRequest);

    AuthResponse login (Login login);
}
