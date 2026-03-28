package assignment.java5.kudo_computer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import assignment.java5.kudo_computer.entity.Company;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByIsActiveTrue();

    Optional<Company> findBySlugAndIsActiveTrue(String slug);
}