package shoppingmall.yeonshop.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponse {
    public Long orderItemId;
    public Long productId;
    public String productName;
    public double productPrice;
    public Integer quantity;
}
