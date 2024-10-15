package shoppingmall.yeonshop.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingmall.yeonshop.order.domain.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
