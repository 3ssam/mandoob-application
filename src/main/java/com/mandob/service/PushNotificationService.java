package com.mandob.service;

import com.mandob.base.exception.ApiValidationException;
import com.mandob.domain.Customer;
import com.mandob.domain.Notifications;
import com.mandob.domain.enums.NotificationsStatus;
import com.mandob.firebase.FCMService;
import com.mandob.projection.Notification.NotificationListProjection;
import com.mandob.projection.Notification.NotificationProjection;
import com.mandob.repository.NotificationRepository;
import com.mandob.request.PushNotificationReq;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class PushNotificationService {

    @Value("#{${app.notifications.defaults}}")
    private Map<String, String> defaults;

    private final CustomerService customerService;

    private final NotificationRepository notificationRepository;

    private Logger logger = LoggerFactory.getLogger(PushNotificationService.class);

    private final FCMService fcmService;

    @Scheduled(initialDelay = 60000, fixedDelay = 60000)
    public void sendSamplePushNotification() {
        try {
            fcmService.sendMessageWithoutData(getSamplePushNotificationRequest());
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
    }

    public void sendPushNotification(PushNotificationReq request) {
        try {
            fcmService.sendMessage(getSamplePayloadData(), request);
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
    }

    public void sendPushNotificationWithoutData(PushNotificationReq request) {
        try {
            fcmService.sendMessageWithoutData(request);
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
    }


    public void sendPushNotificationToToken(PushNotificationReq request) {
        try {
            fcmService.sendMessageToToken(request);
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage());
        }
    }


    private Map<String, String> getSamplePayloadData() {
        Map<String, String> pushData = new HashMap<>();
        pushData.put("messageId", defaults.get("payloadMessageId"));
        pushData.put("text", defaults.get("payloadData") + " " + LocalDateTime.now());
        return pushData;
    }


    private PushNotificationReq getSamplePushNotificationRequest() {
        PushNotificationReq request = new PushNotificationReq(defaults.get("title"),
                defaults.get("message"),
                defaults.get("topic"));
        return request;
    }


    public List<NotificationListProjection> getNewNotifications(String customerId){
        Customer customer = customerService.findById(customerId);
        if (customer == null)
            throw new ApiValidationException("Customer Id", "not-exist");
        return notificationRepository.findAllByCustomersAndState(customer, NotificationsStatus.NEW);
    }

    @Transactional
    public NotificationProjection getNotification(String id){
        NotificationProjection notification = notificationRepository.getById(id);
        if (notification == null)
            throw new ApiValidationException("Notification Id", "not-exist");
        return notification;
    }

    @Transactional
    public NotificationProjection CreateNotification(PushNotificationReq req){
        Notifications notification = new Notifications();
        notification.setState(NotificationsStatus.NEW);
        notification.setMessage(req.getMessage());
        notification.setTitle(req.getTitle());
        notification.setScheduleDate(LocalDateTime.now().toString());
        notification.setCustomers(customerService.getAllCustomers());
        notificationRepository.save(notification);
        NotificationProjection notificationObject = notificationRepository.getById(notification.getId());
        return notificationObject;
    }

    @Transactional
    public String deleteNotification(String customerId,String id){
        Customer customer = customerService.findById(customerId);
        if (customer == null)
            throw new ApiValidationException("Customer Id", "not-exist");
        Notifications notification = notificationRepository.getOne(id);
        if (notification == null)
            throw new ApiValidationException("Notification Id", "not-exist");
        if (notification.getCustomers().remove(customer)) {
            notificationRepository.save(notification);
            return "Success Process";
        }
        else
            return "it is not deleted yet";
    }

}
