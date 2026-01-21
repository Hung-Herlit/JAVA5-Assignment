package assignment.java5.kudo_computer.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Ngày đặt hàng không được để trống")
    @Column(name = "order_date", nullable = false)
    private java.sql.Date orderDate;

    @NotBlank(message = "Trạng thái đơn hàng không được để trống")
    @Column(nullable = false)
    private String status;

    @PositiveOrZero(message = "Tổng tiền phải >= 0")
    @Column(nullable = false)
    private Long total;

    @NotBlank(message = "Thông tin giao hàng không được để trống")
    @Column(name = "delivery_info", nullable = false)
    private String deliveryInfo;

    @NotBlank(message = "Đơn vị vận chuyển không được để trống")
    @Column(name = "shipping_unit", nullable = false)
    private String shippingUnit;

    @PositiveOrZero(message = "Tổng giảm giá phải >= 0")
    @Column(name = "all_discount", nullable = false)
    private Long allDiscount;

    @PositiveOrZero(message = "Phí vận chuyển phải >= 0")
    @Column(name = "shipping_fee", nullable = false)
    private Long shippingFee;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;
}
