package com.carvajal.wishlist.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carvajal.wishlist.dto.notification.NotificationResponse;
import com.carvajal.wishlist.dto.notification.NotificationSummaryResponse;
import com.carvajal.wishlist.entity.User;
import com.carvajal.wishlist.entity.WishlistHistory;
import com.carvajal.wishlist.entity.WishlistItem;
import com.carvajal.wishlist.entity.enums.ActionType;
import com.carvajal.wishlist.repository.WishlistHistoryRepository;
import com.carvajal.wishlist.repository.WishlistItemRepository;

@Service
public class NotificationService {

    private final WishlistItemRepository wishlistItemRepository;
    private final WishlistHistoryRepository wishlistHistoryRepository;

    public NotificationService(WishlistItemRepository wishlistItemRepository,
                               WishlistHistoryRepository wishlistHistoryRepository) {
        this.wishlistItemRepository = wishlistItemRepository;
        this.wishlistHistoryRepository = wishlistHistoryRepository;
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> listPending(Long userId) {
        return wishlistItemRepository.findItemsWithoutStock(userId).stream()
                .map(NotificationResponse::from)
                .toList();
    }

    @Transactional
    public NotificationSummaryResponse send(Long userId) {
        List<WishlistItem> items = wishlistItemRepository.findItemsWithoutStock(userId);
        int sent = 0;
        for (WishlistItem item : items) {
            if (!item.isNotified()) {
                item.setNotified(true);
                wishlistItemRepository.save(item);
                recordOutOfStock(userId, item);
                sent++;
            }
        }
        List<NotificationResponse> notifications = items.stream().map(NotificationResponse::from).toList();
        return new NotificationSummaryResponse(sent, notifications.size(), notifications);
    }

    private void recordOutOfStock(Long userId, WishlistItem item) {
        WishlistHistory history = new WishlistHistory();
        history.setUser(new User(userId));
        history.setProduct(item.getProduct());
        history.setAction(ActionType.OUT_OF_STOCK);
        history.setDetail("Notificación de producto sin stock: \"" + item.getProduct().getName() + "\"");
        wishlistHistoryRepository.save(history);
    }
}
