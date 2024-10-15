package shoppingmall.yeonshop.order.domain;

import jakarta.persistence.*;
import lombok.*;
import shoppingmall.yeonshop.Users.domain.Users;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Orders {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Double totalPrice;

    private LocalDateTime date = LocalDateTime.now();


    public Orders(OrderStatus status) {
        this.status = status;
    }
    //==연관관계 메서드==//
    public void addOrderItem(OrderItem orderItem) {
        this.users = users;
        this.orderItems.add(orderItem);
    }

    public void setUsers(Users users) {
        this.users = users;
        users.getOrderList().add(this);
    } // 양방향

    //==생성 메서드==//
    public static Orders createOrder(Users users, OrderItem... orderItems) {
        Orders order = new Orders();
        order.users = users;
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        return order;
    }
}
