package assignment.java5.kudo_computer.controller;

import assignment.java5.kudo_computer.repository.OrderRepository;
import assignment.java5.kudo_computer.repository.ProductRepository;
import assignment.java5.kudo_computer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping({ "", "/", "/dashboard" })
    public String dashboard(Model model) {
        // 1. Lấy các con số thống kê
        long totalUsers = userRepository.count();
        long totalProducts = productRepository.count();
        long totalOrders = orderRepository.count();
        Long totalRevenue = orderRepository.calculateTotalRevenue();

        // 2. Truyền sang View
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalRevenue", totalRevenue);

        // 3. Lấy 5 đơn hàng gần nhất
        model.addAttribute("recentOrders", orderRepository.findTop5ByOrderByOrderDateDesc());

        return "admin/dashboard";
    }
}