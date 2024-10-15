package shoppingmall.yeonshop.cart.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.yeonshop.Users.login.repository.UsersRepositoryImpl;
import shoppingmall.yeonshop.Users.domain.Role;
import shoppingmall.yeonshop.Users.domain.Users;
import shoppingmall.yeonshop.cart.domain.Cart;
import shoppingmall.yeonshop.cart.domain.CartItem;
import shoppingmall.yeonshop.cart.dto.CartItemResponse;
import shoppingmall.yeonshop.cart.repository.CartItemRepositoryImpl;
import shoppingmall.yeonshop.cart.repository.CartRepository;
import shoppingmall.yeonshop.order.domain.Orders;
import shoppingmall.yeonshop.order.repository.OrderRepository;
import shoppingmall.yeonshop.product.domain.Product;
import shoppingmall.yeonshop.product.repository.ProductRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
class CartServiceTest {

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
    private UsersRepositoryImpl usersRepositoryImpl;
    @Autowired
    private OrderRepository orderRepository;


    private static Product SampleProduct1() {
        Product book1 = new Product("나의 투쟁", 24000D, null, 123, null);
        return book1;
    }
    private static Product SampleProduct2() {
        Product book2 = new Product("역사란 무엇인가", 25000D, null, 143,null);
        return book2;
    }

    @BeforeEach
    void setUp() {
        // 테스트 유저 셋팅, 로그인 되어있다고 가정
        Cart cart = new Cart();
        Users user = new Users("testuser@example.com", "adsf", Role.USER , cart);
//        usersRepository.save(user);
    }

    @Test
    @WithMockUser(username = "testuser@example.com")
    void 장바구니_추가_이미_존재() throws Exception {

        // Given
        Product book1 = SampleProduct1();
        productRepository.saveAndFlush(book1);
        System.out.println("book1의 상품 id : " + book1.getId());

        // When
        CartItemResponse cartItem = cartService.addProductToCart(book1.getId(), 12);
        System.out.println("첫 번째 추가 후 수량: " + cartItem.getQuantity());

        cartItem = cartService.addProductToCart(book1.getId(), 23);
        System.out.println("두 번째 추가 후 수량: " + cartItem.getQuantity());

        // Then
        Cart cart = cartRepository.findById(cartItem.getId()).orElseThrow();
        System.out.println("cart = " + cart.getCartItems());
        Assertions.assertThat(cart.getCartItems().size()).isEqualTo(1);
        assertEquals(35, cartItem.getQuantity());
    }
    @Test
    @WithMockUser(username = "testuser@example.com")
    void 장바구니_추가_존재_안함() throws Exception {
        // Given
        Product book1 = SampleProduct1();
        Product book2 = SampleProduct2();
        productRepository.saveAndFlush(book1);
        productRepository.saveAndFlush(book2);

        // When
        CartItemResponse cartItem = cartService.addProductToCart(book1.getId(), 12);
        cartService.addProductToCart(book2.getId(), 13);

        //then
        Cart cart = cartRepository.findById(cartItem.getId()).orElseThrow();
        System.out.println("cart = " + cart.getCartItems());
        Assertions.assertThat(cart.getCartItems().size()).isEqualTo(2);
    }

    @Test
    @WithMockUser(username = "testuser@example.com")
    void 장바구니_삭제() throws Exception {
        //given
        Product book1 = SampleProduct1();
        productRepository.saveAndFlush(book1);
        CartItemResponse cartItem = cartService.addProductToCart(book1.getId(), 12);

        //when
        cartService.removeProductFromCart(cartItem.getId());

        //then
        CartItem result = em.find(CartItem.class, cartItem.getId());
        Assertions.assertThat(result).isNull();
        Cart cart = cartRepository.findById(cartItem.getId()).orElseThrow();
        Assertions.assertThat(cart.getCartItems()).isEmpty();
    }

    @Test
    @WithMockUser(username = "testuser@example.com")
    void getCartItems() {
        //given
        Product book1 = SampleProduct1();
        Product book2 = SampleProduct2();
        productRepository.saveAndFlush(book1);
        productRepository.saveAndFlush(book2);
        CartItemResponse cartItem = cartService.addProductToCart(book1.getId(), 12);
        CartItemResponse cartItem2 = cartService.addProductToCart(book2.getId(), 13);

        // when
        List<CartItemResponse> cartItemList =  cartService.getAllCartItems().cartItems;

        // then
        Assertions.assertThat(cartItemList.size()).isEqualTo(2);
        System.out.println("cartItemList = " + cartItemList);
    }

    @Test
    @WithMockUser(username = "testuser@example.com")
    void 장바구니_클리어_주문후_처리() {
        //given
        Product book1 = SampleProduct1();
        Product book2 = SampleProduct2();
        productRepository.saveAndFlush(book1);
        productRepository.saveAndFlush(book2);
        CartItemResponse cartItem = cartService.addProductToCart(book1.getId(), 12);
        CartItemResponse cartItem2 = cartService.addProductToCart(book2.getId(), 13);

        //when
        Long orderId = cartService.sendCartToOrder();

        //then
        Orders order =  orderRepository.findById(orderId).orElseThrow();
        System.out.println("주문상품목록 " + order.getOrderItems().toString());
        Assertions.assertThat(order.getOrderItems()).hasSize(2);
    }

    @Test
    @WithMockUser(username = "testuser@example.com")
    void 장바구니_금액_합계() {
        // given
        Product book1 = SampleProduct1();
        Product book2 = SampleProduct2();
        productRepository.saveAndFlush(book1);
        productRepository.saveAndFlush(book2);
        cartService.addProductToCart(book1.getId(), 12);
        cartService.addProductToCart(book2.getId(), 13);

        // when
        double total = cartService.calculateTotalPrice();

        //then
        System.out.println("total = " + total);
        Assertions.assertThat(total).isEqualTo(49000D);
    }
}
