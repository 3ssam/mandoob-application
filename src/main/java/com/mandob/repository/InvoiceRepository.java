package com.mandob.repository;

import com.mandob.domain.Customer;
import com.mandob.domain.Invoice;
import com.mandob.domain.Salesforce;
import com.mandob.domain.enums.InvoiceStatus;
import com.mandob.projection.Invoice.InvoiceListProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.Instant;
import java.util.List;

public interface InvoiceRepository extends PagingAndSortingRepository<Invoice, String> {

    InvoiceListProjection findAllById(String Id);

    Page<InvoiceListProjection> findAllBy(Pageable pageable);

    Page<InvoiceListProjection> findAllByCustomer(Customer customer, Pageable pageable);

    Page<InvoiceListProjection> findAllByCustomerAndStatus(Customer customer, InvoiceStatus status, Pageable pageable);

    List<InvoiceListProjection> findAllByCustomerAndCreatedAtBetween(Customer customer, Instant start, Instant end);

    Page<InvoiceListProjection> findAllBySalesforce(Salesforce salesforce, Pageable pageable);

    Page<InvoiceListProjection> findAllBySalesforceAndStatus(Salesforce salesforce, InvoiceStatus status, Pageable pageable);

    List<InvoiceListProjection> findAllBySalesforceAndCreatedAtBetween(Salesforce salesforce, Instant start, Instant end);

    Page<InvoiceListProjection> findAllBySalesforceAndCustomer(Salesforce salesforce, Customer customer, Pageable pageable);

    Page<InvoiceListProjection> findAllBySalesforceAndCustomerAndStatus(Salesforce salesforce, Customer customer, InvoiceStatus status, Pageable pageable);

    List<InvoiceListProjection> findAllBySalesforceAndCustomerAndCreatedAtBetween(Salesforce salesforce, Customer customer, Instant start, Instant end);

    Page<InvoiceListProjection> findAllByStatus(InvoiceStatus status, Pageable pageable);
}
