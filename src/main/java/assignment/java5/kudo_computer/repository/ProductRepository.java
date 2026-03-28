package assignment.java5.kudo_computer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import assignment.java5.kudo_computer.entity.Product;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Chỉ lấy các sản phẩm đang được bán
    List<Product> findByIsActiveTrue();

    // Tìm chi tiết sản phẩm bằng đường dẫn (slug)
    Optional<Product> findBySlugAndIsActiveTrue(String slug);

    // Tìm kiếm sản phẩm theo tên (không phân biệt hoa thường)
    List<Product> findByNameProductContainingIgnoreCaseAndIsActiveTrue(String keyword);

    // Tìm sản phẩm theo hãng (Company)
    List<Product> findByIdCompany_IdAndIsActiveTrue(Long companyId);

    // Lấy top 8 sản phẩm xem nhiều nhất (Sản phẩm hot)
    List<Product> findTop8ByIsActiveTrueOrderByViewsDesc();

    // Lấy top 8 sản phẩm giảm giá nhiều nhất (Flash Sale)
    List<Product> findTop8ByIsActiveTrueOrderByDiscountDesc();

    // Lấy top 8 sản phẩm mới nhất (dựa vào ID giảm dần)
    List<Product> findTop8ByIsActiveTrueOrderByIdDesc();

    // Lấy ngẫu nhiên 4 sản phẩm đang hoạt động
    @Query(value = "SELECT TOP 4 * FROM Products WHERE is_active = 1 ORDER BY NEWID()", nativeQuery = true)
    List<Product> findRandomProducts();

    // Lấy TẤT CẢ sản phẩm Hot, Mới, Sale (không dùng Top 8 nữa)
    List<Product> findByIsActiveTrueOrderByViewsDesc();

    List<Product> findByIsActiveTrueOrderByIdDesc();

    List<Product> findByIsActiveTrueOrderByDiscountDesc();

    // Dùng JPQL để lấy danh sách sản phẩm thông qua bảng trung gian ProductCategory
    @Query("SELECT pc.idProduct FROM ProductCategory pc WHERE pc.idCategory.slug = :slug AND pc.idProduct.isActive = true")
    List<Product> findProductsByCategorySlug(@org.springframework.data.repository.query.Param("slug") String slug);

    Page<Product> findByNameProductContainingIgnoreCase(String keyword, org.springframework.data.domain.Pageable pageable);

    // Trong ProductRepository.java
    List<Product> findByNameProductContainingIgnoreCase(String name);
}