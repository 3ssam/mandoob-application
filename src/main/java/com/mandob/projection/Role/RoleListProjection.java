package com.mandob.projection.Role;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mandob.base.Projection.LookupProjection;

import java.time.Instant;

@JsonPropertyOrder({"id", "name", "arName", "enName"})
public interface RoleListProjection extends LookupProjection {


}
