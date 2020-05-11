package com.mandob.request;

import com.mandob.domain.enums.PayingType;
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

    @NotNull(message = "must-not-be-null")
    private boolean Installment;

    @NotNull(message = "must-not-be-null")
    private String amountPaid;

    private String installmentNumber;

    @NotNull(message = "must-not-be-null")
    private PayingType payingType;

    @NotBlank(message = "must-not-be-blank")
    private String currentUser;

}
