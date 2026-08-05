package com.carvajal.wishlist.dto.notification;

import java.util.List;

public record NotificationSummaryResponse(
        int sent,
        int totalPending,
        List<NotificationResponse> notifications) {
}
