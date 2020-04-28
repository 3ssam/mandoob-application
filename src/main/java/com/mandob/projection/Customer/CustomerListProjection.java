package com.mandob.projection.Customer;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mandob.base.Projection.LookupProjection;
import com.mandob.projection.User.UserListProjection;

@JsonPropertyOrder({"id", "name", "arName", "enName", "email", "activated", "suspended", "createdAt", "updatedAt", "longitude", "latitude"})
public interface CustomerListProjection extends UserListProjection {
    LookupProjection getRoute();

    LookupProjection getGovernment();

    LookupProjection getCity();

    String getLongitude();

    String getLatitude();

}
