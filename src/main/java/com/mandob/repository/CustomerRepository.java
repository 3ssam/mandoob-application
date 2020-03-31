package com.mandob.repository;

import com.mandob.base.repository.MasterRepository;
import com.mandob.domain.Customer;

import java.util.List;

public interface CustomerRepository extends MasterRepository<Customer> {
    List<Customer> findAll();
}
