package com.mandob.service;

import com.mandob.base.exception.ApiValidationException;
import com.mandob.base.repository.BaseRepository;
import com.mandob.base.service.MasterService;
import com.mandob.domain.Company;
import com.mandob.domain.Role;
import com.mandob.projection.Role.RoleProjection;
import com.mandob.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleService extends MasterService<Role> {

    private RoleRepository roleRepository;

//    public boolean isExist(String enName){
//        return roleRepository.existsByEnName(enName);
//    }

    public Role getId(String enName){
        List<Role> roles = roleRepository.findByEnName(enName);
        if (roles.size() == 0)
            return null;
           return roles.get(0);
    }

    public RoleProjection getRoleById(String roleId){
        Role role = roleRepository.findById(roleId).get();
        if (role == null)
        throw new ApiValidationException("Role Id", "is-not-exist");
        return findById(role.getId(),RoleProjection.class);
    }

    public String getRole(String roleId){
        return getRoleById(roleId).getEnName();
    }

    @Override
    protected BaseRepository<Role> getRepository() {
        return roleRepository;
    }
}
