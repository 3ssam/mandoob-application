package com.mandob.projection.Category;

import com.mandob.base.Projection.LookupProjection;
import com.mandob.base.Projection.MasterProjection;

public interface CategoryProjection extends MasterProjection {
    LookupProjection getParent();
}
