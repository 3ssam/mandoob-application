package com.mandob.response;

import lombok.Data;

@Data
public class HomeCounters {
    private int ordersNumber;
    private int activeAgentNumber;
    private int achievedVisitsNumber;
    private double totalPaidMoney;
}
