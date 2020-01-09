package com.mandob.base.service;

import com.mandob.base.Projection.LookupProjection;
import com.mandob.base.domain.MasterFile;
import com.mandob.base.repository.MasterRepository;

import java.util.List;

public abstract class MasterService<T extends MasterFile> extends AuditService<T> {
    public List<LookupProjection> lookup(String id) {
        return getMasterRepository().findAllByCompanyId(id);
    }

    private MasterRepository<T> getMasterRepository() {
        return (MasterRepository<T>) getRepository();
    }
}
