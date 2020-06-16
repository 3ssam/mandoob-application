package com.mandob.repository;

import com.mandob.base.repository.BaseRepository;
import com.mandob.domain.Customer;
import com.mandob.domain.Notifications;
import com.mandob.domain.Salesforce;
import com.mandob.domain.enums.NotificationsStatus;
import com.mandob.projection.Notification.NotificationListProjection;
import com.mandob.projection.Notification.NotificationProjection;

import java.util.List;

public interface NotificationRepository extends BaseRepository<Notifications> {
    List<NotificationListProjection> findAllByState(NotificationsStatus notificationsStatus);

    List<NotificationListProjection> findAllByCustomersAndState(Customer customer, NotificationsStatus notificationsStatus);

    List<NotificationListProjection> findAllBySalesforcesAndState(Salesforce salesforce, NotificationsStatus notificationsStatus);

    NotificationProjection getById(String id);
}
