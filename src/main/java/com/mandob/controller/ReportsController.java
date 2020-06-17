package com.mandob.controller;

import com.mandob.projection.Customer.CustomerListProjection;
import com.mandob.projection.SalesForce.SalesforceListProjection;
import com.mandob.response.MovementReport;
import com.mandob.response.OrderReport;
import com.mandob.response.ScheduleVisitReport;
import com.mandob.service.ReportsServices;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("reports")
public class ReportsController {
    private final ReportsServices reportsServices;

    @GetMapping("salesforce/{salesforceId}/customers")
    public List<CustomerListProjection> findAllSalesforceCustomers(@PathVariable String salesforceId) {
        return reportsServices.getCustomersOfSalesforce(salesforceId);
    }


    @GetMapping("{companyId}")
    public List<SalesforceListProjection> getSalesAgentByCompanyId(@PathVariable String companyId) {
        return reportsServices.getSalesAgentByCompanyId(companyId);
    }

    @GetMapping("visits/time")
    public List<ScheduleVisitReport> getTimeVisitReport(@RequestParam(required = false) String companyId, @RequestParam(required = false) String salesagentId,
                                                        @RequestParam(required = false) String customerId,
                                                        @RequestParam(required = false) String salesagentCode,
                                                        @RequestParam(required = false) String from,
                                                        @RequestParam(required = false) String to) {
        return reportsServices.getVisitReport(companyId, salesagentId, customerId, salesagentCode, from, to, "TIME");
    }

    @GetMapping("visits/distance")
    public List<ScheduleVisitReport> getDistanceVisitReport(@RequestParam(required = false) String companyId, @RequestParam(required = false) String salesagentId,
                                                            @RequestParam(required = false) String customerId,
                                                            @RequestParam(required = false) String salesagentCode,
                                                            @RequestParam(required = false) String from,
                                                            @RequestParam(required = false) String to) {
        return reportsServices.getVisitReport(companyId, salesagentId, customerId, salesagentCode, from, to, "DISTANCE");
    }

    @GetMapping("orders")
    public OrderReport getOrdersReport(@RequestParam(required = false) String orderId,
                                       @RequestParam(required = false) String salesagentId,
                                       @RequestParam(required = false) String customerId,
                                       @RequestParam(required = false) String from,
                                       @RequestParam(required = false) String to) {
        return reportsServices.getOrdersReport(orderId, salesagentId, customerId, from, to);
    }


    @GetMapping("movements")
    public MovementReport getMovementsReport(@RequestParam(required = false) String salesagentId,
                                             @RequestParam(required = false) String from,
                                             @RequestParam(required = false) String to) {
        return reportsServices.getMovementsReport(salesagentId, from, to);
    }

    @GetMapping("download")
    public ResponseEntity<Resource> downloadReport(@RequestParam(required = false) String companyId,
                                                   @RequestParam(required = false) String orderId,
                                                   @RequestParam(required = false) String salesagentId,
                                                   @RequestParam(required = false) String salesagentCode,
                                                   @RequestParam(required = false) String customerId,
                                                   @RequestParam(required = false) String from,
                                                   @RequestParam(required = false) String to,
                                                   @RequestParam String reportType) {
        return reportsServices.DownloadReport(companyId, orderId, salesagentId, salesagentCode, customerId, from, to, reportType);
    }


}
