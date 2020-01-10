package com.mandob.projection.SalesForce;

import java.time.LocalDateTime;

public interface SalesforceMovementListProjection {
    String getId();

    String getLongitude();

    String getLatitude();

    LocalDateTime dateTime();
}
