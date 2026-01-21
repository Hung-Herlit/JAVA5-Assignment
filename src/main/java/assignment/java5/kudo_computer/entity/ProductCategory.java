package assignment.java5.kudo_computer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Product_Category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_product", nullable = false)
    private Product idProduct;

    @ManyToOne
    @JoinColumn(name = "id_category", nullable = false)
    private Category idCategory;

    @Column(nullable = false)
    private Integer priority;
}
