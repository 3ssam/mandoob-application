package com.mandob.controller;

import com.mandob.base.Utils.PageRequestVM;
import com.mandob.projection.Invoice.InvoiceListProjection;
import com.mandob.response.InvoiceItem;
import com.mandob.service.InvoiceService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("invoice")
public class InvoiceController {
    private final InvoiceService invoiceService;


    @GetMapping()
    public Page<InvoiceListProjection> getAllInvoices(@RequestParam(required = false) String salesagentId
            , @RequestParam(required = false) String status
            , @RequestParam(required = false) String customerId
            , @RequestParam(defaultValue = "0") PageRequestVM pr) {
        return invoiceService.getInvoices(salesagentId, status, customerId, pr);
    }

    @GetMapping("customer/{customerId}")
    public Page<InvoiceListProjection> findAllInvoicesByCusomer(@PathVariable String customerId
            , @RequestParam(required = false) String status
            , @RequestParam(defaultValue = "0") PageRequestVM pr) {
        return invoiceService.getInvoiceByCustomer(customerId, status, pr);
    }

    @GetMapping("salesagentId/{salesagentId}")
    public Page<InvoiceListProjection> findAllInvoicesBySalesagent(@PathVariable String salesagentId
            , @RequestParam(required = false) String status
            , @RequestParam(required = false) String customerId
            , @RequestParam(defaultValue = "0") PageRequestVM pr) {
        return invoiceService.getInvoiceBySalesagent(salesagentId, status, customerId, pr);
    }

    @GetMapping("{invoiceId}")
    public InvoiceListProjection getInvoice(@PathVariable String invoiceId) {
        return invoiceService.getInvoice(invoiceId);
    }

    @GetMapping("report")
    public List<InvoiceItem> getInvoiceReport(@RequestParam(required = false) String invoiceId
            , @RequestParam(required = false) String salesagentId
            , @RequestParam(required = false) String status
            , @RequestParam(required = false) String customerId
            , @RequestParam(required = false) String from
            , @RequestParam(required = false) String to
            , @RequestParam(required = false) String payingType) {
        return invoiceService.getInvoiceReport(invoiceId, salesagentId, status, customerId, from, to, payingType);
    }

    @PostMapping("{invoiceId}")
    public InvoiceListProjection CashCollection(@PathVariable String invoiceId, @RequestParam String amount) {
        return invoiceService.cashCollection(invoiceId, amount);
    }


}
