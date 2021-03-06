package com.mandob.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mandob.domain.enums.SalesforceRole;
import com.mandob.domain.lookup.City;
import com.mandob.domain.lookup.Government;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "salesforce")
public class Salesforce extends User {
    private String employeeCode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    //TODO Search @Enumerated
    private SalesforceRole salesforceRole;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to", referencedColumnName = "id")
    private Salesforce assignedTo;

    @Column(nullable = false, length = 13)
    private String phoneNumber;

    @Column(length = 14)
    private String nationalId;

    private String detailedAddress;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "government_id", referencedColumnName = "id")
    private Government government;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "salesforce")
    private Set<SalesforceMovement> movements = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "salesforce")
    private List<Customer> customers = new ArrayList<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "salesforce")
    private List<ScheduleVisit> scheduleVisits = new ArrayList<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "salesforce")
    private List<Invoice> invoices = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "salesforces")
    private List<Notifications> notifications = new ArrayList<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "salesforce")
    private List<Order> orders;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "salesforce")
    private List<Stock> stocks;


    @Override
    public String toString() {
        return "Salesforce{" +
                "id='" + id + '\'' +
                ", arName='" + arName + '\'' +
                ", enName='" + enName + '\'' +
                ", email='" + getEmail() + '\'' +
                ", activated=" + getActivated() +
                ", suspended=" + getSuspended() +
                ", employeeCode='" + employeeCode + '\'' +
                ", salesforceRole=" + salesforceRole +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", nationalId='" + nationalId + '\'' +
                ", detailedAddress='" + detailedAddress + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                "}";
    }
}
