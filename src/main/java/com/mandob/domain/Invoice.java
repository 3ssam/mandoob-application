package com.mandob.domain;

import com.mandob.base.domain.EntityAudit;
import com.mandob.domain.enums.InvoiceStatus;
import com.mandob.domain.enums.PayingType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "invoice")
public class Invoice extends EntityAudit {

    @NotNull
    private double totalAmount;

    @NotNull
    private double amountPaid;

    @NotNull
    private double amountRemain;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salesforce_id", referencedColumnName = "id")
    private Salesforce salesforce;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayingType payingType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceStatus status;

    @NotNull
    private double amountCashcollection;

    private int InstallmentNumber;

    @NotNull
    private boolean Installment;

    public void CheckInvoice() {
        if (totalAmount == amountPaid)
            status = InvoiceStatus.CLOSED;
    }

}
