package com.mandob.repository;

import com.mandob.domain.Customer;
import com.mandob.domain.Order;
import com.mandob.domain.Salesforce;
import com.mandob.projection.Order.OrderListProjection;
import com.mandob.projection.Order.OrderProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, String> {
    Page<OrderListProjection> findAllByCustomer(Customer customer, Pageable pageable);

    OrderProjection findAllById(String id);

    List<Order> findByCustomerAndCreatedAtBetween(Customer customer, Instant start, Instant end);

    List<Order> findByCreatedAtBetween(Instant start, Instant end);

    Page<OrderListProjection> findAllBySalesforce(Salesforce salesforce, Pageable pageable);

    Page<OrderListProjection> findAllBySalesforceAndCustomer(Salesforce salesforce, Customer customer, Pageable pageable);

    Page<OrderListProjection> findAllBySalesforceAndCreatedAtBetween(Salesforce salesforce, Instant start, Instant end, Pageable pageable);

    Page<OrderListProjection> findAllBySalesforceAndCustomerAndCreatedAtBetween(Salesforce salesforce, Customer customer, Instant start, Instant end, Pageable pageable);
}
