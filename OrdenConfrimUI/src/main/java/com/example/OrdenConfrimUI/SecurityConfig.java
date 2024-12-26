package com.example.OrdenConfrimUI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .anyRequest().permitAll()  // Permitir todas las solicitudes sin autenticación
            .and()
            .csrf().disable()           // Desactivar CSRF si no lo necesitas
            .httpBasic().disable();     // Desactivar la autenticación básica
        return http.build();
    }
}
