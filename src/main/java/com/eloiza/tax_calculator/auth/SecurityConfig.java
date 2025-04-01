package com.eloiza.tax_calculator.auth;


import com.eloiza.tax_calculator.auth.jwt.JwtAuthenticationEntryPoint;
import com.eloiza.tax_calculator.auth.jwt.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    private JwtAuthenticationFilter authenticationFilter;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> {
                    authorize.requestMatchers(HttpMethod.POST,"api/tax/user/register").permitAll();
                    authorize.requestMatchers(HttpMethod.POST,"/api/tax/user/login").permitAll();

                    authorize.requestMatchers(HttpMethod.GET,"/api/tax/tipos").authenticated();
                    authorize.requestMatchers(HttpMethod.GET,"/api/tax/tipos/{id}").authenticated();

                    authorize.requestMatchers(HttpMethod.POST,"/api/tax/tipos").hasRole("ADMIN");
                    authorize.requestMatchers(HttpMethod.POST,"/api/tax/calculo").hasRole("ADMIN");
                    authorize.requestMatchers(HttpMethod.DELETE,"/api/tax/tipos").hasRole("ADMIN");

                    authorize.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll();
                    authorize.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();

                    authorize.anyRequest().authenticated();
                }).httpBasic(Customizer.withDefaults());

        http.exceptionHandling(exception -> exception
                .authenticationEntryPoint(authenticationEntryPoint));

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}