package com.mandob.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;

@Data
@JsonPropertyOrder({"code", "title", "message", "body"})
public class ApiResponse<T> {

    private int code;

    private String title;

    private String message;

    private T body;

    private List<T> bodyList;


    public ApiResponse(int code, String title, String message, List<T> bodyList) {
        this.code = code;
        this.title = title;
        this.message = message;
        this.bodyList = bodyList;
    }

    public ApiResponse(int code, String title, String message, T body) {
        this.code = code;
        this.title = title;
        this.message = message;
        this.body = body;
    }

    public static <T> ApiResponse<T> ok(T body) {
        return ok("", "", body);
    }

    public static <T> ApiResponse<T> ok(String title, String message, T body) {
        return new ApiResponse<>(200, title, message, body);
    }

    public static <T> ApiResponse<T> ok(String title, String message, List<T> body) {
        return new ApiResponse<>(200, title, message, body);
    }


    public static <T> ApiResponse<T> created(String title, String message, T body) {
        return new ApiResponse<>(201, title, message, body);
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
}
