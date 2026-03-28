package assignment.java5.kudo_computer.service.impl;

import assignment.java5.kudo_computer.entity.Product;
import assignment.java5.kudo_computer.repository.ProductRepository;
import assignment.java5.kudo_computer.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAllActiveProducts() {
        return productRepository.findByIsActiveTrue();
    }

    @Override
    public List<Product> getTop8HotProducts() {
        return productRepository.findTop8ByIsActiveTrueOrderByViewsDesc();
    }

    @Override
    public List<Product> getTop8DiscountProducts() {
        return productRepository.findTop8ByIsActiveTrueOrderByDiscountDesc();
    }

    @Override
    public List<Product> getTop8NewProducts() {
        return productRepository.findTop8ByIsActiveTrueOrderByIdDesc();
    }

    @Override
    public List<Product> getProductsByCompany(Long companyId) {
        return productRepository.findByIdCompany_IdAndIsActiveTrue(companyId);
    }

    @Override
    public Product getProductBySlug(String slug) {
        return productRepository.findBySlugAndIsActiveTrue(slug).orElse(null);
    }

    @Override
    public List<Product> getRandomProducts() {
        return productRepository.findRandomProducts();
    }

    @Override
    public List<Product> getAllHotProductsAll() {
        return productRepository.findByIsActiveTrueOrderByViewsDesc();
    }

    @Override
    public List<Product> getAllNewProductsAll() {
        return productRepository.findByIsActiveTrueOrderByIdDesc();
    }

    @Override
    public List<Product> getAllDiscountProductsAll() {
        return productRepository.findByIsActiveTrueOrderByDiscountDesc();
    }

    @Override
    public List<Product> getProductsByCategorySlug(String slug) {
        return productRepository.findProductsByCategorySlug(slug);
    }

    @Override
    public Page<Product> getProductsWithPaginationAndSearch(String keyword, int page,
            int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page - 1,
                size);
        if (keyword != null && !keyword.isEmpty()) {
            return productRepository.findByNameProductContainingIgnoreCase(keyword, pageable);
        }
        return productRepository.findAll(pageable);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            product.setActive(false); // Xóa mềm (Soft Delete)
            productRepository.save(product);
        }
    }

    @Override
    public List<Product> searchProducts(String keywords) {
        return productRepository.findByNameProductContainingIgnoreCase(keywords);
    }
}