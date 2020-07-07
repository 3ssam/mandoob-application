package com.mandob.repository;

import com.mandob.domain.Customer;
import com.mandob.domain.Invoice;
import com.mandob.domain.Salesforce;
import com.mandob.domain.enums.InvoiceStatus;
import com.mandob.domain.enums.PayingType;
import com.mandob.projection.Invoice.InvoiceListProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface InvoiceRepository extends PagingAndSortingRepository<Invoice, String> {

    InvoiceListProjection findAllById(String Id);

    Page<InvoiceListProjection> findAllBy(Pageable pageable);

    Page<InvoiceListProjection> findAllByCustomer(Customer customer, Pageable pageable);

    Page<InvoiceListProjection> findAllByCustomerAndStatus(Customer customer, InvoiceStatus status, Pageable pageable);

    Page<InvoiceListProjection> findAllBySalesforce(Salesforce salesforce, Pageable pageable);

    Page<InvoiceListProjection> findAllBySalesforceAndStatus(Salesforce salesforce, InvoiceStatus status, Pageable pageable);

    Page<InvoiceListProjection> findAllBySalesforceAndCustomer(Salesforce salesforce, Customer customer, Pageable pageable);

    Page<InvoiceListProjection> findAllBySalesforceAndCustomerAndStatus(Salesforce salesforce, Customer customer, InvoiceStatus status, Pageable pageable);

    Page<InvoiceListProjection> findAllByStatus(InvoiceStatus status, Pageable pageable);


    Page<InvoiceListProjection> findBySalesforceAndCreatedAtBetween(Salesforce salesforce, Instant start, Instant end, Pageable pageable);

    Page<InvoiceListProjection> findBySalesforceAndCustomerAndCreatedAtBetween(Salesforce salesforce, Customer customer, Instant start, Instant end, Pageable pageable);

    Page<InvoiceListProjection> findBySalesforceAndStatusAndCreatedAtBetween(Salesforce salesforce, InvoiceStatus status, Instant start, Instant end, Pageable pageable);

    Page<InvoiceListProjection> findBySalesforceAndCustomerAndStatusAndCreatedAtBetween(Salesforce salesforce, Customer customer, InvoiceStatus status, Instant start, Instant end, Pageable pageable);


    List<Invoice> findAllBySalesforceAndCustomerAndCreatedAtBetween(Salesforce salesforce, Customer customer, Instant start, Instant end);

    List<Invoice> findAllBySalesforceAndCustomerAndPayingTypeAndCreatedAtBetween(Salesforce salesforce, Customer customer, PayingType payingType, Instant start, Instant end);

    List<Invoice> findAllBySalesforceAndCustomerAndStatusAndCreatedAtBetween(Salesforce salesforce, Customer customer, InvoiceStatus status, Instant start, Instant end);

    List<Invoice> findAllBySalesforceAndCustomerAndStatusAndPayingTypeAndCreatedAtBetween(Salesforce salesforce, Customer customer, InvoiceStatus status, PayingType payingType, Instant start, Instant end);

    List<Invoice> findAllBySalesforceAndCreatedAtBetween(Salesforce salesforce, Instant start, Instant end);

    List<Invoice> findAllBySalesforceAndPayingTypeAndCreatedAtBetween(Salesforce salesforce, PayingType payingType, Instant start, Instant end);

    List<Invoice> findAllBySalesforceAndStatusAndCreatedAtBetween(Salesforce salesforce, InvoiceStatus status, Instant start, Instant end);

    List<Invoice> findAllBySalesforceAndStatusAndPayingTypeAndCreatedAtBetween(Salesforce salesforce, InvoiceStatus status, PayingType payingType, Instant start, Instant end);

    List<Invoice> findAllByCustomerAndCreatedAtBetween(Customer customer, Instant start, Instant end);

    List<Invoice> findAllByCustomerAndPayingTypeAndCreatedAtBetween(Customer customer, PayingType payingType, Instant start, Instant end);

    List<Invoice> findAllByCustomerAndStatusAndCreatedAtBetween(Customer customer, InvoiceStatus status, Instant start, Instant end);

    List<Invoice> findAllByCustomerAndStatusAndPayingTypeAndCreatedAtBetween(Customer customer, InvoiceStatus status, PayingType payingType, Instant start, Instant end);

    List<Invoice> findAllByStatusAndCreatedAtBetween(InvoiceStatus status, Instant start, Instant end);

    List<Invoice> findAllByPayingTypeAndCreatedAtBetween(PayingType payingType, Instant start, Instant end);

    List<Invoice> findAllByStatusAndPayingTypeAndCreatedAtBetween(InvoiceStatus status, PayingType payingType, Instant start, Instant end);

    List<Invoice> findAllByCreatedAtBetween(Instant start, Instant end);

}
