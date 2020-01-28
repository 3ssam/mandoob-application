package com.mandob.service;

import com.mandob.base.Utils.PageRequestVM;
import com.mandob.base.exception.ApiException;
import com.mandob.base.exception.ApiValidationException;
import com.mandob.base.repository.BaseRepository;
import com.mandob.base.service.AuditService;
import com.mandob.domain.Customer;
import com.mandob.domain.Salesforce;
import com.mandob.domain.ScheduleVisit;
import com.mandob.domain.enums.ScheduleVisitStatus;
import com.mandob.projection.schedulevisit.ScheduleVisitListProjection;
import com.mandob.projection.schedulevisit.ScheduleVisitProjection;
import com.mandob.repository.ScheduleVisitRepository;
import com.mandob.request.ScheduleVisitReq;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ScheduleVisitService extends AuditService<ScheduleVisit> {
    private final CustomerService customerService;
    private final SalesForceServices salesforceService;
//    private final ScheduleVisitMapper scheduleVisitMapper;
    private final ScheduleVisitRepository scheduleVisitRepository;
    private final UserService userService;

    @Transactional
    public ScheduleVisitProjection create(ScheduleVisitReq req) {
        if (!userService.findById(req.getCurrentUser()).getRole().getEnName().equals("sales_manager"))
            throw new ApiValidationException("Error !", "User is not allowed to create a visit, the user have right to create user his role must be salesforce manger");
        validateDateGreaterThanToday(req.getScheduleDate());
        Customer customer = customerService.findById(req.getCustomer());
        Salesforce salesforce = salesforceService.findById(req.getSalesforce());
        validateCustomerAvailability(customer, req.getScheduleDate(), null,req.getCurrentUser());
        validateSalesforceAvailability(salesforce, req.getScheduleDate(), null,req.getCurrentUser());
        ScheduleVisit scheduleVisit = new ScheduleVisit();//scheduleVisitMapper.toEntity(req);
        scheduleVisit.setCreatedBy(userService.findById(req.getCurrentUser()));
        scheduleVisit.setCreatedAt(Instant.now());
        scheduleVisit.setCustomer(customer);
        scheduleVisit.setSalesforce(salesforce);
        scheduleVisit.setVisitStatus(ScheduleVisitStatus.PLANNED);
        scheduleVisit = createNewScheduleVisit(req,scheduleVisit);
        scheduleVisitRepository.save(scheduleVisit);
        return findById(scheduleVisit.getId(), ScheduleVisitProjection.class);
    }

    @Transactional
    public ScheduleVisitProjection update(String id, ScheduleVisitReq req) {
        ScheduleVisit scheduleVisit = findById(id);
        validateVisitOlderThanToday(scheduleVisit);
        Customer customer = customerService.findById(req.getCustomer());
        Salesforce salesforce = salesforceService.findById(req.getSalesforce());
        scheduleVisit.setCustomer(customer);
        scheduleVisit.setSalesforce(salesforce);
        scheduleVisit = createNewScheduleVisit(req,scheduleVisit);
        validateCustomerAvailability(customer, req.getScheduleDate(), scheduleVisit,req.getCurrentUser());
        validateSalesforceAvailability(salesforce, req.getScheduleDate(), scheduleVisit,req.getCurrentUser());
        scheduleVisitRepository.save(scheduleVisit);
        return findById(scheduleVisit.getId(), ScheduleVisitProjection.class);
    }

    public ScheduleVisit createNewScheduleVisit(ScheduleVisitReq req,ScheduleVisit scheduleVisit){
        scheduleVisit.setPartialPayAllowed(req.getPartialPayAllowed());
        scheduleVisit.setScheduleDate(req.getScheduleDate());
        scheduleVisit.setVisitType(req.getVisitType());
        scheduleVisit.setUpdatedBy(userService.findById(req.getCurrentUser()));
        scheduleVisit.setCompany(scheduleVisit.getCreatedBy().getCompany());
        scheduleVisit.setUpdatedAt(Instant.now());
        return scheduleVisit;
    }

    @Transactional
    public ScheduleVisitProjection ChangeVisiteStatus(String customerId){
        Customer customer = customerService.findById(customerId);
        if (customer == null)
            throw new ApiValidationException("Customer Id", "not-exist");
        Salesforce salesforce = customer.getAssignedTo();
        if (salesforce == null)
            throw new ApiValidationException("Salesforce Id", "not-exist");
        ScheduleVisit scheduleVisit = scheduleVisitRepository.findBySalesforceAndCustomer(salesforce,customer);
        scheduleVisit.setVisitStatus(ScheduleVisitStatus.ACHIEVED);
        scheduleVisitRepository.save(scheduleVisit);
        return findById(scheduleVisit.getId(), ScheduleVisitProjection.class);
    }

    public List<ScheduleVisitListProjection> getAllScheduleVisitForSalesForce(String salesforceId){
        Salesforce salesforce = salesforceService.findById(salesforceId);
        if (salesforce == null)
            throw new ApiValidationException("Salesforce Id", "not-exist");
        List<ScheduleVisitListProjection> list  = scheduleVisitRepository.findAllBySalesforce(salesforce);
        return list;
    }


    private void validateDateGreaterThanToday(LocalDateTime scheduleDate) {
        LocalDateTime todayEnd = LocalDateTime.now().withHour(23).withMinute(59);
        if (scheduleDate.isBefore(todayEnd))
            throw new ApiValidationException("scheduleDate", "must-be-after-today");
    }

    private void validateVisitOlderThanToday(ScheduleVisit scheduleVisit) {
        LocalDateTime todayEnd = LocalDateTime.now().withHour(23).withMinute(59);
        if (scheduleVisit.getScheduleDate().isBefore(todayEnd))
            throw new ApiException("Error!", "Can not update older visits");
    }

    private void validateSalesforceAvailability(Salesforce salesforce, LocalDateTime date, ScheduleVisit currentVisit,String currentUser) {
        LocalDateTime start = LocalDateTime.of(date.toLocalDate(), date.toLocalTime()).withHour(0).withMinute(0);
        LocalDateTime end = LocalDateTime.of(date.toLocalDate(), date.toLocalTime()).withHour(23).withMinute(59);
        Optional<ScheduleVisit> scheduleVisit = scheduleVisitRepository
                .findBySalesforceIdAndScheduleDateBetweenAndCompanyId(salesforce.getId(), start, end, userService.findById(currentUser).getCompany().getId());
        scheduleVisit.ifPresent(v -> {
            if (currentVisit == null || ObjectUtils.notEqual(currentVisit.getId(), v.getId()))
                throw new ApiValidationException("salesforce", "already-has-visit-on-this-day");
        });
    }

    private void validateCustomerAvailability(Customer customer, LocalDateTime date, ScheduleVisit currentVisit,String currentUser) {
        LocalDateTime start = LocalDateTime.of(date.toLocalDate(), date.toLocalTime()).withHour(0).withMinute(0);
        LocalDateTime end = LocalDateTime.of(date.toLocalDate(), date.toLocalTime()).withHour(23).withMinute(59);
        Optional<ScheduleVisit> scheduleVisit = scheduleVisitRepository
                .findByCustomerIdAndScheduleDateBetweenAndCompanyId(customer.getId(), start, end, userService.findById(currentUser).getCompany().getId());
        scheduleVisit.ifPresent(v -> {
            if (currentVisit == null || ObjectUtils.notEqual(currentVisit.getId(), v.getId()))
                throw new ApiValidationException("customer", "already-has-visit-on-this-day");
        });
    }

    @Transactional(readOnly = true)
    public Page<ScheduleVisitProjection> findCurrentUserTodayVisits(PageRequestVM pr,String currentUser) {
        String id = userService.findById(currentUser).getId();
        LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0);
        LocalDateTime end = LocalDateTime.now().withHour(23).withMinute(59);
        return scheduleVisitRepository.findAllBySalesforceIdOrCustomerIdAndScheduleDateBetween(id, id, start, end, pr.build());
    }

    @Override
    protected BaseRepository<ScheduleVisit> getRepository() {
        return scheduleVisitRepository;
    }
}