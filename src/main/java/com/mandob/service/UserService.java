package com.mandob.service;

import com.mandob.domain.User;
import com.mandob.repository.UserRepository;
import com.mandob.request.UserReq;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    @Transactional
    public User create(UserReq req){
        User user =  new User();
        user.setActivated(req.getActivated());
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword());
        user.setRole(roleService.getId(req.getRole().toLowerCase()));
        user.setSuspended(req.getActivated());
        user.setArName(req.getArName());
        user.setEnName(req.getEnName());
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        user.setCompany(null);
//        user.setCreatedBy(userRepository.getOne(req.getCurrentUser()));
//        user.setUpdatedBy(userRepository.getOne(req.getCurrentUser()));
        userRepository.save(user);
        return user;
    }


    public  Page<User> findAllUsers(String page){
        int pageNumber = StringUtils.isNumeric(page) ? Integer.parseInt(page) : 0;
        PageRequest pageRequest = PageRequest.of(Math.max(0, --pageNumber), 20);

        return null;
    }

}
