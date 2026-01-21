package assignment.java5.kudo_computer.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Company")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên công ty không được để trống")
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @NotBlank(message = "Slug không được để trống")
    @Column(nullable = false)
    private String slug;

    @Column(name = "is_Active", nullable = false)
    @Builder.Default
    private boolean isActive = false;
}
