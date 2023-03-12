package com.example.dutchpay.cotroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class homeController {

    @RequestMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/previousCalculations")
    public String previousCalculations() {
        return "previousCalculations";
    }
}
