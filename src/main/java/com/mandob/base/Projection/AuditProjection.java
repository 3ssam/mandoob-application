package com.mandob.base.Projection;

import java.time.Instant;

public interface AuditProjection  extends BaseProjection {
    Instant getCreatedAt();

    Instant getUpdatedAt();

    LookupProjection getCompany();

    LookupProjection getCreatedBy();

    LookupProjection getUpdatedBy();
}
