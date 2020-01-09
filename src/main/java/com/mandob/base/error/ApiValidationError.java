package com.mandob.base.error;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mandob.base.Utils.ApiBaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder({"code", "title", "message", "errors"})
public class ApiValidationError extends ApiBaseResponse {
    private Map<String, List<String>> errors = new LinkedHashMap<>();

    public ApiValidationError(String title, String message, List<ApiFieldError> errors) {
        super(400, title, message);
        if (errors != null) {
            errors.forEach(error -> this.errors.computeIfAbsent(error.getField(), v -> new ArrayList<>()).add(error.getMessage()));
        }
    }
}
