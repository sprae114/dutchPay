package com.example.dutchpay.controller;

import com.example.dutchpay.domain.DutchResult;
import com.example.dutchpay.dto.*;
import com.example.dutchpay.exception.CodeError;
import com.example.dutchpay.exception.UserNotFoundException;
import com.example.dutchpay.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static com.example.dutchpay.service.DutchPayService.dutchpayTotal;
import static com.example.dutchpay.service.FriendService.friendSelectListAfter;
import static com.example.dutchpay.service.InMemoryDutchPayService.dutchpayDb;


@Controller
@RequestMapping(path = "/dutch")
@RequiredArgsConstructor
public class DutchPayController {

    private final DutchPayService dutchPayService;

    private final DutchResultService dutchResultService;

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
    public String dutchResultNoAlcoholSubmit(@ModelAttribute FreindsDutchpayNoAlcholDto form) {
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
    public String dutchResultAlcoholSubmit(@ModelAttribute FreindsDutchpayDto form) {
        form.calculate();

        return "redirect:/dutch/dutchPayList";
    }


    @GetMapping("/dutchResult")
    public String dutchResultDetail(Model model) {
        String totalMoney = dutchPayService.calculateDutchPayMoney();

        List<String> dutchPayResult = DutchPayService.minTransfers(dutchpayTotal,
                friendSelectListAfter.stream().map(FriendSelectSaveDto::getName).collect(Collectors.toList()));

        model.addAttribute("result", dutchPayService.printDutchPay(totalMoney, dutchPayResult));
        return "cal/calculraterResult";
    }


    @PostMapping("/recordDutchResult")
    public String recordDutchResult(@AuthenticationPrincipal LoginPrincipal loginPrincipal,
                                    @RequestParam String recordDutch) {
        UserAccountDto userAccountDto = userAccountService.searchUser(loginPrincipal.getId())
                .orElseThrow(() -> new UserNotFoundException(CodeError.NOT_FOUND_USER, loginPrincipal.getId().toString()));

        DutchResult dutchResult = new DutchResult(recordDutch, userAccountDto.toEntity(), makeNames(), LocalDate.now().toString());

        dutchResultService.saveDutchResult(dutchResult);

        return "redirect:/dutch";
    }

    private String makeNames() {
        StringJoiner sj = new StringJoiner(",");
        for (String name : friendSelectListAfter.stream().map(FriendSelectSaveDto::getName).collect(Collectors.toList())) {
            sj.add(name);
        }

        return sj.toString();
    }

}
