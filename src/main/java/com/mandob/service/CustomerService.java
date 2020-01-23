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

import java.time.Instant;

@Service
@AllArgsConstructor
public class CustomerService extends MasterService<Customer> {
    private final UserService userService;
    private final RouteService routeService;
    //private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;
    private final RoleService roleService;

    @Transactional
    public CustomerProjection create(CustomerReq req) {
        userService.validateNewUserEmail(req.getEmail());
        Customer customer = new Customer();
        customer.setCreatedBy(userService.findById(req.getCurrentUser()));
        customer.setCreatedAt(Instant.now());
        customer = createNewCustomer(req,customer);
        userService.createNewUserData(customer);
        customerRepository.save(customer);
        return findById(customer.getId(), CustomerProjection.class);
    }

    @Transactional
    public CustomerProjection update(String id, CustomerReq req) {
        Customer customer = findById(id);
        customer = createNewCustomer(req,customer);
        userService.validateUserNewEmail(req.getEmail(), id);
        customerRepository.save(customer);
        return findById(customer.getId(), CustomerProjection.class);
    }

    public Customer createNewCustomer(CustomerReq req,Customer customer){
        customer.setLicenseNo(req.getLicenseNo());
        customer.setPhoneNumber1(req.getPhoneNumber1());
        customer.setPhoneNumber2(req.getPhoneNumber2());
        customer.setActivated(true);
        customer.setSuspended(false);
        customer.setArName(req.getArName());
        customer.setEnName(req.getEnName());
        customer.setUpdatedAt(Instant.now());
        customer.setUpdatedBy(userService.findById(req.getCurrentUser()));
        customer.setCompany(customer.getCreatedBy().getCompany());
        customer.setEmail(req.getEmail());
        customer.setPassword(req.getPassword());
        customer.setRole(roleService.getId("customer"));
        Route route = routeService.findById(req.getRoute());
        customer.setAssignedTo(route.getSalesforce());
        customer.setGovernment(route.getGovernment());
        customer.setCity(route.getCity());
        return customer;
    }

    @Override
    protected BaseRepository<Customer> getRepository() {
        return customerRepository;
    }
}
