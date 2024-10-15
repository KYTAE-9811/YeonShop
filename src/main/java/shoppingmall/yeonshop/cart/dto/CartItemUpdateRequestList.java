package shoppingmall.yeonshop.cart.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CartItemUpdateRequestList {
    private List<CartItemUpdateRequest> cartItemUpdateRequests;
}