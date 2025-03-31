package com.eloiza.tax_calculator.mappers;

import com.eloiza.tax_calculator.controllers.dtos.UserRequest;
import com.eloiza.tax_calculator.controllers.dtos.UserResponse;
import com.eloiza.tax_calculator.models.Role;
import com.eloiza.tax_calculator.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        // Inicializa o UserMapper antes de cada teste
        userMapper = new UserMapper();
    }

    @Test
    void toEntity_shouldMapUserRequestToUser() {
        UserRequest userRequest = new UserRequest("testUser", "password123", Set.of("ROLE_ADMIN"));
        String encodedPassword = "encodedPassword123";
        Role role = new Role( "ROLE_ADMIN");
        Set<Role> roles = Set.of(role);

        User user = userMapper.toEntity(userRequest, encodedPassword, roles);

        assertNotNull(user, "User não deve ser nulo");
        assertEquals(userRequest.username(), user.getUsername(), "O username deve ser igual");
        assertEquals(encodedPassword, user.getPassword(), "A senha codificada deve ser igual");
        assertEquals(roles, user.getRoles(), "Os papéis (roles) devem ser iguais");
    }


    @Test
    void toResponse_shouldMapUserToUserResponse() {
        Role role1 = new Role( "ROLE_USER");
        Role role2 = new Role( "ROLE_ADMIN");
        Set<Role> roles = Set.of(role1, role2);
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setRoles(roles);

        UserResponse userResponse = userMapper.toResponse(user);

        assertNotNull(userResponse, "UserResponse não deve ser nulo");
        assertEquals(user.getId(), userResponse.id(), "O ID deve ser igual");
        assertEquals(user.getUsername(), userResponse.username(), "O username deve ser igual");
        assertEquals(Set.of("ROLE_USER", "ROLE_ADMIN"), userResponse.role(), "Os papéis (roles) devem ser iguais");
    }

    @Test
    void toResponse_shouldThrowExceptionWhenUserIsNull() {
        // Cenário de exceção: Verifica se uma exceção é lançada ao passar um User nulo
        assertThrows(NullPointerException.class, () -> userMapper.toResponse(null),
                "Deve lançar NullPointerException");
    }
}