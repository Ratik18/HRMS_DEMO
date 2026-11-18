package com.hrms.employee.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Security rules for the employee-service:
 *  - every API needs a valid JWT (issued by the auth-service)
 *  - only the health/info actuator endpoints are public
 *  - no sessions, no login form: the JWT is sent on every request
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // same secret as the auth-service (see application.yml)
    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)          // token in a header, no cookies
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                    .anyRequest().authenticated())
            .exceptionHandling(ex -> ex
                    // no / bad / expired token -> 401
                    .authenticationEntryPoint((request, response, e) -> writeError(response,
                            HttpStatus.UNAUTHORIZED,
                            "Authentication required: missing, invalid or expired token"))
                    // logged in but not allowed -> 403
                    .accessDeniedHandler((request, response, e) -> writeError(response,
                            HttpStatus.FORBIDDEN,
                            "You do not have permission to access this resource")))
            // our JWT filter runs before Spring's own login filter
            .addFilterBefore(new JwtAuthenticationFilter(jwtSecret),
                    UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Same JSON shape as GlobalExceptionHandler: timestamp, status, error, message
    private void writeError(HttpServletResponse response, HttpStatus status, String message)
            throws IOException {

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"timestamp\":\"" + LocalDateTime.now()
                + "\",\"status\":" + status.value()
                + ",\"error\":\"" + status.getReasonPhrase()
                + "\",\"message\":\"" + message + "\"}");
    }
}
