package assignment.java5.kudo_computer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class demoController {

    @GetMapping("/demo")
    public String demoPage(Model model) {
        return "index";
    }
}
