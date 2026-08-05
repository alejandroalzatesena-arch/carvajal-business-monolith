package com.carvajal.wishlist.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carvajal.wishlist.dto.notification.NotificationResponse;
import com.carvajal.wishlist.dto.notification.NotificationSummaryResponse;
import com.carvajal.wishlist.service.NotificationService;

@RestController
@RequestMapping("/api/users/{userId}/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<NotificationResponse> listPending(@PathVariable Long userId) {
        return notificationService.listPending(userId);
    }

    @PostMapping("/send")
    public NotificationSummaryResponse send(@PathVariable Long userId) {
        return notificationService.send(userId);
    }
}
