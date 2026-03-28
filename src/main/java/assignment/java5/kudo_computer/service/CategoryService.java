package assignment.java5.kudo_computer.service;

import assignment.java5.kudo_computer.entity.Category;
import java.util.List;

import org.springframework.data.domain.Page;

public interface CategoryService {
    List<Category> getAllActiveCategories();

    Category getCategoryBySlug(String slug);

    Page<Category> getCategoriesWithPaginationAndSearch(String keyword, int page, int size);

    Category saveCategory(Category category);

    Category getCategoryById(Long id);

    void deleteCategory(Long id);
}