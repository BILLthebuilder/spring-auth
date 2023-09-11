package com.unknown.dto;


public record LoginResponse(
        String message,
        String token,
        Status status

) {
}

