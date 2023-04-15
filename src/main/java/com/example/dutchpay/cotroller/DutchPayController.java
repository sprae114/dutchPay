package com.example.dutchpay.cotroller;

import com.example.dutchpay.domain.DutchResult;
import com.example.dutchpay.domain.UserAccount;
import com.example.dutchpay.dto.*;
import com.example.dutchpay.repository.DutchResultRepository;
import com.example.dutchpay.repository.UserAccountRepository;
import com.example.dutchpay.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.dutchpay.service.DutchPayService.dutchpayTotal;
import static com.example.dutchpay.service.FriendService.friendSelectListAfter;
import static com.example.dutchpay.service.InMemoryDutchPayService.dutchpayDb;


@Controller
@RequestMapping(path = "/dutch")
@RequiredArgsConstructor
public class DutchPayController {

    private final DutchPayService dutchPayService;
    private final DutchResultRepository dutchResultRepository;

    private final UserAccountService userAccountService;


    public static List<Long> dutchpayMain = new ArrayList<>();

    @GetMapping
    public String dutchHome(@AuthenticationPrincipal LoginPrincipal loginPrincipal, Model model) {

        model.addAttribute("name", loginPrincipal.getName());

        return "dutchHome";
    }


    @GetMapping("/dutchPayList")
    public String dutchResult(Model model) {
        model.addAttribute("dutchList", dutchpayMain);
        return "cal/totalcalculrate";
    }

    @PostMapping("/dutchPayList/{id}")
    public String dutchResultDelete(@PathVariable int id) {
        dutchpayDb.remove(id);
        dutchpayMain.remove(id);

        return "redirect:/dutch/dutchPayList";
    }


    @GetMapping("/dutchPayListClear")
    public String dutchClear() {
        dutchPayService.dutchinit();
        return "redirect:/dutch/dutchPayList";
    }


    @GetMapping("/dutchPayList/noAlcohol")
    public String dutchResultNoAlcohol(Model model) {
        FreindsDutchpayNoAlcholDto freindsDutchpayNoAlcholDto = new FreindsDutchpayNoAlcholDto();

        friendSelectListAfter.forEach(friend -> {
            freindsDutchpayNoAlcholDto.addDutchpayAlchol(new DutchpayNoAlchol(friend.getName()));
        });

        model.addAttribute("form", freindsDutchpayNoAlcholDto);

        return "cal/calculraterNoAlcoholDetail";
    }

    @PostMapping("/dutchPayList/noAlcohol")
    public String dutchResultNoAlcoholSubmit(@ModelAttribute FreindsDutchpayNoAlcholDto form, Model model) {
        form.calculate();

        return "redirect:/dutch/dutchPayList";
    }


    @GetMapping("/dutchPayList/alcohol")
    public String dutchResultAlcohol(Model model) {
        FreindsDutchpayDto freindsDutchpayDto = new FreindsDutchpayDto();

        friendSelectListAfter.forEach(friend -> {
            freindsDutchpayDto.addDutchpay(new Dutchpay(friend.getName()));
        });

        model.addAttribute("form", freindsDutchpayDto);

        return "cal/calculaterDetail";
    }

    @PostMapping("/dutchPayList/alcohol")
    public String dutchResultAlcoholSubmit(@ModelAttribute FreindsDutchpayDto form, Model model) {
        form.calculate();

        return "redirect:/dutch/dutchPayList";
    }


    @GetMapping("/dutchResult")
    public String dutchResultDetail(Model model) {
        String totalMoney = dutchPayService.calculateDutchPayMoney();

        List<String> dutchPayResult = dutchPayService.minTransfers(dutchpayTotal,
                friendSelectListAfter.stream().map(FriendSelectSaveDto::getName).collect(Collectors.toList()));

        model.addAttribute("result", dutchPayService.printDutchPay(totalMoney, dutchPayResult));
        return "cal/calculraterResult";
    }

    @PostMapping("/dutchResult")
    public String saveDutchResult(@ModelAttribute String result) {
        return "redirect:/dutch";
    }

    @PostMapping("/recordDutchResult")
    public String recordDutchResult(@AuthenticationPrincipal LoginPrincipal loginPrincipal,
                                    @RequestParam String recordDutch) {
        UserAccountDto userAccountDto = userAccountService.searchUser(loginPrincipal.getId()).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 없습니다."));

        dutchResultRepository.save(new DutchResult(recordDutch, userAccountDto.toEntity()));

        return "redirect:/dutch";
    }
}
