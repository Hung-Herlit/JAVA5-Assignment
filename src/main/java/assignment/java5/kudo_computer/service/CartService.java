package assignment.java5.kudo_computer.service;

import assignment.java5.kudo_computer.entity.Cart;
import java.util.List;

public interface CartService {
    List<Cart> getCartByUser(Long userId);

    Cart addToCart(Long userId, Long productId, Integer quantity);

    void updateQuantity(Long cartId, Integer quantity);

    void removeFromCart(Long cartId);

    void clearCart(Long userId);

    Long calculateTotal(Long userId);
}