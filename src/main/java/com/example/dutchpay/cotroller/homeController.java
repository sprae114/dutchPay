package com.example.dutchpay.cotroller;

import com.example.dutchpay.domain.DutchResult;
import com.example.dutchpay.domain.UserAccount;
import com.example.dutchpay.service.DutchResultService;
import com.example.dutchpay.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class homeController {

    private final DutchResultService dutchResultService;
    private final UserAccountService userService;

    @Autowired
    public homeController(DutchResultService dutchResultService, UserAccountService userService) {
        this.dutchResultService = dutchResultService;
        this.userService = userService;
    }


    @RequestMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/main")
    public String main(@AuthenticationPrincipal OAuth2User principal, Model model) {
        userService.saveUser(principal);

        return "redirect:/dutch";
    }

    @GetMapping("/previousCalculations")
    public String previousCalculations(@AuthenticationPrincipal OAuth2User principal,
                                       Model model) {

        List<DutchResult> dutchResultList = dutchResultService.getDutchResult(principal);
        model.addAttribute("dutchResultList", dutchResultList);
        
        return "previousCalculations";
    }
}
