package com.mandob.projection.Product;

import com.mandob.base.Projection.LookupProjection;
import com.mandob.base.Projection.MasterProjection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ProductProjection extends MasterProjection {
    LookupProjection getCategory();

    LookupProjection getSubCategory();

    String getDescription();

    Long getBarcode();

    BigDecimal getPrice();

    String getDimension();

    String getColor();

    LocalDate getProdDate();

    LocalDate getExpiryDate();

    String getPhotoUrl();

    BigDecimal getWeight();
}
