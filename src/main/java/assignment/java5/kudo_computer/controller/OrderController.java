package assignment.java5.kudo_computer.controller;

import assignment.java5.kudo_computer.entity.Order;
import assignment.java5.kudo_computer.entity.OrderDetails;
import assignment.java5.kudo_computer.entity.User;
import assignment.java5.kudo_computer.service.OrderService;
import assignment.java5.kudo_computer.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private SessionService sessionService;

    // Xem danh sách đơn hàng
    @GetMapping
    public String orderHistory(Model model) {
        User user = sessionService.get("currentUser", null);
        if (user == null) {
            return "redirect:/login";
        }

        List<Order> orders = orderService.getOrderHistory(user.getId());
        model.addAttribute("orders", orders);
        return "views/order-history";
    }

    // Xem chi tiết một đơn hàng cụ thể
    @GetMapping("/detail/{id}")
    public String orderDetail(@PathVariable("id") Long orderId, Model model) {
        User user = sessionService.get("currentUser", null);
        if (user == null) {
            return "redirect:/login";
        }

        Order order = orderService.getOrderById(orderId);

        // Kiểm tra bảo mật: Đảm bảo đơn hàng này thuộc về user đang đăng nhập
        if (order == null || !order.getUser().getId().equals(user.getId())) {
            return "redirect:/orders";
        }

        List<OrderDetails> details = orderService.getOrderDetails(orderId);
        model.addAttribute("order", order);
        model.addAttribute("orderDetails", details);

        return "views/order-detail";
    }
}