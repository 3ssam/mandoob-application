package com.mandob.base.error;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mandob.base.Utils.ApiBaseResponse;

@JsonPropertyOrder({"code", "title", "message"})
public class ApiAuthorizationError extends ApiBaseResponse {
    public ApiAuthorizationError(String title, String message) {
        super(403, title, message);
    }
}
