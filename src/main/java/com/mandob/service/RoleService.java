package com.mandob.service;

import com.mandob.domain.Role;
import com.mandob.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleService {

    private RoleRepository roleRepository;

    public boolean isExist(String enName){
        return roleRepository.existsByEnName(enName);
    }

    public Role getId(String enName){
        List<Role> roles = roleRepository.findByEnName(enName);
        if (roles.size() == 0)
            return null;
           return roles.get(0);
    }

}
