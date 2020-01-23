package com.mandob.service;

import com.mandob.base.repository.BaseRepository;
import com.mandob.base.service.MasterService;
import com.mandob.domain.Route;
import com.mandob.domain.enums.SalesforceRole;
import com.mandob.domain.lookup.City;
import com.mandob.projection.Route.RouteProjection;
import com.mandob.repository.RouteRepository;
import com.mandob.request.RouteReq;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@AllArgsConstructor
public class RouteService extends MasterService<Route> {
    private final CityService cityService;
    private final RouteRepository routRepository;
    private final SalesForceServices salesforceService;
    private final UserService userService;

    @Transactional
    public RouteProjection create(RouteReq req) {
        Route route = new Route();//routeMapper.toEntity(req);
        route.setCreatedAt(Instant.now());
        route.setCreatedBy(userService.findById(req.getCurrentUser()));
        route = createNewRoute(req,route);
        updateReferences(req, route);
        routRepository.save(route);
        return findById(route.getId(), RouteProjection.class);
    }

    public Route createNewRoute(RouteReq req,Route route){
        route.setCompany(userService.findById(req.getCurrentUser()).getCompany());
        route.setUpdatedBy(userService.findById(req.getCurrentUser()));
        route.setUpdatedAt(Instant.now());
        route.setArName(req.getArName());
        route.setEnName(req.getEnName());
        return route;
    }

    @Transactional
    public RouteProjection update(String id, RouteReq req) {
        Route route = findById(id);
        //routeMapper.toEntity(req, route);
        route = createNewRoute(req,route);
        updateReferences(req, route);
        routRepository.save(route);
        return findById(route.getId(), RouteProjection.class);
    }

    private void updateReferences(RouteReq req, Route route) {
        salesforceService.validateSalesforceToRole(req.getSalesforce(), SalesforceRole.SALES_AGENT, "salesforce");
        City city = cityService.findById(req.getCity());
        route.setCity(city);
        route.setGovernment(city.getGovernment());
        route.setSalesforce(salesforceService.getReference(req.getSalesforce()));
    }

    @Override
    protected BaseRepository<Route> getRepository() {
        return routRepository;
    }

}
