package shoppingmall.yeonshop.cart.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CartResponse {
    public List<CartItemResponse> cartItems;
}
