package assignment.java5.kudo_computer.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên danh mục không được để trống")
    @Column(name = "nameCategory", nullable = false)
    private String nameCategory;

    @NotBlank(message = "Slug không được để trống")
    @Column(nullable = false)
    private String slug;

    @Builder.Default
    @Column(name = "is_Active", nullable = false)
    private boolean isActive = false;
}
