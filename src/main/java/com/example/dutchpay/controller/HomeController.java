package com.example.dutchpay.controller;

import com.example.dutchpay.domain.DutchResult;
import com.example.dutchpay.domain.type.SearchType;
import com.example.dutchpay.dto.DutchResultDTO;
import com.example.dutchpay.dto.LoginPrincipal;
import com.example.dutchpay.service.DutchResultService;
import com.example.dutchpay.service.PaginationService;
import com.example.dutchpay.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    private final DutchResultService dutchResultService;
    private final UserAccountService userService;

    private final PaginationService paginationService;

    @Autowired
    public HomeController(DutchResultService dutchResultService,
                          UserAccountService userService,
                          PaginationService paginationService) {
        this.dutchResultService = dutchResultService;
        this.userService = userService;
        this.paginationService= paginationService;
    }

    @RequestMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/main")
    public String main(@AuthenticationPrincipal LoginPrincipal loginPrincipal) {
        userService.saveUser(loginPrincipal);

        return "redirect:/dutch";
    }

    @GetMapping("/previousCalculations")
    public String previousCalculations(@AuthenticationPrincipal LoginPrincipal loginPrincipal,
                                       @RequestParam(required = false) SearchType searchType,
                                       @RequestParam(required = false) String searchValue,
                                       @PageableDefault(size = 4) Pageable pageable,
                                       Model model) {

        Page<DutchResultDTO> dutchResultList = dutchResultService
                .searchDutchResult(loginPrincipal.getId(),
                                    searchType,
                                    searchValue,
                                    pageable);

        List<Integer> barNumbers = paginationService
                .getPaginationBarNumbers(pageable.getPageNumber(), dutchResultList.getTotalPages());

        model.addAttribute("dutchResultList", dutchResultList);
        model.addAttribute("paginationBarNumbers", barNumbers);

        return "previousCalculations";
    }
}
