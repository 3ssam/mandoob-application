package com.mandob.projection.Stock;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mandob.base.Projection.AuditProjection;
import com.mandob.domain.Product;

@JsonPropertyOrder({"id", "createdAt", "updatedAt", "product", "salesforceName", "customerName", "soldItems", "stockItems"})
public interface StockProjection extends AuditProjection {

    Product getProduct();

    String getSalesforceName();

    String getCustomerName();

    long getSoldItems();

    long getStockItems();
}
