package com.mandob.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CompanyReq {
    @NotBlank(message = "must-not-be-blank")
    private String arName;

    @NotBlank(message = "must-not-be-blank")
    private String enName;

    @NotBlank(message = "must-not-be-blank")
    private Boolean suspended;

    @NotBlank(message = "must-not-be-blank")
    private String currentUser;
}
