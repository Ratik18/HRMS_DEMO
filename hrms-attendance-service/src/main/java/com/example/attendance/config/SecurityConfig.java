
package com.example.attendance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth

                // Public endpoints
                .requestMatchers(
                    "/login",
                    "/register"
                ).permitAll()

                // Employee APIs
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/attendance/check-in",
                    "/api/attendance/check-out"
                ).hasAnyRole("EMPLOYEE", "HR", "ADMIN")

                .requestMatchers(
                    HttpMethod.GET,
                    "/api/attendance/my"
                ).hasAnyRole("EMPLOYEE", "HR", "ADMIN")

                // HR/Admin APIs
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/attendance"
                ).hasAnyRole("HR", "ADMIN")

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/attendance/*/correction"
                ).hasAnyRole("HR", "ADMIN")

                // Other authenticated APIs
                .anyRequest().authenticated()
            )

            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, exception) -> {
                    response.setStatus(401);
                    response.setContentType("application/json");
                    response.getWriter().write(
                        "{\"error\":\"Unauthorized - Invalid or missing JWT\"}"
                    );
                })
                .accessDeniedHandler((request, response, exception) -> {
                    response.setStatus(403);
                    response.setContentType("application/json");
                    response.getWriter().write(
                        "{\"error\":\"Forbidden - Insufficient role permissions\"}"
                    );
                })
            )

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
