package assignment.java5.kudo_computer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import assignment.java5.kudo_computer.entity.Order;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Lấy lịch sử mua hàng của 1 user (sắp xếp từ mới nhất đến cũ nhất)
    List<Order> findByUser_IdOrderByOrderDateDesc(Long userId);

    // (Dành cho Admin) Lọc đơn hàng theo trạng thái (Đang chờ, Đang giao, Đã
    // hủy...)
    List<Order> findByStatus(String status);

    // Tính tổng doanh thu từ các đơn hàng đã 'Hoàn thành'
    @Query("SELECT COALESCE(SUM(o.total), 0) FROM Order o WHERE o.status = 'Hoàn thành'")
    Long calculateTotalRevenue();

    // Lấy 5 đơn hàng mới nhất cho trang chủ Admin
    List<Order> findTop5ByOrderByOrderDateDesc();
}