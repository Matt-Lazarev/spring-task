package com.selivanov.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ValidationErrorResponse(
        Integer status,
        String description,
        List<FieldError> errors
) {
}

/*

{
    "status" : 400,
    "description": "desc",
    "errors" : {
        "field1" : "value1",
        "field2" : "value2"
    }
}

 */