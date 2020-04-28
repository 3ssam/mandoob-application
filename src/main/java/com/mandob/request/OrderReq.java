package com.mandob.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OrderReq {

    @NotNull(message = "must-not-be-null")
    private List<String> products;

    @NotNull(message = "must-not-be-null")
    private List<Integer> amounts;

    @NotBlank(message = "must-not-be-blank")
    private String currentUser;

}
