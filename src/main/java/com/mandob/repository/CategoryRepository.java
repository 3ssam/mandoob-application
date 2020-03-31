package com.mandob.repository;

import com.mandob.base.Projection.LookupProjection;
import com.mandob.base.repository.MasterRepository;
import com.mandob.domain.Category;

import java.util.List;

public interface CategoryRepository extends MasterRepository<Category> {
    List<LookupProjection> findAllByParentIdAndCompanyId(String parentId, String companyId);


}
