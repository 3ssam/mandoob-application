package com.mandob.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class MovementReq {
    private String longitude;

    private String latitude;

    @NotNull(message = "must-not-be-null")
    @Pattern(regexp = "START|END|CHECKIN|CHECKOUT|SCHEDULE", message = "invalid-value")
    private String status;
}
