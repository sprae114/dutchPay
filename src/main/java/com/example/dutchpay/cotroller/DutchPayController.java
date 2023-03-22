package com.example.dutchpay.cotroller;

import com.example.dutchpay.domain.Friend;
import com.example.dutchpay.dto.*;
import com.example.dutchpay.service.DutchResultService;
import com.example.dutchpay.service.FriendService;
import com.example.dutchpay.service.InMemoryDutchPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.dutchpay.service.DutchResultService.dutchpayTotal;
import static com.example.dutchpay.service.FriendService.friendSelectListAfter;
import static com.example.dutchpay.service.InMemoryDutchPayService.dutchpayDb;


@Controller
@RequestMapping(path = "/dutch")
@RequiredArgsConstructor
public class DutchPayController {

    private final FriendService friendService;
    private final DutchResultService dutchResultService;
    private final InMemoryDutchPayService inMemoryDutchPayService;

    public static List<Long> dutchpayMain = new ArrayList<>();

    @GetMapping
    public String dutchHome() {
        return "dutchHome";
    }


    @GetMapping("/dutchPayList")
    public String dutchResult(Model model) {
        model.addAttribute("dutchList", dutchpayMain);
        System.out.println(dutchpayDb);
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
        dutchResultService.dutchinit();
        return "redirect:/dutch/dutchPayList";
    }


    @GetMapping("/dutchPayList/noAlcohol")
    public String dutchResultNoAlcohol(Model model) {
        FreindsDutchpayNoAlcholDto freindsDutchpayNoAlcholDto = new FreindsDutchpayNoAlcholDto();

        friendSelectListAfter.forEach(friend -> {
            freindsDutchpayNoAlcholDto.addDutchpayAlchol(new DutchpayNoAlchol(friend.getName()));
        });

        System.out.println(">> freindsDutchpayNoAlcholDto " + freindsDutchpayNoAlcholDto);
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
        String totalMoney = dutchResultService.calculateDutchPayMoney();

        List<String> dutchPayResult = dutchResultService.minTransfers(dutchpayTotal,
                friendSelectListAfter.stream().map(FriendSelectSaveDto::getName).collect(Collectors.toList()));

        model.addAttribute("result", dutchResultService.printDutchPay(totalMoney, dutchPayResult));
        return "cal/calculraterResult";
    }

    @PostMapping("/dutchResult")
    public String saveDutchResult(@ModelAttribute String result) {
        return "redirect:/dutch";
    }
}
