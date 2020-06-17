package com.mandob.response;

import lombok.Data;

@Data
public class DistanceVisitReport implements VisitReport {
    private String checkInLongitude;
    private String checkInLatitude;
    private String checkInAddress;
    private String checkOutLongitude;
    private String checkOutLatitude;
    private String checkOutAddress;
    private String distance;
    private String customerArabicName;
    private String customerEnglishName;
    private String salesAgentArabicName;
    private String salesAgentEnglishName;
    private String employeeCode;
    private String visitLongitude;
    private String visitLatitude;
    private String visitAddress;
    private String visitState;
}
