package com.example.dutchpay.cotroller;

import com.example.dutchpay.domain.DutchResult;
import com.example.dutchpay.service.DutchResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class homeController {

    private final DutchResultService dutchResultService;

    @Autowired
    public homeController(DutchResultService dutchResultService) {
        this.dutchResultService = dutchResultService;
    }

    @RequestMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/previousCalculations")
    public String previousCalculations(Model model) {
        List<DutchResult> dutchResultList = dutchResultService.getDutchResult();
        model.addAttribute("dutchResultList", dutchResultList);
        return "previousCalculations";
    }
}
