package com.aeropelican.apigateway.client.dto;

import lombok.Builder;

@Builder
public record AuthUser(
        String firstname,
        String lastname,
        String email,
        String hashedPassword
) {
}
