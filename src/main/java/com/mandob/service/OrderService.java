package com.mandob.service;

import com.mandob.base.Utils.PageRequestVM;
import com.mandob.base.exception.ApiValidationException;
import com.mandob.base.repository.BaseRepository;
import com.mandob.base.service.AuditService;
import com.mandob.domain.Customer;
import com.mandob.domain.Order;
import com.mandob.domain.Product;
import com.mandob.projection.Order.OrderListProjection;
import com.mandob.projection.Order.OrderProjection;
import com.mandob.repository.OrderRepository;
import com.mandob.repository.ProductRepository;
import com.mandob.request.OrderReq;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService extends AuditService<Order> {
    @Autowired
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerService customerService;
    private final UserService userService;

    public Page<OrderListProjection> getOrdersOfCustomer(String customerId, PageRequestVM pr) {
        Customer customer = customerService.findById(customerId);
        if (customer == null)
            throw new ApiValidationException("Customer Id", "customer-id-is-not-vaild");
        PageRequest request = pr.buildWithPage(pr.getPage());
        Page<OrderListProjection> orders = orderRepository.findAllByCustomer(customer, request);
        return orders;
    }

    public OrderProjection getOrder(String orderId) {
        OrderProjection order = orderRepository.findAllById(orderId);
        if (order == null)
            throw new ApiValidationException("Order Id", "Order-id-is-not-vaild");
        return order;
    }

    @Transactional
    public OrderProjection CreateOrder(OrderReq req) {
        Order order = new Order();
        List<Product> products = new ArrayList<>();
        List<Integer> amounts = new ArrayList<>();
        Customer customer = customerService.findById(req.getCurrentUser());
        if (customer == null)
            throw new ApiValidationException("Customer Id", "customer-id-is-not-vaild");

        for (int i = 0; i < req.getAmounts().size(); i++) {
            Product product = productRepository.getOne(req.getProducts().get(i));
            if (product != null && product.getRemainingAmount() >= req.getAmounts().get(i)) {
                product.setRemainingAmount(product.getRemainingAmount() - req.getAmounts().get(i));
                product.setAmount(product.getRemainingAmount());
                productRepository.save(product);
                products.add(product);
                amounts.add(req.getAmounts().get(i));
            }
        }
        order.setUpdatedAt(Instant.now());
        order.setCreatedAt(Instant.now());
        order.setCreatedBy(userService.findById(req.getCurrentUser()));
        order.setUpdatedBy(order.getCreatedBy());

        order.setCompany(customer.getCreatedBy().getCompany());

        order.setCustomer(customer);
        order.setProducts(products);
        order.setOrderAmount(amounts);
        order.calculateTotalPrice();
        orderRepository.save(order);
        return findById(order.getId(), OrderProjection.class);
    }

    @Override
    protected BaseRepository<Order> getRepository() {
        return null;
    }
}
