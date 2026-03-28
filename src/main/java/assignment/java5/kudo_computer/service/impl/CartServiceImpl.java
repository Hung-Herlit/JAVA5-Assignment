package assignment.java5.kudo_computer.service.impl;

import assignment.java5.kudo_computer.entity.Cart;
import assignment.java5.kudo_computer.entity.Product;
import assignment.java5.kudo_computer.entity.User;
import assignment.java5.kudo_computer.repository.CartRepository;
import assignment.java5.kudo_computer.repository.ProductRepository;
import assignment.java5.kudo_computer.repository.UserRepository;
import assignment.java5.kudo_computer.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Cart> getCartByUser(Long userId) {
        return cartRepository.findByIdUser_Id(userId);
    }

    @Override
    public Cart addToCart(Long userId, Long productId, Integer quantity) {
        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        Optional<Cart> existingCart = cartRepository.findByIdUser_IdAndIdProduct_Id(userId, productId);

        if (existingCart.isPresent()) {
            // Nếu có rồi thì cộng dồn số lượng
            Cart cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + quantity);
            return cartRepository.save(cart);
        } else {
            // Nếu chưa có thì tạo mới
            Cart cart = new Cart();
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            cart.setIdUser(user);
            cart.setIdProduct(product);
            cart.setQuantity(quantity);
            return cartRepository.save(cart);
        }
    }

    @Override
    public void updateQuantity(Long cartId, Integer quantity) {
        cartRepository.findById(cartId).ifPresent(cart -> {
            cart.setQuantity(quantity);
            cartRepository.save(cart);
        });
    }

    @Override
    public void removeFromCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        cartRepository.deleteByIdUser_Id(userId);
    }

    @Override
    public Long calculateTotal(Long userId) {
        List<Cart> carts = cartRepository.findByIdUser_Id(userId);
        long total = 0;
        for (Cart cart : carts) {
            Product p = cart.getIdProduct();
            // Giá sau khi đã trừ đi discount
            long finalPrice = p.getPrice() - (p.getPrice() * p.getDiscount() / 100);
            total += finalPrice * cart.getQuantity();
        }
        return total;
    }
}