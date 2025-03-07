package com.eloiza.tax_calculator.controllers;

import com.eloiza.tax_calculator.controllers.dtos.LoginRequest;
import com.eloiza.tax_calculator.controllers.dtos.LoginResponse;
import com.eloiza.tax_calculator.controllers.dtos.UserRequest;
import com.eloiza.tax_calculator.controllers.dtos.UserResponse;
import com.eloiza.tax_calculator.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tax/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest){
        LoginResponse loginResponse= userService.login(loginRequest);
        return ResponseEntity.ok().body(loginResponse.token());
    }

}
