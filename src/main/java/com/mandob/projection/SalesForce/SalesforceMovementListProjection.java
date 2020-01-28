package com.mandob.projection.SalesForce;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mandob.projection.Customer.CustomerListProjection;

import java.time.LocalDateTime;

@JsonPropertyOrder({"id", "longitude", "latitude", "dateTime", "status","customer"})
public interface SalesforceMovementListProjection {
    String getId();

    String getLongitude();

    String getLatitude();

    LocalDateTime getDateTime();

    String getStatus();

    CustomerListProjection getCustomer();
}
