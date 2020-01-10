package com.mandob.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthReq {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
