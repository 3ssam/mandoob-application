package com.mandob.controller;

import com.mandob.base.Projection.LookupProjection;
import com.mandob.base.Utils.ApiPageResponse;
import com.mandob.base.Utils.ApiResponse;
import com.mandob.base.Utils.PageRequestVM;
import com.mandob.projection.Customer.CustomerListProjection;
import com.mandob.projection.Customer.CustomerProjection;
import com.mandob.request.CustomerReq;
import com.mandob.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("customers")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public ApiPageResponse<CustomerListProjection> findAllCustomers(PageRequestVM pr) {
        return ApiPageResponse.of(customerService.findAll(CustomerListProjection.class, pr));
    }

    @GetMapping("{customerId}")
    public ApiResponse<CustomerProjection> findCustomerById(@PathVariable String customerId) {
        return ApiResponse.ok(customerService.findById(customerId, CustomerProjection.class));
    }

    @PostMapping
    public ApiResponse<CustomerProjection> createCustomer(@Valid @RequestBody CustomerReq req) {
        return ApiResponse.created(customerService.create(req));
    }

    @PutMapping("{customerId}")
    public ApiResponse<CustomerProjection> updateCustomer(@PathVariable String customerId, @Valid @RequestBody CustomerReq req) {
        return ApiResponse.updated(customerService.update(customerId, req));
    }

    @GetMapping("lookup")
    public List<LookupProjection> lookup(String customerId) {
        return customerService.lookup(customerId);
    }

}
