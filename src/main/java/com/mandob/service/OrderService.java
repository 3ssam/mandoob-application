package com.mandob.service;

import com.mandob.base.Utils.PageRequestVM;
import com.mandob.base.exception.ApiValidationException;
import com.mandob.base.repository.BaseRepository;
import com.mandob.base.service.AuditService;
import com.mandob.domain.*;
import com.mandob.domain.enums.InvoiceStatus;
import com.mandob.domain.enums.OrderStatus;
import com.mandob.domain.enums.PayingType;
import com.mandob.projection.Order.OrderListProjection;
import com.mandob.projection.Order.OrderProjection;
import com.mandob.repository.*;
import com.mandob.request.OrderReq;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService extends AuditService<Order> {
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final InvoiceRepository invoiceRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final SalesforceRepository salesforceRepository;
    private final UserService userService;

    public Page<OrderListProjection> getOrdersOfCustomer(String customerId, PageRequestVM pr) {
        Customer customer = customerRepository.getOne(customerId);
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
        Customer customer = customerRepository.getOne(req.getCurrentUser());
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
        order.setSalesforce(customer.getSalesforce());
        order.setProducts(products);
        order.setOrderAmount(amounts);
        order.calculateTotalPrice();
        order.setStatus(OrderStatus.NEW);
        order.setPayment(req.getPayingType().toString());
        order.setOrderDate(LocalDateTime.now().toString());
        orderRepository.save(order);
        createInvoice(order, req);
        return orderRepository.findAllById(order.getId());
    }

    @Transactional
    protected void createInvoice(Order order, OrderReq req) {
        Customer customer = order.getCustomer();
        Invoice invoice = new Invoice();
        invoice.setAmountPaid(0);
        invoice.setTotalAmount(order.getTotalOrder());
        invoice.setAmountRemain(order.getTotalOrder());
        invoice.setStatus(InvoiceStatus.OPEN);
        invoice.setPayingType(req.getPayingType());
        if (req.getPayingType().equals(PayingType.INCENTIVE)) {
            if (customer.getBalance() >= invoice.getTotalAmount()) {
                customer.setBalance(customer.getBalance() - invoice.getTotalAmount());
                invoice.setAmountRemain(0);
                invoice.setAmountPaid(invoice.getTotalAmount());
                invoice.setStatus(InvoiceStatus.CLOSED);
                invoice.setAmountCashcollection(0);
                customerRepository.save(customer);
            } else
                throw new ApiValidationException("Payment Type", "In suffecient balance");
        }
        invoice.setInstallment(req.isInstallment());
        invoice.setUpdatedAt(order.getUpdatedAt());
        invoice.setCreatedAt(order.getCreatedAt());
        invoice.setCreatedBy(order.getCreatedBy());
        invoice.setUpdatedBy(order.getCreatedBy());
        invoice.setCompany(order.getCompany());
        if (invoice.isInstallment())
            invoice.setInstallmentNumber(Integer.parseInt(req.getInstallmentNumber()));
        invoice.setOrder(order);
        invoice.setCustomer(customer);
        invoice.setSalesforce(order.getCustomer().getSalesforce());
        if (invoice.getPayingType().equals(PayingType.CASH)) {
            invoice.setAmountCashcollection(invoice.getTotalAmount());
        }
        if (invoice.getPayingType().equals(PayingType.INSTALLMENT))
            invoice.setAmountCashcollection(Double.parseDouble(req.getAmountPaid()));
        invoiceRepository.save(invoice);
    }

    public int countOrdersOfDay() {
        Instant start = LocalDateTime.now().withHour(0).withMinute(0).toInstant(ZoneOffset.UTC);
        Instant end = LocalDateTime.now().withHour(23).withMinute(59).toInstant(ZoneOffset.UTC);
        List<Order> orders = orderRepository.findByCreatedAtBetween(start, end);
        return orders.size();
    }

    public Boolean isNotIncentive(OrderReq req) {
        Order order = new Order();
        List<Product> products = new ArrayList<>();
        List<Integer> amounts = new ArrayList<>();
        Customer customer = customerRepository.findById(req.getCurrentUser()).get();
        if (customer == null)
            throw new ApiValidationException("Customer Id", "customer-id-is-not-vaild");

        for (int i = 0; i < req.getAmounts().size(); i++) {
            Product product = productRepository.getOne(req.getProducts().get(i));
            if (product != null && product.getRemainingAmount() >= req.getAmounts().get(i)) {
                product.setRemainingAmount(product.getRemainingAmount() - req.getAmounts().get(i));
                product.setAmount(product.getRemainingAmount());
                products.add(product);
                amounts.add(req.getAmounts().get(i));
            }
        }
        order.setProducts(products);
        order.setOrderAmount(amounts);
        order.calculateTotalPrice();
        if (customer.getBalance() >= order.getTotalOrder())
            return false;
        return true;
    }


    public Page<OrderListProjection> getOrdersOfSalesforce(String salesforceId,
                                                           String customerId, String date, PageRequestVM pr) {
        Salesforce salesforce = salesforceRepository.getOne(salesforceId);
        if (salesforce == null)
            throw new ApiValidationException("Salesforce Id", "Salesforce-id-is-not-vaild");
        Customer customer = null;
        if (customerId != null) {
            customer = customerRepository.getOne(customerId);
            if (customer == null)
                throw new ApiValidationException("Customer Id", "customer-id-is-not-vaild");
        }
        Instant start = null;
        Instant end = null;
        if (date != null) {
            LocalDate localDate = LocalDate.parse(date);
            start = localDate.atTime(0, 0, 0).toInstant(ZoneOffset.UTC);
            end = localDate.atTime(23, 59, 59).toInstant(ZoneOffset.UTC);
        }
        PageRequest request = pr.buildWithPage(pr.getPage());
        if (customerId != null && date != null)
            return orderRepository.findAllBySalesforceAndCustomerAndCreatedAtBetween(salesforce, customer, start, end, request);
        else if (customerId != null)
            return orderRepository.findAllBySalesforceAndCustomer(salesforce, customer, request);
        else if (date != null)
            return orderRepository.findAllBySalesforceAndCreatedAtBetween(salesforce, start, end, request);
        else
            return orderRepository.findAllBySalesforce(salesforce, request);
    }


    @Override
    protected BaseRepository<Order> getRepository() {
        return null;
    }


}
