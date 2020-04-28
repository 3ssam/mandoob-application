package com.mandob.controller;

import com.mandob.base.Utils.ApiResponse;
import com.mandob.base.Utils.PageRequestVM;
import com.mandob.projection.Order.OrderListProjection;
import com.mandob.projection.Order.OrderProjection;
import com.mandob.request.OrderReq;
import com.mandob.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("customer/{customerId}")
    public Page<OrderListProjection> findAllOrders(@PathVariable String customerId, @RequestParam() PageRequestVM pr) {
        return orderService.getOrdersOfCustomer(customerId, pr);
    }

//    @GetMapping("customer/{customerId}")
//    public ApiPageResponse<OrderListProjection> findAllCustomers(PageRequestVM pr) {
//        return ApiPageResponse.of(orderService.findAll(OrderListProjection.class, pr));
//    }


    @GetMapping("{orderId}")
    public OrderProjection findAllOrders(@PathVariable String orderId) {
        return orderService.getOrder(orderId);
    }

    @PostMapping
    public ApiResponse<OrderProjection> createCustomer(@Valid @RequestBody OrderReq req) {
        return ApiResponse.created(orderService.CreateOrder(req));
    }


}
