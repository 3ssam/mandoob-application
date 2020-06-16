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
    public NotificationProjection sendNotification(@RequestBody PushNotificationReq request,
                                                   @RequestParam(required = false) String customerId,
                                                   @RequestParam(required = false) String salesagentId,
                                                   @RequestParam(defaultValue = "ALL") String userType) {
        //pushNotificationService.sendPushNotificationWithoutData(request);
        return pushNotificationService.CreateNotification(request, userType, customerId, salesagentId);
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
    public List<NotificationListProjection> getNotifications(@RequestParam(required = false) String customerId,
                                                             @RequestParam(required = false) String salesagentId
    ) {
        //pushNotificationService.sendSamplePushNotification();
        return pushNotificationService.getNewNotifications(customerId, salesagentId);
    }

    @GetMapping("{notificationId}")
    public NotificationProjection getNotification(@PathVariable("notificationId") String notificationId) {
        return pushNotificationService.getNotification(notificationId);
    }

    @DeleteMapping("{notificationId}")
    public String deleteNotification(@PathVariable("notificationId") String notificationId,
                                     @RequestParam(required = false) String customerId,
                                     @RequestParam(required = false) String salesagentId) {
        return pushNotificationService.deleteNotification(notificationId, customerId, salesagentId);
    }


}
