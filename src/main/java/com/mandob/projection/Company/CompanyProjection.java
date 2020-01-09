package com.mandob.projection.Company;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mandob.base.Projection.MasterProjection;

@JsonPropertyOrder({"id", "arName", "enName", "suspended", "createdAt", "updatedAt", "company", "createdBy", "updatedBy"})
public interface CompanyProjection extends MasterProjection {
    Boolean getSuspended();
}
