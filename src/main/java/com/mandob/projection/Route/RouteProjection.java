package com.mandob.projection.Route;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mandob.base.Projection.LookupProjection;
import com.mandob.base.Projection.MasterProjection;

@JsonPropertyOrder({"id", "name", "arName", "enName", "government", "city", "salesforce", "createdAt", "updatedAt"})
public interface RouteProjection extends MasterProjection {
    LookupProjection getCity();

    LookupProjection getGovernment();

    LookupProjection getSalesforce();
}
