package com.mandob.projection.User;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mandob.base.Projection.LookupProjection;
import com.mandob.base.Projection.MasterProjection;

@JsonPropertyOrder({"id", "arName", "enName", "email", "username", "role",
        "createdAt", "updatedAt", "company", "createdBy", "updatedBy"})
public interface UserProjection extends MasterProjection {
    String getEmail();

    LookupProjection getRole();

    LookupProjection getCompany();
}