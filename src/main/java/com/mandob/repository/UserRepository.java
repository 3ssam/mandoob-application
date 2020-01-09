package com.mandob.repository;

import com.mandob.base.repository.MasterRepository;
import com.mandob.domain.User;

import java.util.Optional;

public interface UserRepository extends MasterRepository<User> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, String id);

    Optional<User> findByEmail(String email);
}