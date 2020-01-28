package com.mandob.projection.Product;

import com.mandob.base.Projection.LookupProjection;

import java.math.BigDecimal;
import java.time.Instant;

public interface ProductListProjection extends LookupProjection {
    LookupProjection getCategory();

    LookupProjection getSubCategory();

    String getDescription();

    BigDecimal getPrice();

    Instant getCreatedAt();

    Instant getUpdatedAt();

    BigDecimal getWeight();
}
