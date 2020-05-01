package com.mandob.response;

import lombok.Data;

@Data
public class OrderItem {
    private String orderId;
    private String status;
    private String customerArabicName;
    private String customerEnglishName;
    private String salesAgentArabicName;
    private String salesAgentEnglishName;
    private String orderDate;
    private String totalOfOrder;
}
