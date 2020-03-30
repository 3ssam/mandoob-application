package com.mandob.controller;

import com.mandob.projection.Notification.NotificationListProjection;
import com.mandob.projection.Notification.NotificationProjection;
import com.mandob.request.PushNotificationReq;
import com.mandob.response.PushNotificationResponse;
import com.mandob.service.PushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("notification")
public class PushNotificationController {

    @Autowired
    private PushNotificationService pushNotificationService;


    @PostMapping()
    public NotificationProjection sendNotification(@RequestBody PushNotificationReq request) {
        //pushNotificationService.sendPushNotificationWithoutData(request);
        return pushNotificationService.CreateNotification(request);
    }

    @PostMapping("/token")
    public ResponseEntity sendTokenNotification(@RequestBody PushNotificationReq request) {
        pushNotificationService.sendPushNotificationToToken(request);
        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }

    @PostMapping("/data")
    public ResponseEntity sendDataNotification(@RequestBody PushNotificationReq request) {
        pushNotificationService.sendPushNotification(request);
        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }

    @GetMapping()
    public List<NotificationListProjection> getNotifications(@RequestParam() String customerId) {
        //pushNotificationService.sendSamplePushNotification();
        return pushNotificationService.getNewNotifications(customerId);
    }

    @GetMapping("{notificationId}")
    public NotificationProjection getNotification(@PathVariable("notificationId") String notificationId) {
        return pushNotificationService.getNotification(notificationId);
    }

    @DeleteMapping("{notificationId}")
    public String deleteNotification(@PathVariable("notificationId") String notificationId,@RequestParam String customerId) {
        return pushNotificationService.deleteNotification(customerId,notificationId);
    }


}
