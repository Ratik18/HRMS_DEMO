
package com.example.attendance.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7).trim();

            if (token.isEmpty()) {
                throw new JwtException("Empty JWT token");
            }

            // Decode Base64 Secret
            byte[] decodedSecret =
                    Base64.getDecoder().decode(jwtSecret.trim());

            SecretKey secretKey =
                    Keys.hmacShaKeyFor(decodedSecret);

            // Validate JWT
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String username = claims.getSubject();

            if (username == null || username.isBlank()) {
                throw new JwtException("JWT subject missing");
            }

            Object roleClaim = claims.get("roles");

            if (roleClaim == null) {
                roleClaim = claims.get("role");
            }

            List<SimpleGrantedAuthority> authorities =
                    new ArrayList<>();

            if (roleClaim instanceof String) {

                String role = (String) roleClaim;

                authorities.add(new SimpleGrantedAuthority(
                        role.startsWith("ROLE_")
                                ? role
                                : "ROLE_" + role
                ));

            } else if (roleClaim instanceof List<?>) {

                for (Object item : (List<?>) roleClaim) {

                    String role = item.toString();

                    authorities.add(new SimpleGrantedAuthority(
                            role.startsWith("ROLE_")
                                    ? role
                                    : "ROLE_" + role
                    ));
                }
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            authorities
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);

        } catch (io.jsonwebtoken.ExpiredJwtException e) {

            System.err.println("JWT Token Expired");

            SecurityContextHolder.clearContext();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            response.getWriter().write(
                "{\"error\":\"JWT token expired. Please login again.\"}"
            );

            return;

        } catch (JwtException | IllegalArgumentException e) {

            System.err.println(
                "JWT Validation Error: " +
                e.getClass().getSimpleName()
            );

            SecurityContextHolder.clearContext();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            response.getWriter().write(
                "{\"error\":\"Invalid JWT token\"}"
            );

            return;
        }
    }
}