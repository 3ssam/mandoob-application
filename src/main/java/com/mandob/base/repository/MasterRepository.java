package com.mandob.base.repository;

//import com.mandoob.platform.base.domain.MasterFile;
//import com.mandoob.platform.base.projection.LookupProjection;


import com.mandob.base.Projection.LookupProjection;
import com.mandob.base.domain.MasterFile;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface MasterRepository<T extends MasterFile> extends AuditRepository<T> {
    boolean existsByArName(String arName);

    boolean existsByEnName(String enName);

    boolean existsByArNameAndIdNot(String arName, String id);

    boolean existsByEnNameAndIdNot(String enName, String id);

    List<LookupProjection> findAllByCompanyId(String companyId);
}
