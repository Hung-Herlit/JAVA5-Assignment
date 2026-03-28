package assignment.java5.kudo_computer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import assignment.java5.kudo_computer.entity.Category;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByIsActiveTrue();

    Optional<Category> findBySlugAndIsActiveTrue(String slug);
    
    Page<Category> findByNameCategoryContainingIgnoreCase(String keyword, Pageable pageable);
}