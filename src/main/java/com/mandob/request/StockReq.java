package com.mandob.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class StockReq {

    @NotNull
    private String currentUser;

    @NotNull
    private String product;

    private long soldItems = 0;

    private long stockItems = 0;
}
