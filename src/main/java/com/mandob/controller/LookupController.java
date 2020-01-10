package com.mandob.controller;

import com.mandob.base.Projection.LookupProjection;
import com.mandob.service.CityService;
import com.mandob.service.GovernmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("lookups")
public class LookupController {
    private final CityService cityService;
    private final GovernmentService governmentService;

    @GetMapping("governments")
    public List<LookupProjection> findAllGovernments() {
        return governmentService.findAll();
    }

    @GetMapping("governments/{governmentId}")
    public LookupProjection findGovernmentById(@PathVariable String governmentId) {
        return governmentService.findById(governmentId, LookupProjection.class);
    }

    @GetMapping("governments/{governmentId}/cities")
    public List<LookupProjection> findAllGovernmentCities(@PathVariable String governmentId) {
        return cityService.findAll(governmentId);
    }

    @GetMapping("cities")
    public List<LookupProjection> findAllCities() {
        return cityService.findAll();
    }

    @GetMapping("cities/{cityId}")
    public LookupProjection findCityById(@PathVariable String cityId) {
        return cityService.findById(cityId, LookupProjection.class);
    }

}
