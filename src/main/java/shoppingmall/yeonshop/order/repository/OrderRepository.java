package shoppingmall.yeonshop.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingmall.yeonshop.order.domain.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}
