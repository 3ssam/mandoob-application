package com.mandob.request;

import com.mandob.domain.enums.ScheduleVisitType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
public class ScheduleVisitReq {
    @NotBlank(message = "must-not-be-blank")
    private String salesforce;

    @NotBlank(message = "must-not-be-blank")
    private String customer;

    @NotNull(message = "must-not-be-null")
    private LocalDateTime scheduleDate;

    @NotNull(message = "must-not-be-null")
    @Pattern(regexp = "FOLLOW_UP|CASH_COLLECTION", message = "invalid-value")
    private String visitType;

    @NotNull(message = "must-not-be-null")
    private Boolean partialPayAllowed;

    @NotBlank(message = "must-not-be-blank")
    private String currentUser;

    public ScheduleVisitType getVisitType() {
        return ScheduleVisitType.valueOf(visitType);
    }
}
