package com.mandob.repository;

import com.mandob.base.repository.MasterRepository;
import com.mandob.domain.Role;

import java.util.List;

public interface RoleRepository extends MasterRepository<Role> {
    List<Role> findByEnName(String enName);
}
