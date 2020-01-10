package com.mandob.service;

import com.mandob.base.Projection.LookupProjection;
import com.mandob.base.repository.BaseRepository;
import com.mandob.base.service.BaseService;
import com.mandob.domain.lookup.City;
import com.mandob.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService extends BaseService<City> {
    private final CityRepository cityRepository;

    public List<LookupProjection> findAll() {
        return cityRepository.findAllAsLookupBy();
    }

    public List<LookupProjection> findAll(String governmentId) {
        return cityRepository.findAllByGovernmentId(governmentId);
    }

    @Override
    protected BaseRepository<City> getRepository() {
        return cityRepository;
    }
}
