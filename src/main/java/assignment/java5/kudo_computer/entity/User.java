package assignment.java5.kudo_computer.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private long id;
    private String phone_number;
    private String fullname;
    private String password;
    private String avatar;
    private boolean gender;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date birthday;
    private String email;
    @Builder.Default
    private boolean is_active = true;
    @Builder.Default
    private boolean is_admin = false;
}