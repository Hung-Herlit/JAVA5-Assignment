package assignment.java5.kudo_computer.controller;

import assignment.java5.kudo_computer.entity.Cart;
import assignment.java5.kudo_computer.entity.User;
import assignment.java5.kudo_computer.service.CartService;
import assignment.java5.kudo_computer.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private CartService cartService;

    // Phương thức này sẽ tự động chạy và gắn biến "cartCount" vào mọi trang HTML
    @ModelAttribute("cartCount")
    public int getCartCount() {
        User user = sessionService.get("currentUser", null);
        if (user != null) {
            List<Cart> cartItems = cartService.getCartByUser(user.getId());

            // Lấy tổng số lượng TẤT CẢ sản phẩm (Ví dụ: 2 chuột + 1 phím = 3)
            return cartItems.stream().mapToInt(Cart::getQuantity).sum();

            // Nếu bạn chỉ muốn đếm số LOẠI sản phẩm (2 chuột + 1 phím = 2 loại),
            // thì comment dòng trên và dùng dòng dưới này:
            // return cartItems.size();
        }
        return 0; // Trả về 0 nếu chưa đăng nhập hoặc giỏ hàng trống
    }
}