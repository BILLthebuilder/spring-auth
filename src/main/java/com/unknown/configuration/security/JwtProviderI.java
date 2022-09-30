package com.unknown.configuration.security;

import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

public interface JwtProviderI {
    String generateToken(UserDetails user);

    String generateRefreshToken(UserDetails user);

    String getUsernameFromToken(String token);

    LocalDateTime getExpiryDateFromToken(String token);

    boolean validateToken(String token);
}
