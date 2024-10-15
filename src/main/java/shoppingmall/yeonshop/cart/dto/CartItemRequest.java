package shoppingmall.yeonshop.cart.dto;

import lombok.*;
import shoppingmall.yeonshop.product.domain.Product;

@Getter
@Setter
public class CartItemRequest {
    private Product product;
    private int quantity;
}