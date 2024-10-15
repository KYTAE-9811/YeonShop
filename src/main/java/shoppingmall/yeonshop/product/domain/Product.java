package shoppingmall.yeonshop.product.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shoppingmall.yeonshop.expception.NotEnoughStockException;
import shoppingmall.yeonshop.order.domain.OrderItem;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private Double price;
    private String description;
    private Integer stockQuantity;
    private String imageUrl;

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems = new ArrayList<>();


    public Product(String productName, Double price, String description, Integer stockQuantity, String imageUrl) {
        this.productName = productName;
        this.price = price;
        this.description = description;
        this.stockQuantity = stockQuantity;
        this.imageUrl = imageUrl;
    }

    public Product UpdateProduct(String productName, Double price, String description, Integer stockQuantity, String imageUrl) {
        this.productName = productName;
        this.price = price;
        this.description = description;
        this.stockQuantity = stockQuantity;
        this.imageUrl = imageUrl;
        return this;
    }


    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", stockQuantity=" + stockQuantity +
                ", orderItems=" + orderItems +
                '}';
    }

    // == 비즈니스 로직 == //
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }

}
