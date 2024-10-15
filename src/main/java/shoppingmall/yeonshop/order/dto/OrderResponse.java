package shoppingmall.yeonshop.order.dto;

import lombok.Getter;
import lombok.Setter;
import shoppingmall.yeonshop.Users.domain.Users;
import shoppingmall.yeonshop.order.domain.OrderItem;
import shoppingmall.yeonshop.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponse {
    public Long orderId;
    public Users user;
    public OrderStatus status;
    public Double totalPrice;
    public LocalDateTime orderDate;
    public List<OrderItem> orderItems;
}
