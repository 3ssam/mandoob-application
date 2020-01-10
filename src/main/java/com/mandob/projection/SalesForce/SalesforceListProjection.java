package com.mandob.projection.SalesForce;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mandob.domain.enums.SalesforceRole;
import com.mandob.projection.User.UserListProjection;

@JsonPropertyOrder({"id", "name", "arName", "enName", "email", "salesforceRole", "activated", "suspended", "createdAt", "updatedAt"})
public interface SalesforceListProjection extends UserListProjection {
    SalesforceRole getSalesforceRole();
}
