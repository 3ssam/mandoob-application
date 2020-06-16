package com.mandob.domain;

import com.mandob.base.domain.BaseEntity;
import com.mandob.domain.enums.NotificationsStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notifications extends BaseEntity {
    @Column(nullable = false)
    private String message;

    @Column(length = 13)
    private String title;

    @Column(nullable = false)
    private String scheduleDate;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Customer> customers;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Salesforce> salesforces;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationsStatus state;

    @Override
    public String toString() {
        return "Notifications{" +
                "message='" + message + '\'' +
                ", title='" + title + '\'' +
                ", scheduleDate='" + scheduleDate + '\'' +
                ", state=" + state +
                '}';
    }
}
