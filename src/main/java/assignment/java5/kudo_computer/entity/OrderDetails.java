package assignment.java5.kudo_computer.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Order_Details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_order", nullable = false)
    private Order idOrder;

    @ManyToOne
    @JoinColumn(name = "id_product", nullable = false)
    private Product idProduct;

    @PositiveOrZero(message = "Giá hiện tại phải >= 0")
    @Column(name = "current_price", nullable = false)
    private Long currentPrice;

    @Min(value = 0, message = "Giảm giá hiện tại không được âm")
    @Column(name = "current_discount")
    private Integer currentDiscount;

    @Positive(message = "Số lượng phải > 0")
    @Column(nullable = false)
    private Integer quantity;
}
