package com.mandob.controller;

import com.mandob.base.Utils.ApiPageResponse;
import com.mandob.base.Utils.ApiResponse;
import com.mandob.base.Utils.PageRequestVM;
import com.mandob.projection.schedulevisit.ScheduleVisitListProjection;
import com.mandob.projection.schedulevisit.ScheduleVisitProjection;
import com.mandob.request.ScheduleVisitReq;
import com.mandob.service.ScheduleVisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("visits")
public class ScheduleVisitController {
    private final ScheduleVisitService scheduleVisitService;

    @GetMapping
    public ApiPageResponse<ScheduleVisitListProjection> findAllScheduleVisits(@RequestParam PageRequestVM pr,
                                                                              @RequestParam(required = false) String salesForceId,
                                                                              @RequestParam(required = false) String customerId) {
        return ApiPageResponse.of(scheduleVisitService.getVisits(pr, salesForceId, customerId));
    }

    @GetMapping("{scheduleVisitId}")
    public ApiResponse<ScheduleVisitProjection> findScheduleVisitById(@PathVariable String scheduleVisitId) {
        return ApiResponse.ok(scheduleVisitService.findById(scheduleVisitId, ScheduleVisitProjection.class));
    }

    @GetMapping("/salesforce/{salesForceId}")
    public List<ScheduleVisitListProjection> findScheduleVisitBySalesForce(@PathVariable String salesForceId) {
        return scheduleVisitService.getAllScheduleVisitForSalesForce(salesForceId);
    }

    @PutMapping("{customerId}")
    public ApiResponse<ScheduleVisitProjection> UpdateScheduleVisitStatus(@PathVariable String customerId) {
        return ApiResponse.ok(scheduleVisitService.ChangeVisiteStatus(customerId));
    }


    @PostMapping()
    public ApiResponse<ScheduleVisitProjection> createScheduleVisit(@Valid @RequestBody ScheduleVisitReq req) {
        return ApiResponse.created(scheduleVisitService.create(req));
    }

    @PutMapping("{scheduleVisitId}")
    public ApiResponse<ScheduleVisitProjection> updateScheduleVisit(@PathVariable String scheduleVisitId, @Valid @RequestBody ScheduleVisitReq req) {
        return ApiResponse.updated(scheduleVisitService.update(scheduleVisitId, req));
    }

}
