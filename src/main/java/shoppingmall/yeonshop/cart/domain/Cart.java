package shoppingmall.yeonshop.cart.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shoppingmall.yeonshop.Users.domain.Users;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", users=" + users +
                ", cartItems=" + cartItems +
                '}';
    }
    //== 연관관계 메서드 ==//

    public void setUsers(Users user) {
        this.users = user;
        if (user.getCart() != this) {
            user.setCart(this); // 양방향 관계 설정
        }
    }

    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.setCart(this);
    }

    public void removeCartItem(CartItem cartItem) {
        cartItems.remove(cartItem);
        cartItem.setCart(null);
    }

    //== 생성 메서드 ==//
    public static Cart createCart(Users user, CartItem... cartItems) {
        Cart cart = new Cart();
        cart.users = user;
        for (CartItem cartItem : cartItems) {

            cart.addCartItem(cartItem);
        }
        return cart;
    }

    //== 조회 로직 ==//
    public double getTotalPrice() {
        double totalPrice = 0;
        for (CartItem cartItem : cartItems) {
            totalPrice += cartItem.getOrderPrice();
        }
        return totalPrice;
    }
}