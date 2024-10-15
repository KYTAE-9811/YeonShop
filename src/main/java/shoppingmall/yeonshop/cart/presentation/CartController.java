package shoppingmall.yeonshop.cart.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shoppingmall.yeonshop.cart.dto.CartItemUpdateRequest;
import shoppingmall.yeonshop.cart.dto.CartItemUpdateRequestList;
import shoppingmall.yeonshop.cart.dto.CartResponse;
import shoppingmall.yeonshop.cart.service.CartService;
import shoppingmall.yeonshop.order.service.OrderService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;

    // 장바구니 보기를 클릭했을때 장바구니 목록을 보여줌
    // 상품명 , 수량, 이미지 정도만
    @GetMapping
    public String getCart(Model model) {
        CartResponse cartResponse = new CartResponse();
        cartResponse = cartService.getAllCartItems();
        model.addAttribute("cart", cartResponse.cartItems);
        double totalPrice =  cartService.calculateTotalPrice();
        model.addAttribute("totalPrice", totalPrice);

        return "cart";
    }

    // 장바구니 추가는 상품상세 페이지에서 장바구니 담기를 클릭 -> 상세 페이지에서 productId 담아서 "/cart

    // 장바구니 추가된 상품에서 수량만 올릴 수 있음
    // 사실 결국 들어온 폼 데이터로 DB만 바꾸면 되는 거임
    @PostMapping("/updateCart")
    public String postCart(@ModelAttribute("updateCartItem") CartItemUpdateRequestList requestList) {
        // cartItemUpdateRequest들을 가진 래퍼클래스로 폼 데이터를 받아서 카트 아이템 리스트를 새로 만들고, cartService로 DB에 업데이트.
        List<CartItemUpdateRequest> cartItemUpdateRequests = requestList.getCartItemUpdateRequests();
        cartService.updateCartItems(cartItemUpdateRequests);
        // cart로 리다이렉트하면서 카트의 업데이트 결과가 보이게 됨
        return "redirect:/cart";
    }

    @PostMapping("/deleteItem")
    public String deleteCartItem(@RequestParam("cartItemId") Long cartItemId) {
        cartService.removeProductFromCart(cartItemId);
        return "redirect:/cart";
    }

    @GetMapping("/orderAllCart")
    public String orderCart() {
        orderService.sendCartToOrder();
        return "order";
    }
}
