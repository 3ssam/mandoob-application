package com.mandob.repository;

import com.mandob.base.repository.MasterRepository;
import com.mandob.domain.Salesforce;
import com.mandob.domain.enums.SalesforceRole;
import com.mandob.projection.SalesForce.SalesforceListProjection;

import java.util.List;

public interface SalesforceRepository extends MasterRepository<Salesforce> {
    boolean existsByIdAndSalesforceRole(String id, SalesforceRole salesforceRole);

    boolean existsByEmployeeCodeAndCompanyId(String employeeCode, String companyId);

    boolean existsByIdNotAndEmployeeCodeAndCompanyId(String id, String employeeCode, String companyId);

    List<SalesforceListProjection> findAllBySalesforceRole(SalesforceRole salesforceRole);
}
