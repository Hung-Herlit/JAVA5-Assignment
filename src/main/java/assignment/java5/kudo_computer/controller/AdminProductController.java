package assignment.java5.kudo_computer.controller;

import assignment.java5.kudo_computer.entity.Product;
import assignment.java5.kudo_computer.repository.CompanyRepository;
import assignment.java5.kudo_computer.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CompanyRepository companyRepository; // Dùng để đổ dữ liệu vào Select Box

    @GetMapping
    public String listProducts(Model model,
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        int pageSize = 5;
        Page<Product> productPage = productService.getProductsWithPaginationAndSearch(keyword, page, pageSize);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("keyword", keyword);

        return "admin/product/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("companies", companyRepository.findAll()); // Lấy danh sách Hãng
        model.addAttribute("pageTitle", "Thêm Sản Phẩm Mới");
        return "admin/product/form";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            RedirectAttributes ra) {
        try {
            // Thêm điều kiện kiểm tra imageFile != null
            if (imageFile != null && !imageFile.isEmpty()) {
                String fileName = org.springframework.util.StringUtils.cleanPath(imageFile.getOriginalFilename());
                product.setPicture(fileName);
                
                String uploadDir = "kudo-computer/src/main/resources/static/images/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                try (InputStream inputStream = imageFile.getInputStream()) {
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

                }
            } else {
                // Xử lý khi KHÔNG CHỌN ẢNH (giữ ảnh cũ hoặc gán ảnh mặc định)
                if (product.getId() != null) {
                    Product existingProduct = productService.getProductsWithPaginationAndSearch("", 1, 9999)
                            .getContent()
                            .stream().filter(p -> p.getId().equals(product.getId())).findFirst().orElse(null);

                    if (existingProduct != null && existingProduct.getPicture() != null) {
                        product.setPicture(existingProduct.getPicture());
                    } else if (product.getPicture() == null || product.getPicture().isEmpty()) {
                        product.setPicture("product_default.png");
                    }
                } else {
                    product.setPicture("product_default.png");
                }
            }

            productService.saveProduct(product);
            ra.addFlashAttribute("message", "Lưu sản phẩm thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi khi lưu sản phẩm: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
        // Vì ProductService chưa có getProductById(Long id), ta thêm nhanh
        Product product = productService.getProductsWithPaginationAndSearch("", 1, 9999).getContent()
                .stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);

        if (product == null) {
            ra.addFlashAttribute("error", "Không tìm thấy sản phẩm!");
            return "redirect:/admin/products";
        }
        model.addAttribute("product", product);
        model.addAttribute("companies", companyRepository.findAll());
        model.addAttribute("pageTitle", "Chỉnh Sửa Sản Phẩm (ID: " + id + ")");
        return "admin/product/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, RedirectAttributes ra) {
        productService.deleteProduct(id);
        ra.addFlashAttribute("message", "Đã đưa sản phẩm vào trạng thái Ẩn!");
        return "redirect:/admin/products";
    }
}