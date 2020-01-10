package com.mandob.repository;

import com.mandob.base.repository.BaseRepository;
import com.mandob.domain.SalesforceMovement;
import com.mandob.projection.SalesForce.SalesforceMovementListProjection;
import java.util.List;

public interface SalesforceMovementRepository extends BaseRepository<SalesforceMovement> {
    List<SalesforceMovementListProjection> findAllBySalesforceId(String salesforceId);
}
