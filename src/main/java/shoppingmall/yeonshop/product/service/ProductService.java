package shoppingmall.yeonshop.product.service;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.yeonshop.product.domain.Product;
import shoppingmall.yeonshop.product.dto.ProductRequest;
import shoppingmall.yeonshop.product.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 추가. 삭제. 수정 (관리자 기능)
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void addProduct(ProductRequest productRequest) {
        Product product = ProductDTOToEntity(productRequest);
        productRepository.save(product);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateProduct(Long productId, ProductRequest newProductRequest) {
        // findById로 영속성 컨텍스트에서 관리되게 됨
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

        // 엔티티 수정 (세터를 사용하지 않고, 명시적인 수정 메소드를 사용)
        product.UpdateProduct(newProductRequest.getProductName(), newProductRequest.getPrice(), newProductRequest.getDescription(), newProductRequest.getStockQuantity(), newProductRequest.getImageUrl());
    }

    // 전체 상품 조회
    public List<Product> getAllProduct() {
        List<Product> products = productRepository.findAll();
        System.out.println("products = " + products.size());
        return products;
    }
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }


    private static Product ProductDTOToEntity(ProductRequest productRequest) {
       Product product = new Product(productRequest.getProductName(), productRequest.getPrice(), productRequest.getDescription(), productRequest.getStockQuantity(), productRequest.getImageUrl());

        return product;
    }
}
