package assignment.java5.kudo_computer.controller;

import assignment.java5.kudo_computer.entity.Cart;
import assignment.java5.kudo_computer.entity.User;
import assignment.java5.kudo_computer.service.CartService;
import assignment.java5.kudo_computer.service.OrderService;
import assignment.java5.kudo_computer.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private CartService cartService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private OrderService orderService;

    @GetMapping
    public String showCheckoutPage(Model model) {
        User user = sessionService.get("currentUser", null);
        if (user == null) {
            return "redirect:/login";
        }

        List<Cart> cartItems = cartService.getCartByUser(user.getId());
        if (cartItems.isEmpty()) {
            return "redirect:/cart"; // Giỏ hàng trống thì quay lại
        }

        Long totalAmount = cartService.calculateTotal(user.getId());
        Long shippingFee = 35000L; // Giả sử phí ship đồng giá 35k cho demo

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("shippingFee", shippingFee);
        model.addAttribute("finalTotal", totalAmount + shippingFee);
        model.addAttribute("user", user); // Truyền user xuống để fill sẵn Tên, SĐT

        return "views/checkout";
    }

    @PostMapping("/process")
    public String processCheckout(@RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address,
            @RequestParam("province") String province,
            @RequestParam("district") String district,
            @RequestParam("ward") String ward,
            @RequestParam("shippingUnit") String shippingUnit) {

        User user = sessionService.get("currentUser", null);
        if (user == null) {
            return "redirect:/login";
        }

        Long shippingFee = 35000L;
        orderService.placeOrder(user, name, phone, address, province, district, ward, shippingUnit, shippingFee);

        return "redirect:/checkout/success";
    }

    @GetMapping("/success")
    public String checkoutSuccess() {
        return "views/checkout-success";
    }
}