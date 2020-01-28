package com.mandob.domain;

import com.mandob.base.domain.BaseEntity;
import com.mandob.domain.enums.MovementStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(schema = "salesforce_movement")
public class SalesforceMovement extends BaseEntity {
    @Column(nullable = false)
    private String longitude;

    @Column(nullable = false)
    private String latitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salesforce_id", referencedColumnName = "id")
    private Salesforce salesforce;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovementStatus status;

    @PrePersist
    private void updateDateTime() {
        this.dateTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "SalesforceMovement{" +
                "longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", salesforce=" + salesforce +
                ", customer=" + customer +
                ", dateTime=" + dateTime +
                ", status=" + status +
                '}';
    }
}
