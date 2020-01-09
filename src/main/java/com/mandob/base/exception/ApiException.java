package com.mandob.base.exception;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException {
    @NonNull
    private String title;

    @NonNull
    private String message;

    private Instant timestamp = Instant.now();

    private String errorId = UUID.randomUUID().toString();
}
