package com.mandob.projection.Order;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mandob.base.Projection.AuditProjection;

@JsonPropertyOrder({"id", "createdAt", "updatedAt", "totalOrder"})
public interface OrderListProjection extends AuditProjection {
    double getTotalOrder();
}
