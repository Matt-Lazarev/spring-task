package com.selivanov.model.auth;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthResponse(
        String status,
        String username,
        String accessToken,
        String refreshToken
) {
}
