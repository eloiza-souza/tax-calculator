package com.eloiza.tax_calculator.services;

import com.eloiza.tax_calculator.controllers.dtos.AuthResponse;
import com.eloiza.tax_calculator.controllers.dtos.Login;
import com.eloiza.tax_calculator.controllers.dtos.UserRequest;
import com.eloiza.tax_calculator.controllers.dtos.UserResponse;
import com.eloiza.tax_calculator.exeptions.DuplicateUsernameException;
import com.eloiza.tax_calculator.infra.jwt.JwtTokenProvider;
import com.eloiza.tax_calculator.models.Role;
import com.eloiza.tax_calculator.models.User;
import com.eloiza.tax_calculator.repositories.RoleRepository;
import com.eloiza.tax_calculator.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        String username = userRequest.username();
        if (userRepository.existsByUsername(username)){
            throw new DuplicateUsernameException("Usuário já cadastrado no sistema");
        }

        User user = new User();
        user.setUsername(username);
      user.setPassword(bCryptPasswordEncoder.encode(userRequest.password()));
        Set<Role> roles = userRequest.role().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseGet(() -> roleRepository.save(new Role(roleName))))
                .collect(Collectors.toSet());
        user.setRoles(roles);

        userRepository.save(user);

        Set<String> stringRoles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new UserResponse(user.getId(),user.getUsername(),stringRoles);
    }

    @Override
    public AuthResponse login(Login login) {
        return null;
    }


}
