package shoppingmall.yeonshop.order.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.yeonshop.Users.Repository.UserRepository;
import shoppingmall.yeonshop.Users.domain.Role;
import shoppingmall.yeonshop.Users.domain.Users;
import shoppingmall.yeonshop.Users.login.repository.UsersRepositoryImpl;
import shoppingmall.yeonshop.cart.domain.Cart;
import shoppingmall.yeonshop.cart.repository.CartItemRepositoryImpl;
import shoppingmall.yeonshop.cart.repository.CartRepository;
import shoppingmall.yeonshop.cart.service.CartService;
import shoppingmall.yeonshop.order.domain.OrderItem;
import shoppingmall.yeonshop.order.domain.Orders;
import shoppingmall.yeonshop.order.dto.OrderItemResponse;
import shoppingmall.yeonshop.order.dto.OrderResponse;
import shoppingmall.yeonshop.order.repository.OrderRepository;
import shoppingmall.yeonshop.product.domain.Product;
import shoppingmall.yeonshop.product.repository.ProductRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
class OrderServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    CartService cartService;
    @Autowired
    CartItemRepositoryImpl cartItemRepositoryImpl;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;

    private static Product SampleProduct1() {
        Product book1 = new Product("나의 투쟁", 24000D, null, 123, null);
        return book1;
    }

    private static Product SampleProduct2() {
        Product book2 = new Product("역사란 무엇인가", 25000D, null, 143, null);
        return book2;
    }

    @BeforeEach
    void setUp() {
        // 테스트 유저 셋팅, 로그인 되어있고 상품 1,2 모두 장바구니에 들어있음.
        Cart cart = new Cart();
        Users user = new Users("testuser@example.com", "adsf", Role.USER, cart);
        userRepository.save(user);
        Product book1 = SampleProduct1();
        Product book2 = SampleProduct2();
        productRepository.save(book1);
        productRepository.save(book2);
        cartService.addProductToCart(book1.getId(), 1);
        cartService.addProductToCart(book2.getId(), 1);
    }

    @Test
    @WithMockUser(username = "testuser@example.com")
    void 장바구니_목록에서_주문으로() {
        //given
        //when
        Long orderId = orderService.sendCartToOrder();

        //then
        Orders orders = orderRepository.findById(orderId).orElseThrow(() -> new
                EntityNotFoundException("주문내역이 없습니다."));
        assertThat(orders.getOrderItems().size()).isEqualTo(2);
        for (OrderItem orderItem : orders.getOrderItems()) {
            Product product = productRepository.findById(orderItem.getProduct().getId()).orElseThrow(() -> new EntityNotFoundException("없어"));
            System.out.println(product.getStockQuantity());
        }
    }

    @Test
    @WithMockUser(username = "testuser@example.com")
    void 전체_주문내역_조회() {
        //when
        Long orderId = orderService.sendCartToOrder();
        List<OrderResponse> orderResponse = orderService.getOrders();

        //then
        assertThat(orderResponse.size()).isEqualTo(1);
    }

    @Test
    @WithMockUser(username = "testuser@example.com")
    void 특정_주문내역_조회() {
        //when
        Long orderId = orderService.sendCartToOrder();
        List<OrderItemResponse> orderItemsRes = orderService.getOrderItems(orderId);

        //then
        assertThat(orderItemsRes.size()).isEqualTo(2);

    }
}