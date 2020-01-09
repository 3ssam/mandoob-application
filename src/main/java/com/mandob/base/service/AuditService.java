package com.mandob.base.service;

import com.mandob.base.Utils.PageRequestVM;
import com.mandob.base.domain.EntityAudit;
import com.mandob.base.repository.AuditRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;

import java.util.List;

public abstract class AuditService<T extends EntityAudit> extends BaseService<T> {

    public T findById(String id,String companyId) {
        return getAuditRepository().findByIdAndCompanyId(id, companyId).orElseThrow(noSuchElement(id));
    }

    public <R> R findById(String id, Class<R> projection,String companyId) {
        return getAuditRepository().findByIdAndCompanyId(id, companyId, projection).orElseThrow(noSuchElement(id));
    }

    public <R> Page<R> findAll(Class<R> projection, PageRequestVM pr,String companyId) {
        return getAuditRepository().findAllByCompanyId(companyId, projection, pr.build());
    }

    public List<T> findAll(Example<T> example) {
        return getAuditRepository().findAll(example);
    }

    private AuditRepository<T> getAuditRepository() {
        return (AuditRepository<T>) getRepository();
    }
}
