package assignment.java5.kudo_computer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
public class UploadFileController {

    // 1. Hiển thị form test upload
    @GetMapping("/test-upload")
    public String showUploadForm() {
        return "views/test-upload"; // Trỏ tới file HTML bên dưới
    }

    // 2. Xử lý file gửi lên
    @PostMapping("/upload-file")
    @ResponseBody // Trả về chữ trực tiếp lên màn hình để test cho nhanh
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "❌ Lỗi: Bạn chưa chọn file nào!";
        }

        try {
            // Lấy tên file gốc
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            // Chỉ định thư mục lưu (giống với thư mục ảnh product)
            String uploadDir = "src/main/resources/static/images/";
            Path uploadPath = Paths.get(uploadDir);

            // Tạo thư mục nếu chưa tồn tại
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Copy file vào thư mục
            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            return "✅ Thành công! Đã tải lên và lưu file: " + fileName;

        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Lỗi khi lưu file: " + e.getMessage();
        }
    }
}