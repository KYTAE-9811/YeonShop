package shoppingmall.yeonshop.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;
    private Integer price;
    private String productName;
    private String description;
    private Integer stockQuantity;
    private String imageUrl;
}
