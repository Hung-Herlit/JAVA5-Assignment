package assignment.java5.kudo_computer.controller;

import assignment.java5.kudo_computer.entity.Cart;
import assignment.java5.kudo_computer.entity.User;
import assignment.java5.kudo_computer.service.CartService;
import assignment.java5.kudo_computer.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private SessionService sessionService;

    // Xem giỏ hàng
    @GetMapping
    public String viewCart(Model model) {
        // Không cần check user == null và redirect nữa vì Filter đã làm rồi
        User user = sessionService.get("currentUser", null);

        List<Cart> cartItems = cartService.getCartByUser(user.getId());
        Long totalAmount = cartService.calculateTotal(user.getId());

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", totalAmount);

        return "views/cart";
    }

    // Thêm vào giỏ hàng
    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Long productId,
            @RequestParam(value = "quantity", defaultValue = "1") Integer quantity) {
        // Bỏ check null user
        User user = sessionService.get("currentUser", null);

        cartService.addToCart(user.getId(), productId, quantity);
        return "redirect:/cart";
    }

    // Cập nhật số lượng
    @PostMapping("/update")
    public String updateQuantity(@RequestParam("cartId") Long cartId,
            @RequestParam("quantity") Integer quantity) {
        if (quantity > 0) {
            cartService.updateQuantity(cartId, quantity);
        } else {
            cartService.removeFromCart(cartId); // Nếu set số lượng = 0 thì xóa luôn
        }
        return "redirect:/cart";
    }

    // Xóa sản phẩm khỏi giỏ
    @GetMapping("/remove/{id}")
    public String removeFromCart(@PathVariable("id") Long cartId) {
        cartService.removeFromCart(cartId);
        return "redirect:/cart";
    }
}