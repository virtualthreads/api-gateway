package com.aeropelican.apigateway.client.dto;

public record LoginRequest(
        String email,
        String password
) {
}
