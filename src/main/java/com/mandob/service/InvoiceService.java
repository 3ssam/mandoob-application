package com.mandob.service;

import com.mandob.base.Utils.PageRequestVM;
import com.mandob.base.exception.ApiValidationException;
import com.mandob.base.repository.BaseRepository;
import com.mandob.base.service.AuditService;
import com.mandob.domain.Customer;
import com.mandob.domain.Invoice;
import com.mandob.domain.Salesforce;
import com.mandob.domain.enums.InvoiceStatus;
import com.mandob.projection.Invoice.InvoiceListProjection;
import com.mandob.repository.CustomerRepository;
import com.mandob.repository.InvoiceRepository;
import com.mandob.repository.OrderRepository;
import com.mandob.repository.SalesforceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvoiceService extends AuditService<Invoice> {
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    private final SalesforceRepository salesforceRepository;


    public Page<InvoiceListProjection> getInvoices(String salesagentId, String status, String customerId, PageRequestVM pr) {
        Page<InvoiceListProjection> list = null;
        if (salesagentId != null)
            list = getInvoiceBySalesagent(salesagentId, status, customerId, pr);
        else if (customerId != null)
            list = getInvoiceByCustomer(customerId, status, pr);
        else
            getInvoiceByStatus(status, pr);
        return list;
    }


    public Page<InvoiceListProjection> getInvoiceByCustomer(String customerId, String status, PageRequestVM pr) {
        Customer customer = customerRepository.getOne(customerId);
        if (customer == null)
            throw new ApiValidationException("Customer Id", "customer-id-is-not-vaild");
        Page<InvoiceListProjection> list = null;
        if (status == null)
            list = invoiceRepository.findAllByCustomer(customer, pr.build());
        else if (status.equalsIgnoreCase("OPEN"))
            list = invoiceRepository.findAllByCustomerAndStatus(customer, InvoiceStatus.OPEN, pr.build());
        else if (status.equalsIgnoreCase("CLOSED"))
            list = invoiceRepository.findAllByCustomerAndStatus(customer, InvoiceStatus.CLOSED, pr.build());
        return list;
    }

    public Page<InvoiceListProjection> getInvoiceBySalesagent(String salesagentId, String status, String customerId, PageRequestVM pr) {
        Salesforce salesforce = salesforceRepository.getOne(salesagentId);
        Customer customer = null;
        if (salesforce == null)
            throw new ApiValidationException("salesforce Id", "salesforce-id-is-not-vaild");
        if (customerId != null) {
            customer = customerRepository.getOne(customerId);
            if (customer == null)
                throw new ApiValidationException("Customer Id", "customer-id-is-not-vaild");
        }
        Page<InvoiceListProjection> list = null;
        if (status == null) {
            if (customer != null)
                list = invoiceRepository.findAllBySalesforceAndCustomer(salesforce, customer, pr.build());
            else
                list = invoiceRepository.findAllBySalesforce(salesforce, pr.build());
        } else if (status.equalsIgnoreCase("OPEN")) {
            if (customer != null)
                list = invoiceRepository.findAllBySalesforceAndCustomerAndStatus(salesforce, customer, InvoiceStatus.OPEN, pr.build());
            else
                list = invoiceRepository.findAllBySalesforceAndStatus(salesforce, InvoiceStatus.OPEN, pr.build());
        } else if (status.equalsIgnoreCase("CLOSED")) {
            if (customer != null)
                list = invoiceRepository.findAllBySalesforceAndCustomerAndStatus(salesforce, customer, InvoiceStatus.CLOSED, pr.build());
            else
                list = invoiceRepository.findAllBySalesforceAndStatus(salesforce, InvoiceStatus.CLOSED, pr.build());
        }
        return list;
    }

    public Page<InvoiceListProjection> getInvoiceByStatus(String status, PageRequestVM pr) {
        Page<InvoiceListProjection> list = null;
        if (status == null)
            list = invoiceRepository.findAllBy(pr.build());
        else if (status.equalsIgnoreCase("OPEN"))
            list = invoiceRepository.findAllByStatus(InvoiceStatus.OPEN, pr.build());
        else if (status.equalsIgnoreCase("CLOSED"))
            list = invoiceRepository.findAllByStatus(InvoiceStatus.CLOSED, pr.build());
        return list;
    }

    public InvoiceListProjection getInvoice(String invoiceId) {
        InvoiceListProjection invoice = invoiceRepository.findAllById(invoiceId);
        if (invoice == null)
            throw new ApiValidationException("Invoice Id", "Invoice-id-is-not-vaild");
        return invoice;
    }

    @Override
    protected BaseRepository<Invoice> getRepository() {
        return null;
    }
}
