package com.eloiza.tax_calculator.controllers;

import com.eloiza.tax_calculator.controllers.dtos.LoginRequest;
import com.eloiza.tax_calculator.controllers.dtos.LoginResponse;
import com.eloiza.tax_calculator.controllers.dtos.UserRequest;
import com.eloiza.tax_calculator.controllers.dtos.UserResponse;
import com.eloiza.tax_calculator.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Usuários", description = "Gerenciamento de usuários e autenticação")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Registrar um novo usuário", description = "Cadastra um novo usuário no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @Operation(summary = "Login de usuário", description = "Autentica um usuário e retorna um token de acesso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        LoginResponse loginResponse= userService.login(loginRequest);
        return ResponseEntity.ok().body(loginResponse);
    }

}
