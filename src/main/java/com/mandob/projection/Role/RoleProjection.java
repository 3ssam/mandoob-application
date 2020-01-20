package com.mandob.projection.Role;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mandob.base.Projection.LookupProjection;
import com.mandob.base.Projection.MasterProjection;

@JsonPropertyOrder({"id","arName","enName"})
public interface RoleProjection extends MasterProjection {
}