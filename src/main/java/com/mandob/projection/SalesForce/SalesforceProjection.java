package com.mandob.projection.SalesForce;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mandob.base.Projection.LookupProjection;
import com.mandob.base.Projection.MasterProjection;
import com.mandob.domain.enums.SalesforceRole;

@JsonPropertyOrder({"id", "name", "arName", "enName", "email", "employeeCode", "salesforceRole", "assignedTo",
        "phoneNumber", "nationalId", "detailedAddress", "government", "city",
        "activated", "suspended", "createdAt", "updatedAt"})
public interface SalesforceProjection extends MasterProjection {
    String getEmail();

    String getEmployeeCode();

    SalesforceRole getSalesforceRole();

    LookupProjection getAssignedTo();

    String getPhoneNumber();

    String getNationalId();

    String getDetailedAddress();

    LookupProjection getGovernment();

    LookupProjection getCity();

    Boolean getActivated();

    Boolean getSuspended();
}
