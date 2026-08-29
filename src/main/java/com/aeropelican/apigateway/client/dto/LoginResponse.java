package com.aeropelican.apigateway.client.dto;

import java.util.UUID;

public record LoginResponse(
        UUID userId,
        String email,
        String role,
        String token
) {
}
