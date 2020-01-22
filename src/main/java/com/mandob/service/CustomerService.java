package com.mandob.service;

import com.mandob.base.repository.BaseRepository;
import com.mandob.base.service.MasterService;
import com.mandob.domain.Customer;
import com.mandob.domain.Route;
import com.mandob.projection.Customer.CustomerProjection;
import com.mandob.repository.CustomerRepository;
import com.mandob.request.CustomerReq;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CustomerService extends MasterService<Customer> {
    private final UserService userService;
    //private final RouteService routeService;
    //private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;

    @Transactional
    public CustomerProjection create(CustomerReq req) {
        userService.validateNewUserEmail(req.getEmail());
        Customer customer = null;//customerMapper.toEntity(req);
        //Route route = routeService.findById(req.getRoute());
//        customer.setAssignedTo(route.getSalesforce());
//        customer.setGovernment(route.getGovernment());
//        customer.setCity(route.getCity());
        //userService.createNewUserData(customer);
        customerRepository.save(customer);
        //userService.generateNewPassword(customer);
        return findById(customer.getId(), CustomerProjection.class);
    }

    @Transactional
    public CustomerProjection update(String id, CustomerReq req) {
        Customer customer = findById(id);
        //customerMapper.toEntity(req, customer);
//        Route route = routeService.findById(req.getRoute());
//        customer.setAssignedTo(route.getSalesforce());
//        customer.setGovernment(route.getGovernment());
//        customer.setCity(route.getCity());
//        userService.validateUserNewEmail(req.getEmail(), id);
        customerRepository.save(customer);
        return findById(customer.getId(), CustomerProjection.class);
    }

    @Override
    protected BaseRepository<Customer> getRepository() {
        return customerRepository;
    }
}
