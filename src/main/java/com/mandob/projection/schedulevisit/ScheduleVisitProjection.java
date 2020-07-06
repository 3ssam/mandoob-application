package com.mandob.projection.schedulevisit;

import com.mandob.base.Projection.AuditProjection;
import com.mandob.base.Projection.LookupProjection;
import com.mandob.domain.enums.ScheduleVisitStatus;
import com.mandob.domain.enums.ScheduleVisitType;

public interface ScheduleVisitProjection extends AuditProjection {
    LookupProjection getSalesforce();

    LookupProjection getCustomer();

    String getScheduleDate();

    String getMovementStatus();


    Boolean getPartialPayAllowed();

    ScheduleVisitType getVisitType();

    ScheduleVisitStatus getVisitStatus();

    String getLongitude();

    String getLatitude();

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
