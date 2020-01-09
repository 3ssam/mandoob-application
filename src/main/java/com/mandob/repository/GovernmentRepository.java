package com.mandob.repository;

import com.mandob.base.Projection.LookupProjection;
import com.mandob.base.repository.BaseRepository;
import com.mandob.domain.lookup.Government;

import java.util.List;

public interface GovernmentRepository extends BaseRepository<Government> {
    //List<LookupProjection> findAllAsLookupBy();
}
