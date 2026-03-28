package assignment.java5.kudo_computer.controller;

import assignment.java5.kudo_computer.entity.User;
import assignment.java5.kudo_computer.service.SessionService;
import assignment.java5.kudo_computer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "redirect", required = false) String redirect) {
        if (redirect != null && !redirect.isEmpty()) {
            sessionService.set("redirectUrl", redirect);
        }
        return "views/user/login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model) {
        User user = userService.login(username, password);
        if (user != null) {
            if (!user.isActive()) {
                model.addAttribute("error", "Tài khoản của bạn đã bị khóa!");
                return "views/user/login";
            }

            sessionService.set("currentUser", user);

            // LẤY URL CŨ ĐÃ LƯU TỪ FILTER/HEADER
            String redirectUrl = sessionService.get("redirectUrl", "/home");
            sessionService.remove("redirectUrl");

            return "redirect:" + redirectUrl;
        }
        model.addAttribute("error", "Tài khoản hoặc mật khẩu không chính xác!");
        return "views/user/login";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "/views/user/register";
    }

    @PostMapping("/register")
    public String processRegister(@RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("gender") String genderStr,
            @RequestParam(value = "birthday", required = false) String birthdayStr,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        // 1. Kiểm tra logic (Mật khẩu, trùng Email/Phone) - Giữ nguyên của bạn
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "views/user/register";
        }
        if (userService.checkEmailExists(email)) {
            model.addAttribute("error", "Email đã được sử dụng!");
            return "views/user/register";
        }

        // 2. Tạo đối tượng User mới
        User newUser = new User();
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setPhoneNumber(phone);
        newUser.setPassword(password);
        newUser.setGender("male".equalsIgnoreCase(genderStr));
        if (birthdayStr != null && !birthdayStr.isEmpty()) {
            newUser.setBirthday(Date.valueOf(birthdayStr));
        }

        // 3. Lưu vào Database
        User savedUser = userService.register(newUser);

        // 4. XỬ LÝ TỰ ĐỘNG ĐĂNG NHẬP
        if (savedUser != null) {
            // Lưu vào session để hệ thống nhận diện đã đăng nhập
            sessionService.set("currentUser", savedUser);

            // Lấy lại trang cũ người dùng đang xem trước đó (nếu có)
            String redirectUrl = sessionService.get("redirectUrl", "/home");
            sessionService.remove("redirectUrl"); // Xóa sau khi dùng xong

            return "redirect:" + redirectUrl;
        }

        model.addAttribute("error", "Đăng ký thất bại, vui lòng thử lại!");
        return "views/user/register";
    }

    @GetMapping("/logout")
    public String logout() {
        sessionService.remove("currentUser");
        return "redirect:/login";
    }
}