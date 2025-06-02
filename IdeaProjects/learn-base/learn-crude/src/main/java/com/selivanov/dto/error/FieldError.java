package com.selivanov.dto.error;

public record FieldError (
        String field,
        String error
) {}
