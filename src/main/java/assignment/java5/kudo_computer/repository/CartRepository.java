package assignment.java5.kudo_computer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import assignment.java5.kudo_computer.entity.Cart;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    // Lấy toàn bộ sản phẩm trong giỏ hàng của 1 user
    List<Cart> findByIdUser_Id(Long userId);

    // Kiểm tra xem sản phẩm đã có trong giỏ hàng của user chưa (để cộng dồn số
    // lượng)
    Optional<Cart> findByIdUser_IdAndIdProduct_Id(Long userId, Long productId);

    // Xóa toàn bộ giỏ hàng của user (Dùng sau khi thanh toán thành công)
    void deleteByIdUser_Id(Long userId);
}