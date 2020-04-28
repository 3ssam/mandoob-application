package com.mandob.domain;

import com.mandob.base.domain.EntityAudit;
import com.mandob.domain.enums.ScheduleVisitStatus;
import com.mandob.domain.enums.ScheduleVisitType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "schedule_visit")
public class ScheduleVisit extends EntityAudit {

    @Column(nullable = false)
    private String longitude;

    @Column(nullable = false)
    private String latitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salesforce_id", referencedColumnName = "id", nullable = false)
    private Salesforce salesforce;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private String scheduleDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleVisitType visitType;

    @Column(nullable = false)
    private Boolean partialPayAllowed;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
//    private Invoice invoice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleVisitStatus visitStatus;

    @Override
    public String toString() {
        return "ScheduleVisit{" +
                "id='" + id + '\'' +
                ", scheduleDate=" + scheduleDate +
                ", visitType=" + visitType +
                ", partialPayAllowed=" + partialPayAllowed +
                ", visitStatus=" + visitStatus +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                "}";
    }
}
