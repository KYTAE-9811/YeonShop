package shoppingmall.yeonshop.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shoppingmall.yeonshop.cart.domain.CartItem;

// 커스텀 리포지토리를 쓰고 싶으면 따로 인터페이스를 만들고 그걸 상속 받아오면 됨

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> , CustomCartItemRepository {
    public CartItem findByCartIdAndProductId(Long cartId, Long productId);
    public CartItem saveAndFlush(CartItem cartItem);
}
