package com.mandob.response;

import lombok.Data;

import java.util.List;

@Data
public class ScheduleVisitReport {
    private String SalesforceId;
    private String SalesforceCode;
    private String SalesforceName;
    private int visitCount;
    private List<VisitReport> reportRows;
}
