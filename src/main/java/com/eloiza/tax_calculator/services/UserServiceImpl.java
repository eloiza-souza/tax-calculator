package com.eloiza.tax_calculator.services;

import com.eloiza.tax_calculator.controllers.dtos.LoginRequest;
import com.eloiza.tax_calculator.controllers.dtos.LoginResponse;
import com.eloiza.tax_calculator.controllers.dtos.UserRequest;
import com.eloiza.tax_calculator.controllers.dtos.UserResponse;
import com.eloiza.tax_calculator.exceptions.DuplicateUsernameException;
import com.eloiza.tax_calculator.mappers.UserMapper;
import com.eloiza.tax_calculator.models.Role;
import com.eloiza.tax_calculator.models.User;
import com.eloiza.tax_calculator.repositories.RoleRepository;
import com.eloiza.tax_calculator.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder bCryptPasswordEncoder;

    private final UserMapper userMapper;

    private final AuthenticationService authenticationService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder bCryptPasswordEncoder, UserMapper userMapper, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userMapper = userMapper;
        this.authenticationService = authenticationService;
    }

    @Transactional
    @Override
    public UserResponse createUser(UserRequest userRequest) {
        validateDuplicateUsername(userRequest.username());
        Set<Role> roles = getRolesFromRequest(userRequest.role());
        String encodedPassword = bCryptPasswordEncoder.encode(userRequest.password());
        User user = userMapper.toEntity(userRequest, encodedPassword, roles);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest login) {
        return authenticationService.authenticate(login);
    }

    private void validateDuplicateUsername(String username){
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateUsernameException("Usuário já cadastrado no sistema");
        }
    }

    private Set<Role> getRolesFromRequest(Set<String> roleNames) {
        return roleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseGet(() -> roleRepository.save(new Role(roleName))))
                .collect(Collectors.toSet());
    }

}
