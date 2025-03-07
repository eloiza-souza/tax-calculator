package com.eloiza.tax_calculator.services;

import com.eloiza.tax_calculator.controllers.dtos.LoginResponse;
import com.eloiza.tax_calculator.controllers.dtos.LoginRequest;
import com.eloiza.tax_calculator.controllers.dtos.UserRequest;
import com.eloiza.tax_calculator.controllers.dtos.UserResponse;

public interface UserService {

    UserResponse createUser(UserRequest userRequest);

    LoginResponse login (LoginRequest login);
}
