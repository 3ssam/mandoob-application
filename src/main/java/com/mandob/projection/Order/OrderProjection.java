package com.mandob.projection.Order;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mandob.base.Projection.AuditProjection;
import com.mandob.domain.Product;

import java.util.List;

@JsonPropertyOrder({"id", "createdAt", "updatedAt", "totalOrder", "orderAmount", "tempProducts"})
public interface OrderProjection extends AuditProjection {
    double getTotalOrder();

    List<Integer> getOrderAmount();

    List<Product> getTempProducts();
}
