package shoppingmall.yeonshop.cart.domain;

import jakarta.persistence.*;
import lombok.*;
import shoppingmall.yeonshop.cart.dto.CartItemRequest;
import shoppingmall.yeonshop.product.domain.Product;

import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Getter
@NoArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue
    private Long id;

    @JoinColumn(name = "cart_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private double orderPrice;
    private Integer quantity;

    @Override
    public String toString() {
        return "CartItem{" +
                ", product=" + product +
                ", orderPrice=" + orderPrice +
                ", quantity=" + quantity +
                '}';
    }
    //== 연관관계 편의 메서드 ==//
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    //== 생성 메서드 ==//
    public CartItem(Product product, double orderPrice, Integer quantity) {
        this.product = product;
        this.orderPrice = orderPrice;
        this.quantity = quantity;
    }

    //== 명시적 수정 메서드 ==//
    // 수량 변경 메소드
    public void updateQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("수량은 0 이상이어야 합니다");
        }
        this.quantity = quantity;
    }

    // 상품 추가 메서드
    public void addQuantity(int additionalQuantity) {
        if (additionalQuantity < 0) {
            throw new IllegalArgumentException("추가 수량은 0 이상이어야 합니다.");
        }
        this.quantity += additionalQuantity;
    }

    // 수량만 변경하는 메소드임

}
