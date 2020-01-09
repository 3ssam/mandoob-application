package com.mandob.projection.User;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mandob.base.Projection.LookupProjection;

import java.time.Instant;

@JsonPropertyOrder({"id", "name", "arName", "enName", "email", "activated", "suspended", "createdAt", "updatedAt"})
public interface UserListProjection extends LookupProjection {
    String getEmail();

    Boolean getActivated();

    Boolean getSuspended();

    Instant getCreatedAt();

    Instant getUpdatedAt();
}
