package assignment.java5.kudo_computer.service;

import java.util.List;

import assignment.java5.kudo_computer.entity.Order;
import assignment.java5.kudo_computer.entity.OrderDetails;
import assignment.java5.kudo_computer.entity.User;

public interface OrderService {
    Order placeOrder(User user, String name, String phone, String address,
            String province, String district, String ward,
            String shippingUnit, Long shippingFee);

    List<Order> getOrderHistory(Long userId);

    Order getOrderById(Long orderId);

    List<OrderDetails> getOrderDetails(Long orderId);

    // Lấy tất cả đơn hàng cho Admin
    List<Order> getAllOrders();

    // Cập nhật trạng thái đơn hàng
    void updateOrderStatus(Long orderId, String status);
}