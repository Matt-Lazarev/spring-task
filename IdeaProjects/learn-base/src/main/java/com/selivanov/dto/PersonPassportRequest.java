package com.selivanov.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PersonPassportRequest(
        PersonDto person,
        PassportDto passport
) {
}
