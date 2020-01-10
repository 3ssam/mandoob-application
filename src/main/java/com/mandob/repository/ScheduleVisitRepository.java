//package com.mandob.repository;
//
//import com.mandoob.platform.base.repository.AuditRepository;
//import com.mandoob.platform.domain.ScheduleVisit;
//import com.mandoob.platform.projection.schedulevisit.ScheduleVisitProjection;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//public interface ScheduleVisitRepository extends AuditRepository<ScheduleVisit> {
//    Optional<ScheduleVisit> findByCustomerIdAndScheduleDateBetweenAndCompanyId(String customerId, LocalDateTime d1, LocalDateTime d2, String companyId);
//
//    Optional<ScheduleVisit> findBySalesforceIdAndScheduleDateBetweenAndCompanyId(String salesforceId, LocalDateTime d1, LocalDateTime d2, String companyId);
//
//    Page<ScheduleVisitProjection> findAllBySalesforceIdOrCustomerIdAndScheduleDateBetween(String salesforce_id, String customer_id, LocalDateTime scheduleDate, LocalDateTime scheduleDate2, Pageable pageable);
//}
