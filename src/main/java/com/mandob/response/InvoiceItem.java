package com.mandob.response;

import lombok.Data;

@Data
public class InvoiceItem {
    private String orderId;
    private String status;
    private String customerArabicName;
    private String customerEnglishName;
    private String salesAgentArabicName;
    private String salesAgentEnglishName;
    private String totalOfOrder;
    private String amountPaid;
    private String amountRemain;
    private String payingType;
    private String amountCashCollection;
    private String InstallmentNumber;
    private String invoiceDate;
}
