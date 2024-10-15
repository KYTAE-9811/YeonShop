package shoppingmall.yeonshop.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    private Double price;
    private String productName;
    private String description;
    private Integer stockQuantity;
    private String imageUrl;
}
