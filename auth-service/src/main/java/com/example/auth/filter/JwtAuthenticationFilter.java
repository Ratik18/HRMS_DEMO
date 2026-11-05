package com.example.auth.filter;

import java.io.IOException;
import java.util.Collections;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;




@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter  {
	
	  private final SecretKey secretKey;

	    public JwtAuthenticationFilter(
	            @Value("${security.jwt.secret}") String secret) {
	    	  System.out.println("JWT secret length: " + secret.length());
	    	    System.out.println("JWT secret contains underscore: " + secret.contains("_"));
	        byte[] keyBytes = Decoders.BASE64.decode(secret);
	        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
	    }

	    @Override
	    protected void doFilterInternal(
	            HttpServletRequest request,
	            HttpServletResponse response,
	            FilterChain filterChain)
	            throws ServletException, IOException {

	        String authorizationHeader =
	                request.getHeader("Authorization");

	        // Continue if there is no Bearer token
	        if (authorizationHeader == null ||
	                !authorizationHeader.startsWith("Bearer ")) {

	            filterChain.doFilter(request, response);
	            return;
	        }

	        String token = authorizationHeader.substring(7);

	        try {

	            Claims claims = Jwts.parser()
	                    .verifyWith(secretKey)
	                    .build()
	                    .parseSignedClaims(token)
	                    .getPayload();

	            String email = claims.getSubject();

	            if (email == null || email.isBlank()) {
	                response.sendError(
	                        HttpServletResponse.SC_UNAUTHORIZED,
	                        "Invalid JWT token");
	                return;
	            }

	            UsernamePasswordAuthenticationToken authentication =
	                    new UsernamePasswordAuthenticationToken(
	                            email,
	                            null,
	                            Collections.emptyList());

	            SecurityContextHolder.getContext()
	                    .setAuthentication(authentication);

	        } catch (JwtException | IllegalArgumentException exception) {

	            SecurityContextHolder.clearContext();

	            response.sendError(
	                    HttpServletResponse.SC_UNAUTHORIZED,
	                    "Invalid or expired JWT token");

	            return;
	        }

	        filterChain.doFilter(request, response);
	    }

}
