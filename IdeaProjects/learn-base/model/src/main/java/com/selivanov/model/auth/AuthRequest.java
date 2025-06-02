package com.selivanov.model.auth;

public record AuthRequest(
        String username,
        String password) {
}
