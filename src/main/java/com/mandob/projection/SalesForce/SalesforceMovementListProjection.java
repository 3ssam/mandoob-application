package com.mandob.projection.SalesForce;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mandob.projection.Customer.CustomerListProjection;

@JsonPropertyOrder({"id", "longitude", "latitude", "address", "dateTime", "status", "customer", "salesforce"})
public interface SalesforceMovementListProjection {
    String getId();

    String getLongitude();

    String getLatitude();

    String getAddress();

    String getDateTime();

    String getStatus();

    CustomerListProjection getCustomer();

    SalesforceListProjection getsalesforce();

}
