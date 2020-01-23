package com.mandob.domain;

import com.mandob.domain.lookup.City;
import com.mandob.domain.lookup.Government;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "customer")
public class Customer extends User {
    @Column(nullable = false, length = 13)
    private String phoneNumber1;

    @Column(length = 13)
    private String phoneNumber2;

    @Column(nullable = false)
    private String licenseNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", referencedColumnName = "id")
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "government_id", referencedColumnName = "id")
    private Government government;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignedTo_id", referencedColumnName = "id")
    private Salesforce assignedTo;

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", arName='" + arName + '\'' +
                ", enName='" + enName + '\'' +
                ", email='" + getEmail() + '\'' +
                ", activated=" + getActivated() +
                ", suspended=" + getSuspended() +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                "}";
    }
}
