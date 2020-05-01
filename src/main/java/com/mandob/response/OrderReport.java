package com.mandob.response;

import lombok.Data;

import java.util.List;

@Data
public class OrderReport {
    private int numberOfOrders;
    private double totalOfOrders;
    private List<OrderItem> orders;
}
