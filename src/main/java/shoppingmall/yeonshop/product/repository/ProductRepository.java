package shoppingmall.yeonshop.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingmall.yeonshop.product.domain.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    public Optional<Product> findById(Long productId);
    public Product saveAndFlush(Product product);
}
