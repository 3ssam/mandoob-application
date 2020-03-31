package com.mandob.projection.Product;

import com.mandob.base.Projection.LookupProjection;
import com.mandob.base.Projection.MasterProjection;

import java.math.BigDecimal;

public interface ProductProjection extends MasterProjection {
    LookupProjection getCategory();

    LookupProjection getSubCategory();

    String getDescription();

    Long getBarcode();

    BigDecimal getPrice();

    String getDimension();

    String getColor();

    String getProdDate();

    String getExpiryDate();

    String getPhotoUrl();

    BigDecimal getWeight();

    int getAmount();

    int getRemainingAmount();
}
