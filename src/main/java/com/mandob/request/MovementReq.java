package com.mandob.request;

import com.mandob.domain.enums.MovementStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class MovementReq {
    @NotNull(message = "must-not-be-null")
    private String longitude;

    @NotNull(message = "must-not-be-null")
    private String latitude;

    @NotNull(message = "must-not-be-null")
    @Pattern(regexp = "START|END|CHECKIN|CHECKOUT|SCHEDULE", message = "invalid-value")
    private String status;

    @NotBlank(message = "must-not-be-blank")
    private String currentUser;

    private String customer;

    public MovementStatus getStatus() {
        return MovementStatus.valueOf(status);
    }

}
