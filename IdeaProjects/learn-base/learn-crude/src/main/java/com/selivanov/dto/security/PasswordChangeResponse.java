package com.selivanov.dto.security;

public record PasswordChangeResponse(
        String operation,
        boolean success
) {}
