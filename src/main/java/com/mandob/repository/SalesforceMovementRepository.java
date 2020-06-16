package com.mandob.repository;

import com.mandob.base.repository.BaseRepository;
import com.mandob.domain.Customer;
import com.mandob.domain.Salesforce;
import com.mandob.domain.SalesforceMovement;
import com.mandob.projection.SalesForce.SalesforceMovementListProjection;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SalesforceMovementRepository extends BaseRepository<SalesforceMovement> {

    List<SalesforceMovementListProjection> findAllBySalesforceId(String salesforceId);

    List<SalesforceMovementListProjection> findAllBySalesforceIdAndDateTimeBetween(String salesforceId, String start, String end);

    List<SalesforceMovementListProjection> findBySalesforceAndCustomerAndAndDateTimeBetween(Salesforce salesforce, Customer customer, String start, String end);

    List<SalesforceMovementListProjection> findAllByDateTimeBetween(String start, String end);

//    @Query(value = "select * from salesforce_movement as firsttable inner join\n" +
//            "(select salesforce_id as second_id ,max(date_time) as Max_Date from salesforce_movement group by salesforce_id) secondtable\n" +
//            "on firsttable.salesforce_id = secondtable.second_id\n" +
//            "and firsttable.date_time = secondtable.Max_Date\n",nativeQuery = true)
//    List<SalesforceMovementListProjection> findAllByLatestDateTime();


    @Query(value = "SELECT * from salesforce_movement WHERE date_time = (select max(date_time) from salesforce_movement)", nativeQuery = true)
    SalesforceMovement findLatestDateTime();

    SalesforceMovementListProjection getById(String id);
}
