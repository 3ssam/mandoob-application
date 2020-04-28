package com.mandob.repository;

import com.mandob.base.repository.BaseRepository;
import com.mandob.domain.Customer;
import com.mandob.domain.Salesforce;
import com.mandob.domain.SalesforceMovement;
import com.mandob.projection.SalesForce.SalesforceMovementListProjection;

import java.time.LocalDateTime;
import java.util.List;

public interface SalesforceMovementRepository extends BaseRepository<SalesforceMovement> {
    List<SalesforceMovementListProjection> findAllBySalesforceId(String salesforceId);

//    List<SalesforceMovementListProjection> findByCustomerAndAndDateTimeIsBefore(Customer customer, LocalDateTime localDateTime);

    List<SalesforceMovementListProjection> findBySalesforceAndCustomerAndAndDateTimeBetween(Salesforce salesforce, Customer customer, LocalDateTime start, LocalDateTime end);

}
