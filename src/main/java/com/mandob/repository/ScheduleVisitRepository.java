package com.mandob.repository;

import com.mandob.base.repository.AuditRepository;
import com.mandob.domain.Customer;
import com.mandob.domain.Salesforce;
import com.mandob.domain.ScheduleVisit;
import com.mandob.domain.enums.ScheduleVisitStatus;
import com.mandob.projection.schedulevisit.ScheduleVisitListProjection;
import com.mandob.projection.schedulevisit.ScheduleVisitProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleVisitRepository extends AuditRepository<ScheduleVisit> {
    Optional<ScheduleVisit> findByCustomerIdAndScheduleDateBetweenAndCompanyId(String customerId, String from, String to, String companyId);

    List<ScheduleVisit> findAllBySalesforceIdAndScheduleDateBetweenAndCompanyId(String salesforceId, String from, String ro, String companyId);

    Page<ScheduleVisitProjection> findAllBySalesforceIdOrCustomerIdAndScheduleDateBetween(String salesforce_id, String customer_id, LocalDateTime scheduleDate, LocalDateTime scheduleDate2, Pageable pageable);

    Page<ScheduleVisitListProjection> findAllBySalesforce(Salesforce salesforce, Pageable pageable);

    List<ScheduleVisitListProjection> findAllBySalesforce(Salesforce salesforce);

    Page<ScheduleVisitListProjection> findAllByCustomer(Customer customer, Pageable Pageable);

    ScheduleVisit findBySalesforceAndCustomer(Salesforce salesforce, Customer customer);

//    Optional<ScheduleVisit> findBySalesforceIdAndScheduleDateBetweenAndCompanyId(String salesforceId, String d1, String d2, String companyId);

    List<ScheduleVisitListProjection> findAllBySalesforceAndScheduleDateBetween(Salesforce salesforce, String from, String to);

    List<ScheduleVisitListProjection> findAllBySalesforceAndCustomerAndScheduleDateBetween(Salesforce salesforce, Customer customer, String from, String to);

    ScheduleVisit findBySalesforceAndCustomerAndScheduleDateBetween(Salesforce salesforce, Customer customer, String from, String to);

    List<ScheduleVisit> findByVisitStatusAndScheduleDateBetween(ScheduleVisitStatus status, String from, String to);

    List<ScheduleVisitListProjection> findAllByCustomerAndSalesforce(Customer customer, Salesforce salesforce);
}
