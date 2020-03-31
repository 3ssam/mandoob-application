package com.mandob.base.Utils;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder({"code", "title", "message", "body"})
public class ApiResponse<T> extends ApiBaseResponse {
    private T body;

    public ApiResponse(int code, String title, String message, T body) {
        super(code, title, message);
        this.body = body;
    }

    public ApiResponse(int code, String title, String message) {
        super(code, title, message);
    }


    public static <T> ApiResponse<T> ok(T body) {
        return ok("", "", body);
    }

    public static <T> ApiResponse<T> ok(String title, String message, T body) {
        return new ApiResponse<>(200, title, message, body);
    }

    public static <T> ApiResponse<T> created(String title, String message, T body) {
        return new ApiResponse<>(201, title, message, body);
    }

    public static <T> ApiResponse<T> deleted(String title, String message) {
        return new ApiResponse<>(200, title, message);
    }


    public static <T> ApiResponse<T> created(T body) {
        return created("success", "created-successfully", body);
    }

    public static <T> ApiResponse<T> updated(T body) {
        return created("success", "updated-successfully", body);
    }

    public static <T> ApiResponse<T> saved(T body) {
        return created("success", "saved-successfully", body);
    }

    public static <T> ApiResponse<T> deleted(String delete) {
        return deleted("success", "deleted-successfully");
    }

}
