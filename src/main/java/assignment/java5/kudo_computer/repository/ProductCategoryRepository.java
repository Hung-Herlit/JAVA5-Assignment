package assignment.java5.kudo_computer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import assignment.java5.kudo_computer.entity.ProductCategory;
import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    // Lấy tất cả sản phẩm thuộc một danh mục
    List<ProductCategory> findByIdCategory_Id(Long categoryId);

    // Lấy tất cả danh mục của một sản phẩm
    List<ProductCategory> findByIdProduct_Id(Long productId);
}