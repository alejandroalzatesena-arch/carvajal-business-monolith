package com.carvajal.wishlist.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carvajal.wishlist.dto.wishlist.WishlistItemRequest;
import com.carvajal.wishlist.dto.wishlist.WishlistItemResponse;
import com.carvajal.wishlist.dto.wishlist.WishlistItemUpdateRequest;
import com.carvajal.wishlist.entity.Product;
import com.carvajal.wishlist.entity.User;
import com.carvajal.wishlist.entity.WishlistHistory;
import com.carvajal.wishlist.entity.WishlistItem;
import com.carvajal.wishlist.entity.enums.ActionType;
import com.carvajal.wishlist.exception.BusinessException;
import com.carvajal.wishlist.exception.ResourceNotFoundException;
import com.carvajal.wishlist.repository.ProductRepository;
import com.carvajal.wishlist.repository.WishlistHistoryRepository;
import com.carvajal.wishlist.repository.WishlistItemRepository;

@Service
public class WishlistService {

    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;
    private final WishlistHistoryRepository wishlistHistoryRepository;

    public WishlistService(WishlistItemRepository wishlistItemRepository,
                           ProductRepository productRepository,
                           WishlistHistoryRepository wishlistHistoryRepository) {
        this.wishlistItemRepository = wishlistItemRepository;
        this.productRepository = productRepository;
        this.wishlistHistoryRepository = wishlistHistoryRepository;
    }

    @Transactional(readOnly = true)
    public List<WishlistItemResponse> list(Long userId) {
        return wishlistItemRepository.findByUserIdOrderByAddedAtDesc(userId).stream()
                .map(WishlistItemResponse::from)
                .toList();
    }

    @Transactional
    public WishlistItemResponse add(Long userId, WishlistItemRequest request) {
        if (wishlistItemRepository.existsByUserIdAndProductId(userId, request.productId())) {
            throw new BusinessException("El producto ya se encuentra en la lista de deseos");
        }
        Product product = productRepository.findByIdAndActiveTrue(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + request.productId()));

        WishlistItem item = new WishlistItem();
        item.setUser(new User(userId));
        item.setProduct(product);
        item.setQuantity(request.quantity() != null ? request.quantity() : 1);
        item.setNotified(product.getStock() <= 0);
        WishlistItem saved = wishlistItemRepository.save(item);

        recordHistory(userId, product, ActionType.ADDED, "Producto agregado a la lista de deseos");
        if (product.getStock() <= 0) {
            recordHistory(userId, product, ActionType.OUT_OF_STOCK,
                    "Producto sin stock al momento de agregarlo a la lista de deseos");
        }
        return WishlistItemResponse.from(saved);
    }

    @Transactional
    public WishlistItemResponse update(Long userId, Long itemId, WishlistItemUpdateRequest request) {
        WishlistItem item = getItem(userId, itemId);
        item.setQuantity(request.quantity() != null ? request.quantity() : item.getQuantity());
        WishlistItem saved = wishlistItemRepository.save(item);
        recordHistory(userId, saved.getProduct(), ActionType.UPDATED, "Cantidad actualizada a " + saved.getQuantity());
        return WishlistItemResponse.from(saved);
    }

    @Transactional
    public void remove(Long userId, Long itemId) {
        WishlistItem item = getItem(userId, itemId);
        wishlistItemRepository.delete(item);
        recordHistory(userId, item.getProduct(), ActionType.REMOVED, "Producto eliminado de la lista de deseos");
    }

    @Transactional(readOnly = true)
    public WishlistItemResponse getItemResponse(Long userId, Long itemId) {
        return WishlistItemResponse.from(getItem(userId, itemId));
    }

    private WishlistItem getItem(Long userId, Long itemId) {
        return wishlistItemRepository.findByIdAndUserId(itemId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Ítem de la lista de deseos no encontrado"));
    }

    private void recordHistory(Long userId, Product product, ActionType action, String detail) {
        WishlistHistory history = new WishlistHistory();
        history.setUser(new User(userId));
        history.setProduct(product);
        history.setAction(action);
        history.setDetail(detail);
        wishlistHistoryRepository.save(history);
    }
}
