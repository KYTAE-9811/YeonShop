package shoppingmall.yeonshop.cart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemUpdateRequest {
    Long productId;
    int quantity;
}
