package com.mandob.controller;

import com.mandob.projection.SalesForce.SalesforceMovementListProjection;
import com.mandob.request.MovementReq;
import com.mandob.service.MovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("movements")
public class MovementController {

    private final MovementService movementService;


    @PostMapping()
    public void postMovement(@Valid @RequestBody MovementReq req) {
        movementService.postMovement(req);
    }

    @GetMapping("{salesforceId}")
    public List<SalesforceMovementListProjection> findAllSalesforceMovements(@PathVariable String salesforceId,
                                                                             @RequestParam(required = false) String date) {
        return movementService.findMovementsBy(salesforceId, date);
    }

    @GetMapping()
    public SalesforceMovementListProjection getLatestMovements() {
        return movementService.getLatestMovements();
    }

    @GetMapping("/active")
    public int countActiveUser() {
        return movementService.countActiveUsers();
    }

}
