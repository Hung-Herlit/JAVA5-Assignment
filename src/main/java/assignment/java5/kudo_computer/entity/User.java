package assignment.java5.kudo_computer.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "User")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(\\+84|0)\\d{9,10}$", message = "Số điện thoại không hợp lệ")
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @NotBlank(message = "Họ tên không được để trống")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Column(name = "password", nullable = false)
    private String password;

    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email không được để trống")
    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String avatar;

    @Builder.Default
    @Column(nullable = false)
    private boolean gender = false; // bit default 0 trong DB

    @Column
    private Date birthday;

    @Builder.Default
    @Column(name = "is_Active", nullable = false)
    private boolean isActive = false;

    @Builder.Default
    @Column(name = "is_Admin", nullable = false)
    private boolean isAdmin = false;
}
