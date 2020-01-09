package com.mandob.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserReq {

    @NotBlank(message = "must-not-be-blank")
    private String arName;

    @NotBlank(message = "must-not-be-blank")
    private String enName;

    @NotBlank(message = "must-not-be-blank")
    private String email;

    @NotBlank(message = "must-not-be-blank")
    private String password;

    private Boolean activated;
    private Boolean suspended;

    @NotBlank(message = "must-not-be-blank")
    private String role;

    @NotBlank(message = "must-not-be-blank")
    private String city;

    @NotBlank(message = "must-not-be-blank")
    private String company;

    @NotBlank(message = "must-not-be-blank")
    private String currentUser;
}
