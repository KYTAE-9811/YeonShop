package shoppingmall.yeonshop.cart.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shoppingmall.yeonshop.cart.domain.CartItem;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CartItemRepositoryImpl implements CustomCartItemRepository {

    private final EntityManager em;


    @Override
    public Optional<CartItem> findByCartAndProduct(Long cartId, Long productId) {
         List result =  em.createQuery("select c from CartItem c where c.cart.id=:cartId and c.product.id=:productId")
                .setParameter("cartId", cartId)
                .setParameter("productId", productId)
                .getResultList();
         return result.isEmpty() ? Optional.empty() : Optional.of((CartItem) result.get(0));
    }
}
