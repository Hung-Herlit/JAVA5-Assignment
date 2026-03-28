package assignment.java5.kudo_computer.controller;

import assignment.java5.kudo_computer.entity.Order;
import assignment.java5.kudo_computer.entity.OrderDetails;
import assignment.java5.kudo_computer.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    // Hiển thị danh sách tất cả đơn hàng
    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "admin/order/list";
    }

    // Hiển thị chi tiết đơn hàng
    @GetMapping("/detail/{id}")
    public String orderDetail(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            ra.addFlashAttribute("error", "Không tìm thấy đơn hàng!");
            return "redirect:/admin/orders";
        }

        List<OrderDetails> orderDetails = orderService.getOrderDetails(id);
        model.addAttribute("order", order);
        model.addAttribute("orderDetails", orderDetails);
        return "admin/order/detail";
    }

    // Xử lý cập nhật trạng thái đơn hàng
    @PostMapping("/update-status")
    public String updateStatus(@RequestParam("orderId") Long orderId,
            @RequestParam("status") String status,
            RedirectAttributes ra) {
        orderService.updateOrderStatus(orderId, status);
        ra.addFlashAttribute("message", "Cập nhật trạng thái đơn hàng thành công!");
        return "redirect:/admin/orders/detail/" + orderId;
    }
}