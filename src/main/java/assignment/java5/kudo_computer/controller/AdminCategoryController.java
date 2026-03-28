package assignment.java5.kudo_computer.controller;

import assignment.java5.kudo_computer.entity.Category;
import assignment.java5.kudo_computer.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    // Xem danh sách (Có tìm kiếm và phân trang)
    @GetMapping
    public String listCategories(Model model,
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        int pageSize = 5; // Số lượng hiển thị trên 1 trang
        Page<Category> categoryPage = categoryService.getCategoriesWithPaginationAndSearch(keyword, page, pageSize);

        model.addAttribute("categories", categoryPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categoryPage.getTotalPages());
        model.addAttribute("keyword", keyword);

        return "admin/category/list";
    }

    // Hiển thị Form Thêm mới
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("pageTitle", "Thêm Danh Mục Mới");
        return "admin/category/form";
    }

    // Xử lý Lưu (Thêm mới / Cập nhật)
    @PostMapping("/save")
    public String saveCategory(@ModelAttribute("category") Category category, RedirectAttributes ra) {
        categoryService.saveCategory(category);
        ra.addFlashAttribute("message", "Đã lưu danh mục thành công!");
        return "redirect:/admin/categories";
    }

    // Hiển thị Form Chỉnh sửa
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
        Category category = categoryService.getCategoryById(id);
        if (category == null) {
            ra.addFlashAttribute("error", "Không tìm thấy danh mục!");
            return "redirect:/admin/categories";
        }
        model.addAttribute("category", category);
        model.addAttribute("pageTitle", "Chỉnh Sửa Danh Mục (ID: " + id + ")");
        return "admin/category/form";
    }

    // Xử lý Xóa
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            categoryService.deleteCategory(id);
            ra.addFlashAttribute("message", "Đã xóa danh mục thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Không thể xóa do danh mục này đang có sản phẩm!");
        }
        return "redirect:/admin/categories";
    }
}