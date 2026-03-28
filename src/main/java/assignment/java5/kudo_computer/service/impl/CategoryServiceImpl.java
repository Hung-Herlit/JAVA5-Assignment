package assignment.java5.kudo_computer.service.impl;

import assignment.java5.kudo_computer.entity.Category;
import assignment.java5.kudo_computer.entity.Product;
import assignment.java5.kudo_computer.entity.ProductCategory;
import assignment.java5.kudo_computer.repository.CategoryRepository;
import assignment.java5.kudo_computer.repository.ProductCategoryRepository;
import assignment.java5.kudo_computer.repository.ProductRepository;
import assignment.java5.kudo_computer.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Nhúng thêm 2 Repository này để xử lý sản phẩm
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Category> getAllActiveCategories() {
        return categoryRepository.findByIsActiveTrue();
    }

    @Override
    public Category getCategoryBySlug(String slug) {
        return categoryRepository.findBySlugAndIsActiveTrue(slug).orElse(null);
    }

    @Override
    public Page<Category> getCategoriesWithPaginationAndSearch(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        if (keyword != null && !keyword.isEmpty()) {
            return categoryRepository.findByNameCategoryContainingIgnoreCase(keyword, pageable);
        }
        return categoryRepository.findAll(pageable);
    }

    @Override
    @Transactional // Thêm cái này để bảo vệ dữ liệu nếu có lỗi xảy ra giữa chừng
    public Category saveCategory(Category category) {

        // Kiểm tra xem đây là hành động Cập nhật (đã có ID) hay Thêm mới
        if (category.getId() != null) {
            // Lấy thông tin cũ của danh mục từ database lên để so sánh
            Category oldCategory = categoryRepository.findById(category.getId()).orElse(null);

            // Nếu danh mục tồn tại, trạng thái cũ là Tự do (true) và trạng thái mới bị tắt
            // đi (false)
            if (oldCategory != null && oldCategory.isActive() && !category.isActive()) {

                // Kích hoạt tính năng Xóa mềm dây chuyền giống y hệt hàm deleteCategory
                List<ProductCategory> productCategories = productCategoryRepository
                        .findByIdCategory_Id(category.getId());

                for (ProductCategory pc : productCategories) {
                    Product product = pc.getIdProduct();

                    if (product.isActive()) {
                        product.setActive(false);
                        productRepository.save(product);
                    }
                }
            }
        }

        // Sau khi xử lý xong các logic phụ, tiến hành lưu danh mục như bình thường
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional // Bắt buộc phải có để đảm bảo tính toàn vẹn dữ liệu
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category != null) {

            // 1. Ẩn Danh mục (Soft delete)
            category.setActive(false);
            categoryRepository.save(category);

            // 2. Tìm tất cả sản phẩm thuộc danh mục này thông qua bảng trung gian
            List<ProductCategory> productCategories = productCategoryRepository.findByIdCategory_Id(id);

            // 3. Lặp qua và ẩn toàn bộ các sản phẩm đó
            for (ProductCategory pc : productCategories) {
                Product product = pc.getIdProduct();

                // Chỉ lưu nếu sản phẩm đang hiển thị (tránh tốn tài nguyên update lại sản phẩm
                // đã ẩn)
                if (product.isActive()) {
                    product.setActive(false);
                    productRepository.save(product);
                }
            }
        }
    }
}