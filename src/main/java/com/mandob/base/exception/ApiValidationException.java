//package com.mandob.base.exception;
//
//import com.mandob.base.error.ApiFieldError;
//import lombok.Getter;
//
//import java.util.Collections;
//import java.util.List;
//
//
//@Getter
//public class ApiValidationException extends ApiException {
//    private List<ApiFieldError> errors;
//
//    public ApiValidationException(String field, String message) {
//        super("Error", "common.request.invalid-input-data");
//        this.errors = Collections.singletonList(ApiFieldError.of(field, message));
//    }
//
//    public ApiValidationException(String title, String message, ApiFieldError error) {
//        super(title, message);
//        this.errors = Collections.singletonList(error);
//    }
//
//    public ApiValidationException(String title, String message, List<ApiFieldError> errors) {
//        super(title, message);
//        this.errors = errors;
//    }
//}
