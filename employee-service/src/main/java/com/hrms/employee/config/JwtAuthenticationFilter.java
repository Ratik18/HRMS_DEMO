package com.hrms.employee.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Runs once for every request, before Spring Security checks access.
 *
 * What it does:
 *  1. Reads the "Authorization: Bearer <token>" header.
 *  2. Checks the token: signature (same secret as the auth-service) and expiry.
 *  3. If the token is valid, marks the request as authenticated (user = token subject).
 *  4. If there is no token, or it is invalid/expired, it does nothing, so the request
 *     stays unauthenticated and SecurityConfig answers 401 for protected URLs.
 *
 * The employee-service never logs anyone in; the auth-service issues the tokens.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtDecoder jwtDecoder;

    public JwtAuthenticationFilter(String secret) {
        SecretKey key = new SecretKeySpec(decodeSecret(secret), "HmacSHA256");
        this.jwtDecoder = NimbusJwtDecoder.withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith(BEARER_PREFIX)) {
            String token = header.substring(BEARER_PREFIX.length()).trim();

            try {
                Jwt jwt = jwtDecoder.decode(token);      // throws if signature is wrong or token expired
                String username = jwt.getSubject();      // the user's email, set by the auth-service

                if (username != null
                        && SecurityContextHolder.getContext().getAuthentication() == null) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, extractAuthorities(jwt));
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JwtException | IllegalArgumentException ex) {
                // Bad, tampered or expired token: stay unauthenticated -> 401 later
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    // Optional: if the token carries a "roles" (list) or "role" claim, turn it into ROLE_xxx authorities
    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles");
        if (roles == null) {
            roles = jwt.getClaimAsStringList("role");
        }
        if (roles == null) {
            return List.of();
        }
        return roles.stream()
                .filter(role -> role != null && !role.isBlank())
                .map(String::trim)
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }

    /**
     * The secret must be turned into key bytes exactly the way the auth-service does it.
     * The shared secret is a Base64 text, so we Base64-decode it. If the auth-service instead
     * uses the plain text bytes (secret.getBytes()), change the return below to
     * secret.getBytes(StandardCharsets.UTF_8).
     */
    private static byte[] decodeSecret(String secret) {
        try {
            byte[] decoded = Base64.getDecoder().decode(secret);
            if (decoded.length >= 32) {
                return decoded;
            }
        } catch (IllegalArgumentException ignored) {
            // not valid Base64, fall through to plain text
        }
        return secret.getBytes(StandardCharsets.UTF_8);
    }
}
