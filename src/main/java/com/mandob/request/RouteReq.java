package com.mandob.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RouteReq {
    @NotBlank(message = "must-not-be-blank")
    private String arName;

    @NotBlank(message = "must-not-be-blank")
    private String enName;

    @NotBlank(message = "must-not-be-blank")
    private String city;

    @NotBlank(message = "must-not-be-blank")
    private String salesforce;

    @NotBlank(message = "must-not-be-blank")
    private String currentUser;
}
