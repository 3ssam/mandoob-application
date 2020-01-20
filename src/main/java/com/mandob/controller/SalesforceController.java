package com.mandob.controller;

import com.mandob.base.Projection.LookupProjection;
import com.mandob.base.Utils.ApiPageResponse;
import com.mandob.base.Utils.ApiResponse;
import com.mandob.base.Utils.PageRequestVM;
import com.mandob.domain.Salesforce;
import com.mandob.projection.SalesForce.SalesforceListProjection;
import com.mandob.projection.SalesForce.SalesforceMovementListProjection;
import com.mandob.projection.SalesForce.SalesforceProjection;
import com.mandob.request.MovementReq;
import com.mandob.request.SalesforceReq;
import com.mandob.service.SalesForceServices;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("salesforce")
public class SalesforceController {
    private final SalesForceServices salesforceService;

    @GetMapping
    public ApiPageResponse<SalesforceListProjection> findAllSalesforce(PageRequestVM pr) {
        return ApiPageResponse.of(salesforceService.findAll(SalesforceListProjection.class, pr));
    }

    @GetMapping("{salesforceId}")
    public ApiResponse<SalesforceProjection> findSalesforceById(@PathVariable String salesforceId) {
        return ApiResponse.ok(salesforceService.findById(salesforceId, SalesforceProjection.class));
    }

    @GetMapping("role/{roleId}")
    public List<SalesforceListProjection> findAllSalesforceByrole(@PathVariable String roleId) {
        return salesforceService.getSalesforceByRole(roleId);
    }


    @PostMapping
    public ApiResponse<SalesforceProjection> createSalesforce(@Valid @RequestBody SalesforceReq req) {
        return ApiResponse.created(salesforceService.create(req));
    }

    @PutMapping("{salesforceId}")
    public ApiResponse<SalesforceProjection> updateSalesforce(@PathVariable String salesforceId, @Valid @RequestBody SalesforceReq req) {
        return ApiResponse.updated(salesforceService.update(salesforceId, req));
    }

    @PostMapping("movement")
    public void postMovement(@Valid @RequestBody MovementReq req) {
        salesforceService.postMovement(req);
    }

    @GetMapping("{salesforceId}/movements")
    public List<SalesforceMovementListProjection> findAllSalesforceMovements(@PathVariable String salesforceId) {
        return salesforceService.findMovementsBy(salesforceId);
    }

    @GetMapping("lookup")
    public List<LookupProjection> lookup(String id) {
        return salesforceService.lookup(id);
    }
}
