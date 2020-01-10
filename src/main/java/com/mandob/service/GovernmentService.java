package com.mandob.service;

import com.mandob.base.Projection.LookupProjection;
import com.mandob.base.repository.BaseRepository;
import com.mandob.base.service.BaseService;
import com.mandob.domain.lookup.Government;
import com.mandob.repository.GovernmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GovernmentService extends BaseService<Government> {
    private final GovernmentRepository governmentRepository;

    public List<LookupProjection> findAll() {
        return governmentRepository.findAllAsLookupBy();
    }

    @Override
    protected BaseRepository<Government> getRepository() {
        return governmentRepository;
    }
}
