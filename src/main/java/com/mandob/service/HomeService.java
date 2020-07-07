package com.mandob.service;

import com.mandob.response.HomeCounters;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeService {

    @Autowired
    private final OrderService orderService;
    @Autowired
    private final InvoiceService invoiceService;
    @Autowired
    private final MovementService movementService;
    @Autowired
    private final ScheduleVisitService scheduleVisitService;

    public HomeCounters getCounters() {
        HomeCounters counters = new HomeCounters();
        counters.setAchievedVisitsNumber(scheduleVisitService.countAchievedVisit());
        counters.setActiveAgentNumber(movementService.countActiveUsers());
        counters.setOrdersNumber(orderService.countOrdersOfDay());
        counters.setTotalPaidMoney(invoiceService.getPaidMoney());
        return counters;
    }
}
