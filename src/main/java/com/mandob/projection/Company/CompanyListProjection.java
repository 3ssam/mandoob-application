package com.mandob.projection.Company;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mandob.base.Projection.LookupProjection;

import java.time.Instant;

@JsonPropertyOrder({"id", "name", "suspended", "createdAt", "updatedAt"})
public interface CompanyListProjection extends LookupProjection {
    Boolean getSuspended();

    Instant getCreatedAt();

    Instant getUpdatedAt();
}
