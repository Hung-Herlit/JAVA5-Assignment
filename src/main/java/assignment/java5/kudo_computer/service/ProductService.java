package assignment.java5.kudo_computer.service;

import assignment.java5.kudo_computer.entity.Product;
import java.util.List;

import org.springframework.data.domain.Page;

public interface ProductService {
    List<Product> getAllActiveProducts();

    List<Product> getTop8HotProducts();

    List<Product> getTop8DiscountProducts();

    List<Product> getTop8NewProducts();

    List<Product> getProductsByCompany(Long companyId);

    Product getProductBySlug(String slug);

    List<Product> getRandomProducts();

    List<Product> getAllHotProductsAll();

    List<Product> getAllNewProductsAll();

    List<Product> getAllDiscountProductsAll();

    List<Product> getProductsByCategorySlug(String slug);

    Page<Product> getProductsWithPaginationAndSearch(String keyword, int page, int size);

    Product saveProduct(Product product);

    void deleteProduct(Long id);
    
    List<Product> searchProducts(String keywords);
}