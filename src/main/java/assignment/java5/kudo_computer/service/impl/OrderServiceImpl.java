package assignment.java5.kudo_computer.service.impl;

import assignment.java5.kudo_computer.entity.*;
import assignment.java5.kudo_computer.repository.*;
import assignment.java5.kudo_computer.service.CartService;
import assignment.java5.kudo_computer.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CartService cartService;

    @Override
    @Transactional // Đảm bảo nếu lỗi ở bước nào thì toàn bộ quá trình sẽ bị hủy
    public Order placeOrder(User user, String name, String phone, String address,
            String province, String district, String ward,
            String shippingUnit, Long shippingFee) {

        // 1. Lấy giỏ hàng
        List<Cart> cartItems = cartService.getCartByUser(user.getId());
        if (cartItems.isEmpty()) {
            return null; // Không có gì để thanh toán
        }

        // 2. Lưu địa chỉ vào sổ địa chỉ (Address)
        Address newAddress = new Address();
        newAddress.setUser(user);
        newAddress.setName(name);
        newAddress.setPhoneNumber(phone);
        newAddress.setProvince(province);
        newAddress.setDistrict(district);
        newAddress.setWard(ward);
        newAddress.setAddress(address);
        addressRepository.save(newAddress);

        // 3. Tạo Đơn hàng (Order)
        long totalCartValue = cartService.calculateTotal(user.getId());
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new java.sql.Date(System.currentTimeMillis()));
        order.setStatus("Đang chờ duyệt");
        order.setShippingUnit(shippingUnit);
        order.setShippingFee(shippingFee);
        order.setAllDiscount(0L); // Demo nên để giảm giá toàn đơn = 0
        order.setTotal(totalCartValue + shippingFee);

        // Nối chuỗi thông tin giao hàng
        String deliveryInfo = name + " | " + phone + " | " + address + ", " + ward + ", " + district + ", " + province;
        order.setDeliveryInfo(deliveryInfo);

        Order savedOrder = orderRepository.save(order);

        // 4. Tạo Chi tiết đơn hàng (Order_Details) từ Giỏ hàng
        for (Cart item : cartItems) {
            Product p = item.getIdProduct();

            if (p.getStock() < item.getQuantity()) {
                throw new RuntimeException("Sản phẩm " + p.getNameProduct() + " không đủ số lượng trong kho!");
            }

            OrderDetails details = new OrderDetails();
            details.setIdOrder(savedOrder);
            details.setIdProduct(p);
            details.setQuantity(item.getQuantity());
            details.setCurrentPrice(p.getPrice());
            details.setCurrentDiscount(p.getDiscount());

            orderDetailsRepository.save(details);

            p.setStock(p.getStock() - item.getQuantity());
            productRepository.save(p);
        }

        // 5. Xóa giỏ hàng sau khi đặt thành công
        cartService.clearCart(user.getId());

        return savedOrder;
    }

    @Override
    public List<Order> getOrderHistory(Long userId) {
        return orderRepository.findByUser_IdOrderByOrderDateDesc(userId);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    public List<OrderDetails> getOrderDetails(Long orderId) {
        return orderDetailsRepository.findByIdOrder_Id(orderId);
    }

    @Override
    public List<Order> getAllOrders() {
        // Lấy tất cả đơn hàng, sắp xếp từ mới nhất đến cũ nhất
        return orderRepository.findAll(org.springframework.data.domain.Sort
                .by(org.springframework.data.domain.Sort.Direction.DESC, "orderDate"));
    }

    @Override
    public void updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
        }
    }
}