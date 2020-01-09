//package com.mandob.base.exception;
//
//import com.mandob.base.Utils.ApiBaseResponse;
//import com.mandob.base.error.ApiFieldError;
//import com.mandob.base.error.ApiValidationError;
//import lombok.var;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import javax.validation.ConstraintViolationException;
//import java.util.ArrayList;
//import java.util.NoSuchElementException;
//
//@RestControllerAdvice
//public class ApiExceptionHandler {
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ApiValidationError validationExceptionHandler(ApiValidationException ex) {
//        return new ApiValidationError(ex.getTitle(), ex.getMessage(), ex.getErrors());
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ApiValidationError MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
//        var  errors = new ArrayList<ApiFieldError>();
//        var fieldErrors = ex.getBindingResult().getFieldErrors();
//        fieldErrors.forEach(err -> errors.add(ApiFieldError.of(err.getField(), err.getDefaultMessage())));
//        return new ApiValidationError("Error", "common.request.invalid-input-data", errors);
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ApiValidationError constraintViolationExceptionHandler(ConstraintViolationException ex) {
//        var errors = new ArrayList<ApiFieldError>();
//        var violations = ex.getConstraintViolations();
//        violations.forEach(err -> errors.add(ApiFieldError.of(err.getPropertyPath().toString(), err.getMessage())));
//        return new ApiValidationError("Error", "common.request.invalid-input-data", errors);
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ApiBaseResponse noSuchElementExceptionHandler(NoSuchElementException ex) {
//        return new ApiBaseResponse(400, "Invalid Entity Id", ex.getMessage());
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public ApiBaseResponse accessDeniedExceptionHandler(AccessDeniedException ex) {
//        return new ApiBaseResponse(403, "common.security.forbidden", ex.getMessage());
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public ApiBaseResponse badCredentialsExceptionHandler(BadCredentialsException ex) {
//        return new ApiBaseResponse(401, "common.security.unauthorized", "Invalid credentials!");
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ApiBaseResponse genericExceptionHandler(Exception ex) {
//        return new ApiBaseResponse(500, "Error, internal server error", ex.getMessage());
//    }
//}
