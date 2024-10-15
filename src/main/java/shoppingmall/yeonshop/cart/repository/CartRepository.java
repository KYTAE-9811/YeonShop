package shoppingmall.yeonshop.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shoppingmall.yeonshop.cart.domain.Cart;
import shoppingmall.yeonshop.cart.domain.CartItem;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    public Cart saveAndFlush(Cart cart);
}
