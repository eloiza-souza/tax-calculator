package com.eloiza.tax_calculator.services;

import com.eloiza.tax_calculator.controllers.dtos.LoginRequest;
import com.eloiza.tax_calculator.controllers.dtos.LoginResponse;


public interface AuthenticationService {

    LoginResponse authenticate(LoginRequest loginRequest);

}
