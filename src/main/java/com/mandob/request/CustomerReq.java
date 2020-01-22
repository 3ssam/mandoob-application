package com.mandob.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CustomerReq {
    @NotBlank(message = "must-not-be-null")
    private String arName;

    @NotBlank(message = "must-not-be-null")
    private String password;

    @NotBlank(message = "must-not-be-null")
    private String enName;

    @NotBlank(message = "must-not-be-null")
    private String email;

    @NotBlank(message = "must-not-be-null")
    private String phoneNumber1;

    @NotBlank(message = "must-not-be-null")
    private String phoneNumber2;

//    @NotBlank(message = "must-not-be-null")
//    private String route;

    @NotBlank(message = "must-not-be-null")
    private String licenseNo;

    @NotBlank(message = "must-not-be-blank")
    private String currentUser;
}
