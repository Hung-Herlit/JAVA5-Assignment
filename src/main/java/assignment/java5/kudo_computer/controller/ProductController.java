package assignment.java5.kudo_computer.controller;

import assignment.java5.kudo_computer.entity.Category;
import assignment.java5.kudo_computer.entity.Product;
import assignment.java5.kudo_computer.service.CategoryService;
import assignment.java5.kudo_computer.service.ProductService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/product/{slug}")
    public String productDetail(@PathVariable("slug") String slug, Model model) {
        Product product = productService.getProductBySlug(slug);

        if (product == null) {
            return "redirect:/home";
        }
        int currentViews = (product.getViews() == null) ? 0 : product.getViews();
        // Cộng thêm 1
        product.setViews(currentViews + 1);
        // Lưu lại vào database thông qua service
        productService.saveProduct(product);

        // Truyền dữ liệu ra View
        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", productService.getProductsByCompany(product.getIdCompany().getId()));
        model.addAttribute("randomProducts", productService.getRandomProducts());

        return "views/detail";
    }

    // Xử lý nút "Xem tất cả" của các bộ sưu tập (hot, new, discount)
    @GetMapping("/collection/{type}")
    public String collectionProducts(@PathVariable("type") String type, Model model) {
        List<Product> products;
        String title = "";
        
        switch (type) {
            case "hot":
                products = productService.getTop8HotProducts();
                title = "8 SẢN PHẨM HOT NHẤT";
                break;
            case "new":
                products = productService.getTop8NewProducts();
                title = "8 SẢN PHẨM MỚI VỀ";
                break;
            case "discount":
                products = productService.getTop8DiscountProducts();
                title = "SIÊU SALE TRONG NGÀY";
                break;
            default:
                return "redirect:/home";
        }
        
        model.addAttribute("pageTitle", title);
        model.addAttribute("products", products);
        return "views/products"; // Trỏ đến file giao diện sắp tạo
    }

    // Xử lý khi bấm vào 1 Danh mục sản phẩm
    @GetMapping("/category/{slug}")
    public String categoryProducts(@PathVariable("slug") String slug, Model model) {
        Category category = categoryService.getCategoryBySlug(slug);
        
        if (category == null) {
            return "redirect:/home";
        }

        List<Product> products = productService.getProductsByCategorySlug(slug);
        model.addAttribute("pageTitle", "DANH MỤC: " + category.getNameCategory().toUpperCase());
        model.addAttribute("products", products);
        
        return "views/products";
    }

    @GetMapping("/search")
    public String search(@RequestParam("keywords") String keywords, Model model) {
        List<Product> products = productService.searchProducts(keywords);
    
        model.addAttribute("products", products);
        model.addAttribute("keywords", keywords); // Giữ lại từ khóa trên ô input
        model.addAttribute("pageTitle", "KẾT QUẢ TÌM KIẾM: " + keywords.toUpperCase());
    
        return "views/products"; // Dùng chung giao diện với trang danh mục
    }
}