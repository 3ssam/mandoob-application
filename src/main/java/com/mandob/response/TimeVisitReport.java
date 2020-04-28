package com.mandob.response;

import lombok.Data;

@Data
public class TimeVisitReport implements VisitReport {
    private String checkInDateTime;
    private String checkOutDateTime;
    private String delay;
    private String duration;
    private String customerArabicName;
    private String customerEnglishName;
    private String salesAgentArabicName;
    private String salesAgentEnglishName;
    private String employeeCode;
    private String scheduleDate;
    private String visitState;
}
