package shoppingmall.yeonshop.home;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shoppingmall.yeonshop.cart.service.CartService;
import shoppingmall.yeonshop.product.domain.Product;
import shoppingmall.yeonshop.product.service.ProductService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class HomeController {

    private final ProductService productService;
    private final CartService cartService;

    @GetMapping
    public String home(Model model) {
        List<Product> products = productService.getAllProduct();
        model.addAttribute("products", products);
        return "home";
    }

    @GetMapping("/myPage")
    public String Mypage() {
        return "MyPage";
    }

    @GetMapping("/{productId}/details")
    public String getProductDetails(@PathVariable Long productId, Model model) {
        Product product = productService.getProductById(productId).orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));
        model.addAttribute("product", product);
        return "product-details";  // product-details.html 뷰 반환
    }

    @PostMapping("/{productId}/details/addToCart")
    public String addToCart(@PathVariable Long productId, @RequestParam("quantity") Integer quantity) {
        cartService.addProductToCart(productId, quantity);
        return "redirect:/products";
    }
}
