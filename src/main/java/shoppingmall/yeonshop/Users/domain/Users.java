package shoppingmall.yeonshop.Users.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shoppingmall.yeonshop.cart.domain.Cart;
import shoppingmall.yeonshop.order.domain.Orders;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Orders> OrderList = new ArrayList<>();

    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @Embedded
    private Address address;

    public Users(String email, String username, String password, Role role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }
    public Users(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Users(String email, String password, Role role, Cart cart) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.cart = cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
        if (cart.getUsers() != this) {
            cart.setUsers(this); // 양방향 관계 설정
        }
    }

    public String getPassword() {
        return password;
    }

    public Address getAddress() {
        return address;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public List<Orders> getOrderList() {
        return OrderList;
    }

    public Cart getCart() {
        return cart;
    }
}
