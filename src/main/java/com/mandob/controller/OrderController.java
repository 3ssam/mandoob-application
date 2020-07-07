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
    public Page<OrderListProjection> findAllOrdersforCustomer(@PathVariable String customerId, @RequestParam() PageRequestVM pr) {
        return orderService.getOrdersOfCustomer(customerId, pr);
    }

    @GetMapping("salesforce/{salesforceId}")
    public Page<OrderListProjection> findAllOrdersforSalesforce(@PathVariable String salesforceId,
                                                                @RequestParam(required = false) String customerId,
                                                                @RequestParam(required = false) String date,
                                                                @RequestParam() PageRequestVM pr) {
        return orderService.getOrdersOfSalesforce(salesforceId, customerId, date, pr);
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
    public ApiResponse<OrderProjection> createOrder(@Valid @RequestBody OrderReq req) {
        return ApiResponse.created(orderService.CreateOrder(req));
    }

    @PostMapping("/isIncentive")
    public ApiResponse<Boolean> isNotIncentive(@RequestBody OrderReq req) {
        return ApiResponse.ok(orderService.isNotIncentive(req));
    }


    @GetMapping("/day")
    public int countOrdersOfDay() {
        return orderService.countOrdersOfDay();
    }


}
