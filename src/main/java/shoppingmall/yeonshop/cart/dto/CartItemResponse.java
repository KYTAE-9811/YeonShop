package shoppingmall.yeonshop.cart.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartItemResponse {
    public Long id;
    private String productName;
    private double productPrice;
    private Integer quantity;
}
