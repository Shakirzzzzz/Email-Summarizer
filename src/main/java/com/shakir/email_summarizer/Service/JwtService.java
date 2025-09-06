package com.shakir.email_summarizer.Service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {
    String extractUsername(String jwtToken);
    boolean isTokenValid(String jwtToken, UserDetails userDetails);
    String generateToken(Map<String,Object> claims, UserDetails userDetails);
}
