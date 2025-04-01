package com.eloiza.tax_calculator.auth.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;

class JwtAuthenticationEntryPointTest {

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException authException;

    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        jwtAuthenticationEntryPoint = new JwtAuthenticationEntryPoint();

        responseWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(responseWriter);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void commence_shouldSetUnauthorizedResponse() throws IOException, ServletException {
        jwtAuthenticationEntryPoint.commence(request, response, authException);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        verify(response).setContentType("application/json");

        verify(response).setCharacterEncoding("UTF-8");

        String expectedResponse = "{\"error\": \"Acesso não autorizado. Verifique o token de autenticação.\"}";
        responseWriter.flush();
        assert responseWriter.toString().contains(expectedResponse);
    }

    @Test
    void commence_shouldHandleIOException() throws IOException, ServletException {
        doThrow(new IOException("Erro ao escrever na resposta")).when(response).getWriter();

        jwtAuthenticationEntryPoint.commence(request, response, authException);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        verify(response).setContentType("application/json");

        verify(response).setCharacterEncoding("UTF-8");

        verify(response).getWriter();
    }
}