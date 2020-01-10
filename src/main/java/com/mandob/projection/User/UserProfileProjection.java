package com.mandob.projection.User;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mandob.base.Projection.LookupProjection;

@JsonPropertyOrder({"id", "arName", "enName", "email", "company"})
public interface UserProfileProjection {
    String getId();

    String getArName();

    String getEnName();

    String getEmail();

    LookupProjection getCompany();
}
