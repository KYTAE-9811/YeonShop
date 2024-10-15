package shoppingmall.yeonshop.order.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import shoppingmall.yeonshop.order.dto.OrderItemResponse;
import shoppingmall.yeonshop.order.dto.OrderResponse;
import shoppingmall.yeonshop.order.service.OrderService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public String AllOrders(Model model) {
        List<OrderResponse> orderResponses = orderService.getOrders();
        model.addAttribute("orders", orderResponses);
        return "orders";
    }

    @GetMapping("/{orderId}/order-detail")
    public String OrderDetail(@PathVariable("orderId") Long orderId, Model model) {
        List<OrderItemResponse> orderItemResponses = orderService.getOrderItems(orderId);
        model.addAttribute("orderItems", orderItemResponses);
        return "order-detail";
    }

    @GetMapping("/{orderId}/delete-order")
    public String CancelOrder(@PathVariable("orderId") Long orderId){
        orderService.deleteOrder(orderId);
        return "redirect:/orders";
    }

}
