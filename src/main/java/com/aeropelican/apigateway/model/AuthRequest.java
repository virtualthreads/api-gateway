package com.aeropelican.apigateway.model;

public record AuthRequest(
        String email,
        String password
) {
}
