package com.mandob.projection.Invoice;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mandob.base.Projection.AuditProjection;
import com.mandob.domain.enums.InvoiceStatus;
import com.mandob.domain.enums.PayingType;

@JsonPropertyOrder({"id", "createdAt", "updatedAt", "totalAmount", "amountPaid", "amountRemain", "payingType", "status"})
public interface InvoiceListProjection extends AuditProjection {

    double getTotalAmount();

    double getAmountPaid();

    double getAmountRemain();

    PayingType getPayingType();

    InvoiceStatus getStatus();

}
