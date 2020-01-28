package com.mandob.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CategoryReq {

    @NotBlank(message = "must-not-be-blank")
    private String arName;

    @NotBlank(message = "must-not-be-blank")
    private String enName;

    private String parent;

    @NotBlank(message = "must-not-be-blank")
    private String currentUser;

}
