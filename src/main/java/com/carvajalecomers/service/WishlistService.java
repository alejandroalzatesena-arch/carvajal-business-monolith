package com.carvajalecomers.service;

import com.carvajalecomers.entity.Product;
import com.carvajalecomers.entity.User;
import com.carvajalecomers.entity.Wishlist;
import com.carvajalecomers.entity.WishlistAction;
import com.carvajalecomers.entity.WishlistHistory;
import com.carvajalecomers.entity.WishlistItem;
import com.carvajalecomers.exception.ResourceNotFoundException;
import com.carvajalecomers.repository.WishlistHistoryRepository;
import com.carvajalecomers.repository.WishlistItemRepository;
import com.carvajalecomers.repository.WishlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final WishlistHistoryRepository wishlistHistoryRepository;
    private final UserService userService;
    private final ProductService productService;

    public WishlistService(WishlistRepository wishlistRepository,
                            WishlistItemRepository wishlistItemRepository,
                            WishlistHistoryRepository wishlistHistoryRepository,
                            UserService userService,
                            ProductService productService) {
        this.wishlistRepository = wishlistRepository;
        this.wishlistItemRepository = wishlistItemRepository;
        this.wishlistHistoryRepository = wishlistHistoryRepository;
        this.userService = userService;
        this.productService = productService;
    }

    /**
     * Obtiene la wishlist del usuario o la crea si aún no existe.
     */
    public Wishlist getOrCreateByUser(Long userId) {
        return wishlistRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userService.getById(userId);
                    return wishlistRepository.save(new Wishlist(user));
                });
    }

    // Sin readOnly: getOrCreateByUser puede insertar la wishlist en el primer
    // acceso del usuario. Al ser una autoinvocacion no pasa por el proxy de
    // Spring, asi que heredaria esta transaccion y PostgreSQL rechazaria el
    // INSERT con "cannot execute INSERT in a read-only transaction".
    public List<WishlistItem> listItems(Long userId) {
        Wishlist wishlist = getOrCreateByUser(userId);
        return wishlistItemRepository.findByWishlistId(wishlist.getId());
    }

    public WishlistItem addProduct(Long userId, Long productId, Integer desiredQuantity) {
        Wishlist wishlist = getOrCreateByUser(userId);
        Product product = productService.getById(productId);

        WishlistItem item = wishlistItemRepository.findByWishlistIdAndProductId(wishlist.getId(), productId)
                .orElse(null);

        if (item == null) {
            int quantity = desiredQuantity != null ? desiredQuantity : 1;
            item = wishlistItemRepository.save(new WishlistItem(wishlist, product, quantity));
            recordHistory(wishlist, product, WishlistAction.ADDED);
        } else {
            item.setDesiredQuantity(desiredQuantity != null ? desiredQuantity : item.getDesiredQuantity());
            recordHistory(wishlist, product, WishlistAction.UPDATED);
        }

        return item;
    }

    public WishlistItem updateItem(Long userId, Long productId, Integer desiredQuantity) {
        Wishlist wishlist = getOrCreateByUser(userId);
        WishlistItem item = wishlistItemRepository.findByWishlistIdAndProductId(wishlist.getId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("El producto no está en la lista de deseos"));

        item.setDesiredQuantity(desiredQuantity);
        recordHistory(wishlist, item.getProduct(), WishlistAction.UPDATED);

        return item;
    }

    public void removeProduct(Long userId, Long productId) {
        Wishlist wishlist = getOrCreateByUser(userId);
        WishlistItem item = wishlistItemRepository.findByWishlistIdAndProductId(wishlist.getId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("El producto no está en la lista de deseos"));

        wishlistItemRepository.delete(item);
        recordHistory(wishlist, item.getProduct(), WishlistAction.REMOVED);
    }

    // Sin readOnly por el mismo motivo que listItems.
    public List<WishlistHistory> getHistory(Long userId) {
        Wishlist wishlist = getOrCreateByUser(userId);
        return wishlistHistoryRepository.findByWishlistIdOrderByActionAtDesc(wishlist.getId());
    }

    private void recordHistory(Wishlist wishlist, Product product, WishlistAction action) {
        WishlistHistory history = new WishlistHistory(
                wishlist,
                product,
                action,
                product.getName(),
                product.getPrice()
        );
        wishlistHistoryRepository.save(history);
    }
}
