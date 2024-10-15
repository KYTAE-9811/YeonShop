package shoppingmall.yeonshop;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import shoppingmall.yeonshop.product.domain.Product;
import shoppingmall.yeonshop.product.repository.ProductRepository;

@Component
@RequiredArgsConstructor
public class TestInit {

    private final ProductRepository productRepository;

    @Bean
    public CommandLineRunner init() {
        return args -> {
            if (productRepository.count() == 0) {
                // 초기 샘플상품 등록
                Product product1 = new Product("나의 투쟁", 10000D, "히틀러의 자서전", 100, "uploads/img.png");
                Product product2 = new Product("역사란 무엇인가", 15000D, "한때 금서", 100,"uploads/img_1.png");
                productRepository.save(product1);
                productRepository.save(product2);
            }
        };
    }
}
