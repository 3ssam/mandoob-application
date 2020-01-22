package com.mandob.projection.SalesForce;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDateTime;

@JsonPropertyOrder({"id", "longitude", "latitude", "dateTime", "status"})
public interface SalesforceMovementListProjection {
    String getId();

    String getLongitude();

    String getLatitude();

    LocalDateTime getDateTime();

    String getStatus();
}
