package com.example.dutchpay.cotroller;

import com.example.dutchpay.domain.DutchResult;
import com.example.dutchpay.dto.LoginPrincipal;
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
public class HomeController {

    private final DutchResultService dutchResultService;
    private final UserAccountService userService;

    @Autowired
    public HomeController(DutchResultService dutchResultService, UserAccountService userService) {
        this.dutchResultService = dutchResultService;
        this.userService = userService;
    }


    @RequestMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/main")
    public String main(@AuthenticationPrincipal LoginPrincipal loginPrincipal, Model model) {
        userService.saveUser(loginPrincipal.getId(), loginPrincipal.getName(), loginPrincipal.getEmail());

        return "redirect:/dutch";
    }

    @GetMapping("/previousCalculations")
    public String previousCalculations(@AuthenticationPrincipal LoginPrincipal loginPrincipal,
                                       Model model) {

        List<DutchResult> dutchResultList = dutchResultService.getDutchResult(loginPrincipal);
        model.addAttribute("dutchResultList", dutchResultList);

        return "previousCalculations";
    }
}
