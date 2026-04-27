package com.olite.app.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // Endpoints públicos
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll()

                                // Endpoints solo ADMIN
                                .requestMatchers(HttpMethod.POST, "/api/productos/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/usuarios").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/pedidos/todos").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/pedidos/estado/**").hasAuthority("ROLE_ADMIN")

                               // Endpoints usuarios autenticados
                                .requestMatchers("/api/carrito/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                                .requestMatchers("/api/pedidos/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/usuarios/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/usuarios/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                              // Cualquier otra petición requiere autenticación
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
