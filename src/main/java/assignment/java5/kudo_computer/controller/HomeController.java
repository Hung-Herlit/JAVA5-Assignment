package assignment.java5.kudo_computer.controller;

import assignment.java5.kudo_computer.service.CategoryService;
import assignment.java5.kudo_computer.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping({ "/", "/home", "/index" })
    public String home(Model model) {
        model.addAttribute("hotProducts", productService.getTop8HotProducts());
        model.addAttribute("newProducts", productService.getTop8NewProducts());
        model.addAttribute("discountProducts", productService.getTop8DiscountProducts());
        model.addAttribute("categories", categoryService.getAllActiveCategories());
        return "index";
    }
}