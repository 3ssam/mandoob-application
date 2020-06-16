package com.mandob.service;

import com.mandob.base.exception.ApiValidationException;
import com.mandob.domain.Salesforce;
import com.mandob.domain.SalesforceMovement;
import com.mandob.domain.ScheduleVisit;
import com.mandob.domain.enums.ScheduleVisitStatus;
import com.mandob.projection.SalesForce.SalesforceMovementListProjection;
import com.mandob.repository.SalesforceMovementRepository;
import com.mandob.repository.SalesforceRepository;
import com.mandob.repository.ScheduleVisitRepository;
import com.mandob.request.MovementReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovementService {

    private final SalesforceRepository salesforceRepository;
    private final SalesforceMovementRepository movementRepository;
    private final ScheduleVisitRepository scheduleVisitRepository;


    @Transactional
    public void postMovement(MovementReq req) {
        Salesforce salesforce = salesforceRepository.getOne(req.getCurrentUser());
        SalesforceMovement movement = new SalesforceMovement();
        movement.setSalesforce(salesforce);
        movement.setLatitude(req.getLatitude());
        movement.setLongitude(req.getLongitude());
        movement.setStatus(req.getStatus());
        movement.setAddress(req.getAddress());
        LocalDateTime time = LocalDateTime.now();

        if (req.getStatus().name().equals("CHECKIN")) {
            if (req.getCustomer() == null)
                throw new ApiValidationException("salesforceRole", "invalid-value");
            List<ScheduleVisit> scheduleVisits = salesforce.getScheduleVisits();
            ScheduleVisit visit = null;
            //LocalDateTime time = req.getMovementDate();
            for (ScheduleVisit scheduleVisit : scheduleVisits) {
                LocalDateTime visitTime = LocalDateTime.parse(scheduleVisit.getScheduleDate());
                if (visitTime.getYear() == time.getYear() && visitTime.getMonth().equals(time.getMonth()) && visitTime.getDayOfMonth() == time.getDayOfMonth()) {
                    if (scheduleVisit.getCustomer().getId().equals(req.getCustomer())) {
                        visit = scheduleVisit;
                        break;
                    }
                }
            }
            if (visit != null) {
                visit.setVisitStatus(ScheduleVisitStatus.ACHIEVED);
                scheduleVisitRepository.save(visit);
            }

        }

        if (req.getCustomer() != null) {
            for (int i = 0; i < salesforce.getCustomers().size(); i++) {
                String customerId = salesforce.getCustomers().get(i).getId();
                if (req.getCustomer().equalsIgnoreCase(customerId))
                    movement.setCustomer(salesforce.getCustomers().get(i));
            }
        }
        movement.setDateTime(time.toString());
        movementRepository.save(movement);
    }


    @Transactional(readOnly = true)
    public List<SalesforceMovementListProjection> findMovementsBy(String salesforceId, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        if (date == null)
            date = LocalDate.now().format(formatter).toString();
        LocalDate localDate = LocalDate.parse(date, formatter);
        LocalDateTime start = localDate.atTime(0, 0, 0);
        LocalDateTime end = localDate.atTime(23, 59, 59);
        return movementRepository.findAllBySalesforceIdAndDateTimeBetween(salesforceId, start.toString(), end.toString());
    }

    public SalesforceMovementListProjection getLatestMovements() {
        return movementRepository.getById(movementRepository.findLatestDateTime().getId());
    }

}
