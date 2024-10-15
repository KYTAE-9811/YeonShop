package shoppingmall.yeonshop.order.service;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.yeonshop.Users.domain.Users;
import shoppingmall.yeonshop.Users.login.repository.UsersRepositoryImpl;
import shoppingmall.yeonshop.cart.domain.Cart;
import shoppingmall.yeonshop.cart.domain.CartItem;
import shoppingmall.yeonshop.cart.repository.CartRepository;
import shoppingmall.yeonshop.order.domain.OrderItem;
import shoppingmall.yeonshop.order.domain.OrderStatus;
import shoppingmall.yeonshop.order.domain.Orders;
import shoppingmall.yeonshop.order.dto.OrderItemResponse;
import shoppingmall.yeonshop.order.dto.OrderResponse;
import shoppingmall.yeonshop.order.repository.OrderItemRepository;
import shoppingmall.yeonshop.order.repository.OrderRepository;
import shoppingmall.yeonshop.product.domain.Product;
import shoppingmall.yeonshop.product.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final UsersRepositoryImpl usersRepositoryImpl;
    private final ProductRepository productRepository;
    private final EntityManager em;
    /**
     *
     * 오더 하나에는 여러개의 주문 정보가 들어간다.
     *
     * @return
     */

    // 1. 장바구니에서 주문으로 넘기는 로직 (오더 ID를 반환)
    @Transactional
    public Long sendCartToOrder() {
        System.out.println("sendCartToOrder 메서드에 진입했습니다.");
        Cart cart = getCartForCurrentUser();
        Orders order = new Orders(OrderStatus.ORDER);
        order.setUsers(cart.getUsers());

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem(cartItem.getProduct(), cartItem.getQuantity());
            order.addOrderItem(orderItem);
        }
        orderRepository.save(order);
        cart.getCartItems().clear();
        cartRepository.save(cart);

        for (OrderItem orderItem : order.getOrderItems()) {
            Product product = productRepository.findById(orderItem.getProduct().getId()).orElseThrow(() -> new EntityNotFoundException("Product not found"));

            product.removeStock(orderItem.getQuantity());
        }
        return order.getId();
    }

    // 2. 전체 주문목록 조회
    public List<OrderResponse> getOrders() {
        List<OrderResponse> orderList = new ArrayList<>();
        List<Orders> result = new ArrayList<>();
        result = orderRepository.findAll();
        for (Orders order : result) {
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setOrderId(order.getId());
            orderResponse.setOrderItems(order.getOrderItems());
            orderResponse.setUser(order.getUsers());
            orderResponse.setStatus(order.getStatus());
            orderResponse.setTotalPrice(order.getTotalPrice());
            orderResponse.setOrderDate(order.getDate());
            orderList.add(orderResponse);
        }
        return orderList;
    }

    // 3. 각 주문의 주문 아이템들 조회
    public List<OrderItemResponse> getOrderItems(Long orderItemId) {
        List<OrderItem> orderItemList = orderItemRepository.findAll();
        List<OrderItemResponse> orderItemResponseList = new ArrayList<>();
        for (OrderItem orderItem : orderItemList) {
            OrderItemResponse orderItemResponse = new OrderItemResponse();
            orderItemResponse.setOrderItemId(orderItem.getId());
            orderItemResponse.setProductId(orderItem.getProduct().getId());
            orderItemResponse.setQuantity(orderItem.getQuantity());
            orderItemResponse.setProductName(orderItem.getProduct().getProductName());
            orderItemResponse.setProductPrice(orderItem.getProduct().getPrice());
            orderItemResponseList.add(orderItemResponse);
        }
        return orderItemResponseList;
    }
    // 4. 주문 하나 삭제
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
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
        // 근데 애초에 개인 화면이라 예외가 터질 일은 없을거임
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
