package assignment.java5.kudo_computer.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String nameProduct;

    @Positive(message = "Giá sản phẩm phải là số dương")
    @Column(nullable = false)
    private Long price;

    @NotBlank(message = "Giảm giá không được để trống")
    @Min(value = 0, message = "Giảm giá không được âm") 
    @Max(value = 100, message = "Giảm giá không vượt quá 100%")
    @Column(nullable = false)
    private Integer discount;

    @DecimalMin(value = "0.0", message = "Thuế phải >= 0") 
    @Column(nullable = false) 
    private Double tax;

    @Column(nullable = false)
    @NotBlank(message = "Lượng hàng không được để trống")
    private Integer stock;

    @Column
    @Builder.Default
    private String picture = "product_default.png";

    @Column(nullable = false)
    @NotBlank(message = "Thông số không được để trống")
    private String specifications;

    @Column(nullable = false)
    @NotBlank(message = "Slug không được để trống")
    private String slug;

    @Column
    @PositiveOrZero(message = "Lượt xem phải >= 0")
    @Builder.Default
    private Integer views = 0;

    @Builder.Default
    @Column(name = "is_Active", nullable = false)
    private boolean isActive = false; 

    @ManyToOne 
    @JoinColumn(name = "id_company", nullable = false) 
    private Company idCompany; 

}
