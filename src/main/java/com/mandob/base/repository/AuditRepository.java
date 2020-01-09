package com.mandob.base.repository;

import com.mandob.base.domain.EntityAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface AuditRepository<T extends EntityAudit> extends BaseRepository<T> {
    Optional<T> findByIdAndCompanyId(String id, String companyId);

    <R> Optional<R> findByIdAndCompanyId(String id, String companyId, Class<R> projection);

    <R> Page<R> findAllByCompanyId(String companyId, Class<R> projection, Pageable pageable);
}
