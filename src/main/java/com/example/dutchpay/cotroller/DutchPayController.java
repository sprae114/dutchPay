package com.example.dutchpay.cotroller;

import com.example.dutchpay.dto.Dutchpay;
import com.example.dutchpay.dto.DutchpayNoAlchol;
import com.example.dutchpay.dto.FreindsDutchpayNoAlcholDto;
import com.example.dutchpay.dto.FreindsDutchpayDto;
import com.example.dutchpay.service.DutchResultService;
import com.example.dutchpay.service.FriendService;
import com.example.dutchpay.service.InMemoryDutchPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.ArrayList;
import java.util.List;

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
        System.out.println(">> dutchpayDb " + dutchpayDb);
        model.addAttribute("dutchList", dutchpayMain);

        return "cal/totalcalculrate";
    }

    @GetMapping("/dutchPayListClear")
    public String dutchClear() {
        dutchinit();
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
        // TODO : 결과 만들어야함.
        sumMoney();

        model.addAttribute("result", "결과");
        return "cal/calculraterResult";
    }

    static public void dutchinit() {
        dutchpayDb.clear();  //각각 금액 저장
        dutchpayTotal.clear();  //계산할 총금액
        dutchpayMain.clear();  //목록
    }

    //총액 더하기
    private void sumMoney(){
        dutchpayTotal = dutchpayDb.get(0);

        for(int i = 1; i < dutchpayDb.size(); i++){
            for(int j = 0; j < dutchpayDb.get(i).size(); j++){
                dutchpayTotal.set(j, dutchpayTotal.get(j) + dutchpayDb.get(i).get(j));
            }
        }
    }

    //금액 보내기
    private void sendMoney(){
        for(int i = 0; i < dutchpayTotal.size(); i++){
            dutchpayMain.add(dutchpayTotal.get(i) / dutchpayTotal.size());
        }
    }

    //글 출력
    private String printDutchPay(){
        return "결과";
    }
}
