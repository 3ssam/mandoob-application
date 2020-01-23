package com.mandob.controller;

import com.mandob.base.Projection.LookupProjection;
import com.mandob.base.Utils.ApiPageResponse;
import com.mandob.base.Utils.ApiResponse;
import com.mandob.base.Utils.PageRequestVM;
import com.mandob.projection.Route.RouteListProjection;
import com.mandob.projection.Route.RouteProjection;
import com.mandob.request.RouteReq;
import com.mandob.service.RouteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("routes")
public class RouteController {
    private final RouteService routeService;

    @GetMapping
    public ApiPageResponse<RouteListProjection> findAllRoutes(PageRequestVM pr) {
        return ApiPageResponse.of(routeService.findAll(RouteListProjection.class, pr));
    }

    @GetMapping("{routId}")
    public ApiResponse<RouteProjection> findRouteById(@PathVariable String routId) {
        return ApiResponse.ok(routeService.findById(routId, RouteProjection.class));
    }

    @PostMapping
    public ApiResponse<RouteProjection> createRoute(@Valid @RequestBody RouteReq req) {
        return ApiResponse.created(routeService.create(req));
    }

    @PutMapping("{routId}")
    public ApiResponse<RouteProjection> updateRoute(@PathVariable String routId, @Valid @RequestBody RouteReq req) {
        return ApiResponse.updated(routeService.update(routId, req));
    }

    @GetMapping("lookup")
    public List<LookupProjection> lookup(String routId) {
        return routeService.lookup(routId);
    }
}
