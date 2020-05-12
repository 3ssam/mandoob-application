package com.mandob.service;

import com.mandob.base.Utils.PageRequestVM;
import com.mandob.base.exception.ApiValidationException;
import com.mandob.base.repository.BaseRepository;
import com.mandob.base.service.AuditService;
import com.mandob.domain.Customer;
import com.mandob.domain.Invoice;
import com.mandob.domain.Salesforce;
import com.mandob.domain.enums.InvoiceStatus;
import com.mandob.domain.enums.PayingType;
import com.mandob.projection.Invoice.InvoiceListProjection;
import com.mandob.repository.CustomerRepository;
import com.mandob.repository.InvoiceRepository;
import com.mandob.repository.SalesforceRepository;
import com.mandob.response.InvoiceItem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService extends AuditService<Invoice> {
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
            list = getInvoiceByStatus(status, pr);
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

    public List<InvoiceItem> getInvoiceReport(String invoiceId
            , String salesagentId
            , String statusType
            , String customerId
            , String start
            , String end
            , String type) {
        Instant from = null;
        Instant to = null;
        Customer customer = null;
        Salesforce salesforce = null;
        InvoiceStatus status = null;
        PayingType payingType = null;
        Invoice invoice = null;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        if (start != null)
            from = LocalDateTime.parse(start, format).minusHours(2).toInstant(ZoneOffset.UTC);
        else
            from = LocalDateTime.now().minusYears(10L).toInstant(ZoneOffset.UTC);
        if (end != null)
            to = LocalDateTime.parse(end, format).minusHours(2).toInstant(ZoneOffset.UTC);
        else
            to = LocalDateTime.now().plusYears(20L).toInstant(ZoneOffset.UTC);
        if (salesagentId != null) {
            salesforce = salesforceRepository.getOne(salesagentId);
            if (salesforce == null)
                throw new ApiValidationException("salesforce Id", "salesforce-id-is-not-vaild");
        }
        if (customerId != null) {
            customer = customerRepository.getOne(customerId);
            if (customer == null)
                throw new ApiValidationException("Customer Id", "customer-id-is-not-vaild");
        }
        if (invoiceId != null) {
            invoice = invoiceRepository.findById(invoiceId).get();
            if (invoice == null)
                throw new ApiValidationException("Invoice Id", "Invoice-id-is-not-vaild");
        }
        if (type != null)
            payingType = PayingType.valueOf(type.toUpperCase());
        if (statusType != null)
            status = InvoiceStatus.valueOf(statusType.toUpperCase());
        //getReport(invoice, customer, salesforce, from, to, status, payingType);
        return getReport(invoice, customer, salesforce, from, to, status, payingType);
    }

    private List<InvoiceItem> getReport(Invoice invoice, Customer customer, Salesforce salesforce, Instant from,
                                        Instant to, InvoiceStatus status, PayingType payingType) {
        List<Invoice> list = new ArrayList<>();
        if (invoice != null)
            list.add(invoice);
        else if (status != null && payingType != null) {
            if (customer != null && salesforce != null)
                list = invoiceRepository.findAllBySalesforceAndCustomerAndStatusAndPayingTypeAndCreatedAtBetween(salesforce, customer, status, payingType, from, to);
            else if (customer != null)
                list = invoiceRepository.findAllByCustomerAndStatusAndPayingTypeAndCreatedAtBetween(customer, status, payingType, from, to);
            else if (salesforce != null)
                list = invoiceRepository.findAllBySalesforceAndStatusAndPayingTypeAndCreatedAtBetween(salesforce, status, payingType, from, to);
            else
                list = invoiceRepository.findAllByStatusAndPayingTypeAndCreatedAtBetween(status, payingType, from, to);
        } else if (status != null) {
            if (customer != null && salesforce != null)
                list = invoiceRepository.findAllBySalesforceAndCustomerAndStatusAndCreatedAtBetween(salesforce, customer, status, from, to);
            else if (customer != null)
                list = invoiceRepository.findAllByCustomerAndStatusAndCreatedAtBetween(customer, status, from, to);
            else if (salesforce != null)
                list = invoiceRepository.findAllBySalesforceAndStatusAndCreatedAtBetween(salesforce, status, from, to);
            else
                list = invoiceRepository.findAllByStatusAndCreatedAtBetween(status, from, to);
        } else if (payingType != null) {
            if (customer != null && salesforce != null)
                list = invoiceRepository.findAllBySalesforceAndCustomerAndPayingTypeAndCreatedAtBetween(salesforce, customer, payingType, from, to);
            else if (customer != null)
                list = invoiceRepository.findAllByCustomerAndPayingTypeAndCreatedAtBetween(customer, payingType, from, to);
            else if (salesforce != null)
                list = invoiceRepository.findAllBySalesforceAndPayingTypeAndCreatedAtBetween(salesforce, payingType, from, to);
            else
                list = invoiceRepository.findAllByPayingTypeAndCreatedAtBetween(payingType, from, to);
        } else {
            if (customer != null && salesforce != null)
                list = invoiceRepository.findAllBySalesforceAndCustomerAndCreatedAtBetween(salesforce, customer, from, to);
            else if (customer != null)
                list = invoiceRepository.findAllByCustomerAndCreatedAtBetween(customer, from, to);
            else if (salesforce != null)
                list = invoiceRepository.findAllBySalesforceAndCreatedAtBetween(salesforce, from, to);
            else
                list = invoiceRepository.findAllByCreatedAtBetween(from, to);
        }
        return mappingInvoiceResponse(list);
    }

    private List<InvoiceItem> mappingInvoiceResponse(List<Invoice> invoices) {
        List<InvoiceItem> items = new ArrayList<>();
        for (int i = 0; i < invoices.size(); i++) {
            InvoiceItem item = new InvoiceItem();
            item.setAmountCashCollection(String.valueOf(invoices.get(i).getAmountCashcollection()));
            item.setAmountPaid(String.valueOf(invoices.get(i).getAmountPaid()));
            item.setAmountRemain(String.valueOf(invoices.get(i).getAmountRemain()));
            item.setCustomerArabicName(invoices.get(i).getCustomer().getArName());
            item.setCustomerEnglishName(invoices.get(i).getCustomer().getEnName());
            item.setInstallmentNumber(String.valueOf(invoices.get(i).getInstallmentNumber()));
            item.setOrderId(invoices.get(i).getOrder().getId());
            item.setPayingType(invoices.get(i).getPayingType().toString());
            item.setStatus(invoices.get(i).getStatus().toString());
            item.setTotalOfOrder(String.valueOf(invoices.get(i).getTotalAmount()));
            item.setSalesAgentArabicName(invoices.get(i).getSalesforce().getArName());
            item.setSalesAgentEnglishName(invoices.get(i).getSalesforce().getEnName());
            item.setInvoiceDate(invoices.get(i).getCreatedAt().toString());
            items.add(item);
        }
        return items;
    }

    @Override
    protected BaseRepository<Invoice> getRepository() {
        return null;
    }
}
