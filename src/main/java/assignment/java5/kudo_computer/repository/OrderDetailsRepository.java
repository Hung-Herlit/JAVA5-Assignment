package assignment.java5.kudo_computer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import assignment.java5.kudo_computer.entity.OrderDetails;
import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    // Lấy danh sách chi tiết của 1 đơn hàng
    List<OrderDetails> findByIdOrder_Id(Long orderId);
}