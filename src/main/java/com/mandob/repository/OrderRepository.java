package com.mandob.repository;

import com.mandob.domain.Customer;
import com.mandob.domain.Order;
import com.mandob.projection.Order.OrderListProjection;
import com.mandob.projection.Order.OrderProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository extends PagingAndSortingRepository<Order, String> {
    Page<OrderListProjection> findAllByCustomer(Customer customer, Pageable pageable);

    OrderProjection findAllById(String id);
}
