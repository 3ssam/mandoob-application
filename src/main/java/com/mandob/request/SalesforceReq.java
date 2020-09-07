package com.mandob.request;

import com.mandob.domain.enums.SalesforceRole;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class SalesforceReq {
    @NotBlank(message = "must-not-be-blank")
    private String email;

    @NotBlank(message = "must-not-be-blank")
    private String password;

    @NotBlank(message = "must-not-be-blank")
    private String arName;

    @NotBlank(message = "must-not-be-blank")
    private String enName;

    private String employeeCode;

    @NotBlank(message = "must-not-be-blank")
    @Pattern(regexp = "SALES_MANAGER|SALES_SUPERVISOR|SALES_AGENT", message = "invalid-value")
    private String salesforceRole;

    private String assignedTo;

    @NotBlank(message = "must-not-be-blank")
    private String phoneNumber;

    @Size(min = 14, max = 14, message = "invalid-value it should be 14 digit")
//    @Max(value = 14, message = "invalid-value")
    private String nationalId;

    @NotBlank(message = "must-not-be-blank")
    private String city;

    private String detailedAddress;

    @NotBlank(message = "must-not-be-blank")
    private String currentUser;

    public SalesforceRole getSalesforceRole() {
        return SalesforceRole.valueOf(salesforceRole);
    }
}
