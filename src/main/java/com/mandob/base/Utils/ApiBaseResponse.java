package com.mandob.base.Utils;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({"code", "title", "message"})
public class ApiBaseResponse {
    private int code;

    private String title;

    private String message;

    public ApiBaseResponse(int code, String title, String message) {
        this.code = code;
        this.title = title;
        this.message = message;
    }
}
