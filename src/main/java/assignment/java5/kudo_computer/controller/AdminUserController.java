package assignment.java5.kudo_computer.controller;

import assignment.java5.kudo_computer.entity.User;
import assignment.java5.kudo_computer.service.SessionService;
import assignment.java5.kudo_computer.service.UserService;
import assignment.java5.kudo_computer.repository.UserRepository; // Import thêm để hỗ trợ lưu nhanh
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository; // Dùng trực tiếp Repository để code ngắn gọn hơn cho hàm Save và FindById

    @Autowired
    private SessionService sessionService;

    // 1. Xem danh sách User (Có tìm kiếm và phân trang)
    @GetMapping
    public String listUsers(Model model,
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page) {

        int pageSize = 5;
        Page<User> userPage = userService.getUsersWithPaginationAndSearch(keyword, page, pageSize);

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("keyword", keyword);

        return "admin/user/list";
    }

    // 2. Khóa / Mở khóa tài khoản
    @GetMapping("/toggle-status/{id}")
    public String toggleStatus(@PathVariable("id") Long id, RedirectAttributes ra) {
        User currentUser = sessionService.get("currentUser", null);

        // Kiểm tra an toàn: Không cho phép Admin tự khóa chính mình
        if (currentUser != null && currentUser.getId().equals(id)) {
            ra.addFlashAttribute("error", "Bạn không thể tự khóa tài khoản đang đăng nhập!");
            return "redirect:/admin/users";
        }

        userService.toggleUserStatus(id);
        ra.addFlashAttribute("message", "Cập nhật trạng thái tài khoản thành công!");
        return "redirect:/admin/users";
    }

    // 3. Hiển thị form thêm mới
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("pageTitle", "Thêm Người Dùng Mới");
        return "admin/user/form";
    }

    // 4. Hiển thị form chỉnh sửa
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            ra.addFlashAttribute("error", "Không tìm thấy người dùng!");
            return "redirect:/admin/users";
        }
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Chỉnh Sửa Người Dùng (ID: " + id + ")");
        return "admin/user/form";
    }

    // 5. Lưu thông tin (Thêm mới / Cập nhật)
    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "newPassword", required = false) String newPassword,
            RedirectAttributes ra) {

        try {
            // Lấy thông tin user cũ từ DB nếu là thao tác Cập nhật
            User existingUser = null;
            if (user.getId() != null) {
                existingUser = userRepository.findById(user.getId()).orElse(null);
            }

            // Xử lý mật khẩu: Có nhập mới thì đổi, không thì giữ nguyên mật khẩu cũ
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                user.setPassword(newPassword);
            } else if (existingUser != null) {
                user.setPassword(existingUser.getPassword());
            }

            // Xử lý upload ảnh Avatar
            if (imageFile != null && !imageFile.isEmpty()) {
                String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
                user.setAvatar(fileName);

                // Đường dẫn lưu ảnh giống với Product
                String uploadDir = "src/main/resources/static/images/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                try (InputStream inputStream = imageFile.getInputStream()) {
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }
            } else {
                // Giữ nguyên ảnh cũ nếu không upload ảnh mới
                if (existingUser != null && existingUser.getAvatar() != null) {
                    user.setAvatar(existingUser.getAvatar());
                }
            }

            // Lưu vào Database
            userRepository.save(user);
            ra.addFlashAttribute("message", "Lưu thông tin người dùng thành công!");

        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi khi lưu người dùng: " + e.getMessage());
        }

        return "redirect:/admin/users";
    }
}