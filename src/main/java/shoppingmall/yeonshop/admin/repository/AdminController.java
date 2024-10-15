package shoppingmall.yeonshop.admin.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shoppingmall.yeonshop.cart.service.CartService;
import shoppingmall.yeonshop.product.domain.Product;
import shoppingmall.yeonshop.product.dto.ProductRequest;
import shoppingmall.yeonshop.product.service.ProductService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService productService;
    private final CartService cartService;

    @GetMapping("/product/add")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product());
        return "product-form";
    }

    @PostMapping("/product/add")
    public String newProduct(@ModelAttribute("product") ProductRequest productRequest, @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        // 이미지 파일이 있는 경우 처리
        if (!imageFile.isEmpty()) {
            // 파일 저장 경로
            String uploadDir = "uploads/";
            String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);

            // 디렉토리 생성
            Files.createDirectories(Paths.get(uploadDir));

            // 파일 저장
            Files.write(filePath, imageFile.getBytes());

            // 이미지 경로를 상품에 설정
            productRequest.setImageUrl(filePath.toString());
        }

        productService.addProduct(productRequest);
        return "redirect:/products";
    }

    @PostMapping("/{productId}/delete")
    public String DeleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return "redirect:/";
    }

    @GetMapping("/{productId}/update")
    public String UpdateProduct(@PathVariable Long productId, Model model) {
        Product product = productService.getProductById(productId).orElseThrow(() -> new EntityNotFoundException("상품이 발견되지 않았습니다."));
        model.addAttribute("product", product);
        return "product-updateForm";
    }

    @PostMapping("/{productId}/update")
    public String UpdateProduct(@PathVariable Long productId, @ModelAttribute("product") ProductRequest productRequest) {
        productService.updateProduct(productId, productRequest);
        return "redirect:/products/{productId}/details";
    }
}
