package com.mandob.service;

import com.mandob.base.exception.ApiValidationException;
import com.mandob.base.repository.BaseRepository;
import com.mandob.base.service.MasterService;
import com.mandob.domain.User;
import com.mandob.projection.User.UserProjection;
import com.mandob.repository.CompanyRepository;
import com.mandob.repository.UserRepository;
import com.mandob.request.UserReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserService extends MasterService<User> {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final CompanyRepository companyRepository;

    @Transactional
    public UserProjection create(UserReq req){
        User user =  new User();
        user.setCreatedAt(Instant.now());
        user.setCreatedBy(userRepository.getOne(req.getCurrentUser()));
        userRepository.save(createUser(req,user));
        userRepository.save(user);
        return findById(user.getId(), UserProjection.class);
    }




    private User createUser(UserReq req,User user){
        user.setUpdatedAt(Instant.now());
        user.setUpdatedBy(userRepository.getOne(req.getCurrentUser()));
        user.setCompany(companyRepository.findById(req.getCompany()).get());
        user.setRole(roleService.getId(req.getRole().toLowerCase()));
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword());
        user.setArName(req.getArName());
        user.setEnName(req.getEnName());
        user.setActivated(req.getActivated());
        user.setSuspended(req.getActivated());
        return user;
    }


    public void validateNewUserEmail(String email) {
        if (existsByEmail(email))
            throw new ApiValidationException("email", "already-exist");
    }

    public void validateUserNewEmail(String email, String id) {
        if (existsByEmailAndIdNot(email, id))
            throw new ApiValidationException("email", "already-exist");
    }

    private boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean existsByEmailAndIdNot(String email, String id) {
        return userRepository.existsByEmailAndIdNot(email, id);
    }

    @Override
    protected BaseRepository<User> getRepository() {
        return userRepository;
    }
}
