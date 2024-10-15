package shoppingmall.yeonshop.cart.repository;

import shoppingmall.yeonshop.cart.domain.CartItem;

import java.util.Optional;

public interface CustomCartItemRepository {
    public Optional<CartItem> findByCartAndProduct(Long cartId, Long productId);
}
