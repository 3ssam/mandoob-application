package com.mandob.projection.schedulevisit;

import com.mandob.base.Projection.BaseProjection;
import com.mandob.base.Projection.LookupProjection;
import com.mandob.domain.enums.ScheduleVisitStatus;
import com.mandob.domain.enums.ScheduleVisitType;

public interface ScheduleVisitListProjection extends BaseProjection {
    LookupProjection getSalesforce();

    LookupProjection getCustomer();

    String getScheduleDate();

    String getAddress();

    ScheduleVisitType getVisitType();

    ScheduleVisitStatus getVisitStatus();

    String getLongitude();

    String getLatitude();


    String getMovementStatus();


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
