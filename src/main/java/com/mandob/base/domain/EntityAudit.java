package com.mandob.base.domain;

//import com.mandoob.domain.Company;
//import com.mandoob.domain.User;
import com.mandob.domain.Company;
import com.mandob.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
public abstract class EntityAudit extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    protected User createdBy;

    @CreatedDate
    protected Instant createdAt;

    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", referencedColumnName = "id")
    protected User updatedBy;

    @LastModifiedDate
    protected Instant updatedAt;

//    @Version
//    private Integer version;

//    @PrePersist
//    public void updateCompany() {
//        if (company == null) {
//            company = CurrentUser.getCompanyRef();
//        }
//    }
}
