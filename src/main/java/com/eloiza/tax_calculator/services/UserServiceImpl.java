package com.eloiza.tax_calculator.services;

import com.eloiza.tax_calculator.controllers.dtos.LoginResponse;
import com.eloiza.tax_calculator.controllers.dtos.LoginRequest;
import com.eloiza.tax_calculator.controllers.dtos.UserRequest;
import com.eloiza.tax_calculator.controllers.dtos.UserResponse;
import com.eloiza.tax_calculator.exeptions.DuplicateUsernameException;
import com.eloiza.tax_calculator.infra.jwt.JwtTokenProvider;
import com.eloiza.tax_calculator.models.CustomUserDetails;
import com.eloiza.tax_calculator.models.Role;
import com.eloiza.tax_calculator.models.User;
import com.eloiza.tax_calculator.repositories.RoleRepository;
import com.eloiza.tax_calculator.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final CustomUserDetailsService customUserDetailsService;

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, CustomUserDetailsService customUserDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        String username = userRequest.username();
        if (userRepository.existsByUsername(username)) {
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

        return new UserResponse(user.getId(), user.getUsername(), stringRoles);
    }

    @Override
    public LoginResponse login(LoginRequest login) {
        User user = userRepository.findByUsername(login.username()).orElseThrow(() ->
                new UsernameNotFoundException("Usuário não encontrado!"));

        if (!bCryptPasswordEncoder.matches(login.password(), user.getPassword())) {
            throw new UsernameNotFoundException("Senha inválida!");
        }

        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(login.username());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateAccessToken(authentication);

        return new LoginResponse(token);

    }

}
