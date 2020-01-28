package com.mandob.projection.schedulevisit;

import com.mandob.base.Projection.BaseProjection;
import com.mandob.base.Projection.LookupProjection;
import com.mandob.domain.enums.ScheduleVisitStatus;
import com.mandob.domain.enums.ScheduleVisitType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ScheduleVisitListProjection extends BaseProjection {
    LookupProjection getSalesforce();

    LookupProjection getCustomer();

    LocalDateTime getScheduleDate();

    ScheduleVisitType getVisitType();

    ScheduleVisitStatus getVisitStatus();

//    default String getInvoiceNumber() {
//        // TODO: return related invoice number
//        return "";
//    }
//
//    default BigDecimal getInvoiceAmount() {
//        // TODO: return related invoice amount
//        return BigDecimal.ZERO;
//    }
}
