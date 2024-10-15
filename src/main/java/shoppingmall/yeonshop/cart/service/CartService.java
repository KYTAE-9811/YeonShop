package shoppingmall.yeonshop.cart.service;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.yeonshop.Users.login.repository.UsersRepositoryImpl;
import shoppingmall.yeonshop.Users.domain.Users;
import shoppingmall.yeonshop.cart.domain.Cart;
import shoppingmall.yeonshop.cart.domain.CartItem;
import shoppingmall.yeonshop.cart.dto.CartItemUpdateRequest;
import shoppingmall.yeonshop.cart.dto.CartResponse;
import shoppingmall.yeonshop.cart.repository.CartItemRepository;
import shoppingmall.yeonshop.cart.dto.CartItemResponse;
import shoppingmall.yeonshop.cart.repository.CartRepository;
import shoppingmall.yeonshop.order.domain.OrderItem;
import shoppingmall.yeonshop.order.domain.OrderStatus;
import shoppingmall.yeonshop.order.domain.Orders;
import shoppingmall.yeonshop.order.repository.OrderItemRepository;
import shoppingmall.yeonshop.order.repository.OrderRepository;
import shoppingmall.yeonshop.product.domain.Product;
import shoppingmall.yeonshop.product.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UsersRepositoryImpl usersRepositoryImpl;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    // 근데 여기서 DTO로 변환해서 반환하는 로직도 넣긴 해야함

    // 장바구니에 상품 추가
    public CartItemResponse addProductToCart(Long productId, int quantity) {
        // 현재 로그인 사용자 장바구니 조회
        Cart cart = getCartForCurrentUser();

        // 상품 조회
        Product product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("상품이 발견되지 않았습니다."));

        String email = getCurrentUserEmail();

        // 장바구니에 해당 상품이 존재하는지 확인
        Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndProduct(cart.getId(), productId);

        if (existingCartItem.isPresent()) {
            // 기존 상품이 있을 경우에는 수량만 증가
            CartItem cartItem = existingCartItem.get();
            cartItem.updateQuantity(cartItem.getQuantity() + quantity);
//            cartItem.setCart(cart);
//            cart.addCartItem(cartItem);
//            -> 이거 살려놓으면 기존 상품 있는 경우에 하나 더 생겨서 에러남
//            이미 있는 경우인데 또 추가하는거였음...
            CartItem entityCartItem = cartItemRepository.saveAndFlush(cartItem);
            return convertTocartItemResponse(entityCartItem);
        } else {
            CartItem cartItem = new CartItem(product, product.getPrice(), quantity);
            cartItem.setCart(cart);
            cart.addCartItem(cartItem);
            CartItem entityCartItem = cartItemRepository.saveAndFlush(cartItem);
            return convertTocartItemResponse(entityCartItem);
        }
    }
    // 장바구니의 상품 업데이트
    public List<CartItemResponse> updateCartItems(List<CartItemUpdateRequest> cartItemUpdateRequests) {
        // 현재 로그인 사용자 장바구니 조회
        Cart cart = getCartForCurrentUser();

        List<CartItemResponse> responses = new ArrayList<>();

        for (CartItemUpdateRequest request : cartItemUpdateRequests) {
            Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new EntityNotFoundException("상품이 발견되지 않았습니다."));

            Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndProduct(cart.getId(), product.getId());

            if (existingCartItem.isPresent()) {
                CartItem cartItem = existingCartItem.get();
                cartItem.updateQuantity(request.getQuantity());
                CartItem updatedCartItem = cartItemRepository.saveAndFlush(cartItem);
                responses.add(convertTocartItemResponse(updatedCartItem));
            }
            else {
                CartItem cartItem = new CartItem(product, product.getPrice(), request.getQuantity());
                cartItem.setCart(cart);
                CartItem newCartItem = cartItemRepository.saveAndFlush(cartItem);
                responses.add(convertTocartItemResponse(newCartItem));
            }
        }
        return responses;
    }

    // 장바구니에서 상품 삭제
    public void removeProductFromCart(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("CartItem not found"));
        Cart cart = cartItem.getCart();
        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
    }

    // 장바구니 상품 목록 조회
    public CartResponse getAllCartItems() {
        Cart cart = getCartForCurrentUser();
        List<CartItemResponse> crl = new ArrayList<>();
        CartResponse cartResponse = new CartResponse();
        for (CartItem cartItem : cart.getCartItems()) {
            CartItemResponse cartItemResponse = convertTocartItemResponse(cartItem);
            crl.add(cartItemResponse);
            cartResponse.cartItems = crl;
        }
        if (crl != null) {
            return cartResponse;
        } else
            return null;
    }

//    public Long sendCartToOrder() {
//        Cart cart = getCartForCurrentUser();
//        Orders order = new Orders(OrderStatus.ORDER);
//        // 일단 장바구니에서 주문 전송을 누른 상태일거니까 스테이터스는 바로 ORDER
//        // 나중에 캔슬해도 되니께
//        System.out.println("cart.toString() = " + cart.toString());
//        order.setUsers(cart.getUsers());
//        // orders가 연관관계 설정을 이렇게 해주는 것이 맞다.
//        // 이렇게 하면 현재 유저랑 연결된 오더가 만들어졌다.
//        // 이제 이 오더의 orderItem에 cart의 cartItem을 넣어보자
//        for (CartItem cartItem : cart.getCartItems()) {
//            OrderItem orderItem = new OrderItem(cartItem.getProduct(), cartItem.getQuantity());
//            // 오더아이템 생성자나 팩토리 메소드에다가 cartItem 요소들을 orderItems에 넣어줌
//            order.addOrderItem(orderItem);
//        }
//        orderRepository.save(order);
//        cart.getCartItems().clear(); // 리스트 내장함수임. 리스트 내부를 전부 지우는
//        cartRepository.saveAndFlush(cart); // 클리어 한채로 다시 지우기
//        return order.getId();
//    }
//    얘 때문에 개고생했네... 그전에 만들어놨던 장바구니->주문 메소드가 그대로 살아서 컨트롤러에서 쓰이고 있었음..


    // 장바구니 총 금액 계산
    public double calculateTotalPrice() {
        Cart cart = getCartForCurrentUser();
        return cart.getTotalPrice();
    }


    // CartItem을 CartItemResponse로 변환하는 메서드
    private CartItemResponse convertTocartItemResponse(CartItem cartItem) {
        CartItemResponse cartItemResponse = new CartItemResponse();
        cartItemResponse.setId(cartItem.getCart().getId());
        cartItemResponse.setProductName(cartItem.getProduct().getProductName());
        cartItemResponse.setQuantity(cartItem.getQuantity());
        cartItemResponse.setProductPrice(cartItem.getProduct().getPrice());
        return cartItemResponse;
    }

    //현재 로그인한 사용자 정보 가져오기
    private String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
    public Cart getCartForCurrentUser() {
        String email = getCurrentUserEmail();
        Users user = usersRepositoryImpl.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("유저가 발견되지 않습니다."));
        System.out.println("user = " + user.getCart());
        // 근데 애초에 개인 화면이라 예외가 터질 일은 없을거임, 그저 Optional로 리턴 받은거니까 예외처리 하는것임
        if (user.getCart() == null) {
            Cart cart = new Cart();
            cart.setUsers(user);
            cartRepository.saveAndFlush(cart);
            return cart;
        }else {
            user.getCart().setUsers(user);
            cartRepository.saveAndFlush(user.getCart());
            return user.getCart();
        }
    }
}
