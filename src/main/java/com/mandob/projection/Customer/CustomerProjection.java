package com.mandob.projection.Customer;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mandob.base.Projection.LookupProjection;
import com.mandob.base.Projection.MasterProjection;

@JsonPropertyOrder({"id", "name", "arName", "enName", "email", "route", "assignedTo", "phoneNumber2", "phoneNumber2",
         "government", "city", "activated", "suspended", "createdAt", "updatedAt"})
public interface CustomerProjection extends MasterProjection {
    String getEmail();

    String getPhoneNumber1();

    String getPhoneNumber2();

    Boolean getActivated();

    Boolean getSuspended();

    LookupProjection getRoute();

    LookupProjection getAssignedTo();

    LookupProjection getGovernment();

    LookupProjection getCity();

    String getLicenseNo();
}
