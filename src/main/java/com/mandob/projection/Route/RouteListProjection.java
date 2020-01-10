package com.mandob.projection.Route;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mandob.base.Projection.LookupProjection;

import java.time.Instant;

@JsonPropertyOrder({"id", "name", "arName", "enName", "government", "city", "salesforce", "createdAt", "updatedAt"})
public interface RouteListProjection extends LookupProjection {
    LookupProjection getGovernment();

    LookupProjection getCity();

    Instant getCreatedAt();

    Instant getUpdatedAt();
}
