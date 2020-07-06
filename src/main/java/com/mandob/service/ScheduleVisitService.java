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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ScheduleVisitService extends AuditService<ScheduleVisit> {
    private final CustomerService customerService;
    private final SalesForceServices salesforceService;
    private final ScheduleVisitRepository scheduleVisitRepository;
    private final UserService userService;

    public static int getDurationBetweenDate(LocalDateTime d1, LocalDateTime d2) {
        int Duration = 0;
        if (d1.isBefore(d2))
            Duration = d2.getHour() - d1.getHour();
        else
            Duration = d1.getHour() - d2.getHour();
        return Duration;
    }

    @Transactional
    public ScheduleVisitProjection create(ScheduleVisitReq req) {
        if (!userService.findById(req.getCurrentUser()).getRole().getEnName().equals("sales_manager") && !userService.findById(req.getCurrentUser()).getRole().getEnName().equals("superuser"))
            throw new ApiValidationException("Error !", "User is not allowed to create a visit, the user have right to create user his role must be salesforce manger");
        validateDateGreaterThanToday(req.getScheduleDate());
        Customer customer = customerService.findById(req.getCustomer());
        Salesforce salesforce = salesforceService.findById(req.getSalesforce());
        validateCustomerAvailability(customer, req.getScheduleDate(), null, req.getCurrentUser());
        validateSalesforceAvailability(salesforce, req.getScheduleDate(), customer, null, req.getCurrentUser());
        ScheduleVisit scheduleVisit = new ScheduleVisit();//scheduleVisitMapper.toEntity(req);
        scheduleVisit.setCreatedBy(userService.findById(req.getCurrentUser()));
        scheduleVisit.setCreatedAt(Instant.now());
        scheduleVisit.setCustomer(customer);
        scheduleVisit.setSalesforce(salesforce);
        scheduleVisit.setMovementStatus("NEW");
        scheduleVisit.setVisitStatus(ScheduleVisitStatus.PLANNED);
        scheduleVisit = createNewScheduleVisit(req, scheduleVisit);
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
        scheduleVisit = createNewScheduleVisit(req, scheduleVisit);
        validateCustomerAvailability(customer, req.getScheduleDate(), scheduleVisit, req.getCurrentUser());
        validateSalesforceAvailability(salesforce, req.getScheduleDate(), customer, scheduleVisit, req.getCurrentUser());
        scheduleVisitRepository.save(scheduleVisit);
        return findById(scheduleVisit.getId(), ScheduleVisitProjection.class);
    }

    @Transactional
    public ScheduleVisitProjection ChangeVisiteStatus(String customerId) {
        Customer customer = customerService.findById(customerId);
        if (customer == null)
            throw new ApiValidationException("Customer Id", "not-exist");
        Salesforce salesforce = customer.getSalesforce();//customer.getAssignedTo();
        if (salesforce == null)
            throw new ApiValidationException("Salesforce Id", "not-exist");
        ScheduleVisit scheduleVisit = scheduleVisitRepository.findBySalesforceAndCustomer(salesforce, customer);
        scheduleVisit.setVisitStatus(ScheduleVisitStatus.ACHIEVED);
        scheduleVisitRepository.save(scheduleVisit);
        return findById(scheduleVisit.getId(), ScheduleVisitProjection.class);
    }

    public List<ScheduleVisitListProjection> getAllScheduleVisitForSalesForce(String salesforceId) {
        Salesforce salesforce = salesforceService.findById(salesforceId);
        if (salesforce == null)
            throw new ApiValidationException("Salesforce Id", "not-exist");
        List<ScheduleVisitListProjection> list = scheduleVisitRepository.findAllBySalesforce(salesforce);
        return list;
    }


    private void validateDateGreaterThanToday(LocalDateTime scheduleDate) {
        LocalDateTime todayEnd = LocalDateTime.now().withHour(23).withMinute(59);
        if (scheduleDate.isBefore(todayEnd))
            throw new ApiValidationException("scheduleDate", "must-be-after-today");
    }

    private void validateVisitOlderThanToday(ScheduleVisit scheduleVisit) {
        LocalDateTime todayEnd = LocalDateTime.now().withHour(23).withMinute(59);
        LocalDateTime scheduleDate = LocalDateTime.parse(scheduleVisit.getScheduleDate());
        if (scheduleDate.isBefore(todayEnd))
            throw new ApiException("Error!", "Can not update older visits");
    }

    public ScheduleVisit createNewScheduleVisit(ScheduleVisitReq req, ScheduleVisit scheduleVisit) {
        scheduleVisit.setPartialPayAllowed(req.getPartialPayAllowed());
        scheduleVisit.setScheduleDate(req.getScheduleDate().toString());
        scheduleVisit.setVisitType(req.getVisitType());
        scheduleVisit.setUpdatedBy(userService.findById(req.getCurrentUser()));
        scheduleVisit.setCompany(scheduleVisit.getCreatedBy().getCompany());
        scheduleVisit.setUpdatedAt(Instant.now());
        scheduleVisit.setLongitude(scheduleVisit.getCustomer().getLongitude());
        scheduleVisit.setLatitude(scheduleVisit.getCustomer().getLatitude());
        scheduleVisit.setAddress(scheduleVisit.getCustomer().getAddress());
        return scheduleVisit;
    }

    private void validateSalesforceAvailability(Salesforce salesforce, LocalDateTime date, Customer customer, ScheduleVisit currentVisit, String currentUser) {
        LocalDateTime start = LocalDateTime.of(date.toLocalDate(), date.toLocalTime()).withHour(0).withMinute(0);
        LocalDateTime end = LocalDateTime.of(date.toLocalDate(), date.toLocalTime()).withHour(23).withMinute(59);
        List<ScheduleVisit> scheduleVisit = scheduleVisitRepository
                .findAllBySalesforceIdAndScheduleDateBetweenAndCompanyId(salesforce.getId(), start.toString(), end.toString(), userService.findById(currentUser).getCompany().getId());
//        scheduleVisit.ifPresent(v -> {
//            if (currentVisit == null || ObjectUtils.notEqual(currentVisit.getId(), v.getId()))
//                throw new ApiValidationException("salesforce", "already-has-visit-on-this-day");
//        });
        for (int i = 0; i < scheduleVisit.size(); i++) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime to = null;
            try {
                to = LocalDateTime.parse(scheduleVisit.get(i).getScheduleDate(), formatter);
            } catch (Exception e) {
                to = LocalDateTime.parse(scheduleVisit.get(i).getScheduleDate());
            }
            int duration = getDurationBetweenDate(date, to);
            if (duration < 2 || scheduleVisit.get(i).getCustomer().getId().equalsIgnoreCase(customer.getId()))
                throw new ApiValidationException("salesforce", "already-has-visit-on-this-day");
            //if (date )
        }

    }

    private void validateCustomerAvailability(Customer customer, LocalDateTime date, ScheduleVisit currentVisit, String currentUser) {
        LocalDateTime start = LocalDateTime.of(date.toLocalDate(), date.toLocalTime()).withHour(0).withMinute(0);
        LocalDateTime end = LocalDateTime.of(date.toLocalDate(), date.toLocalTime()).withHour(23).withMinute(59);
        Optional<ScheduleVisit> scheduleVisit = scheduleVisitRepository
                .findByCustomerIdAndScheduleDateBetweenAndCompanyId(customer.getId(), start.toString(), end.toString(), userService.findById(currentUser).getCompany().getId());
        scheduleVisit.ifPresent(v -> {
            if (currentVisit == null || ObjectUtils.notEqual(currentVisit.getId(), v.getId()))
                throw new ApiValidationException("customer", "already-has-visit-on-this-day");
        });
    }

    @Transactional(readOnly = true)
    public Page<ScheduleVisitProjection> findCurrentUserTodayVisits(PageRequestVM pr, String currentUser) {
        String id = userService.findById(currentUser).getId();
        LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0);
        LocalDateTime end = LocalDateTime.now().withHour(23).withMinute(59);
        return scheduleVisitRepository.findAllBySalesforceIdOrCustomerIdAndScheduleDateBetween(id, id, start, end, pr.build());
    }

    public Page<ScheduleVisitListProjection> getVisits(PageRequestVM pr, String salesForceId, String customerId) {
        if ((salesForceId == null || salesForceId.isEmpty() || salesForceId.isBlank()) &&
                (customerId == null || customerId.isEmpty() || customerId.isBlank()))
            return findAll(ScheduleVisitListProjection.class, pr);
        Page<ScheduleVisitListProjection> page = null;
        if ((salesForceId == null || salesForceId.isEmpty() || salesForceId.isBlank())) {
            Customer customer = customerService.findById(customerId);
            if (customer == null)
                throw new ApiValidationException("Customer Id", "not-exist");
            page = scheduleVisitRepository.findAllByCustomer(customer, pr.build());
        } else {
            Salesforce salesforce = salesforceService.findById(salesForceId);
            if (salesforce == null)
                throw new ApiValidationException("Salesforce Id", "not-exist");
            page = scheduleVisitRepository.findAllBySalesforce(salesforce, pr.build());
        }
        return page;
    }

    @Override
    protected BaseRepository<ScheduleVisit> getRepository() {
        return scheduleVisitRepository;
    }


}
