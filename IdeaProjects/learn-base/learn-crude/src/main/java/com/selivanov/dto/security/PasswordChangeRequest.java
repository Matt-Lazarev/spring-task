package com.selivanov.dto.security;

public record PasswordChangeRequest (
        String oldPassword,
        String newPassword
) {}
