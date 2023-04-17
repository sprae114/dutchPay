package com.example.dutchpay.cotroller;

import com.example.dutchpay.dto.FriendSaveDto;
import com.example.dutchpay.dto.LoginPrincipal;
import com.example.dutchpay.dto.UserAccountDto;
import com.example.dutchpay.service.DutchPayService;
import com.example.dutchpay.service.FriendService;
import com.example.dutchpay.service.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.example.dutchpay.service.FriendService.*;

@Slf4j
@Controller
public class FriendController {
    private final FriendService friendService;
    private final UserAccountService userAccountService;
    private final DutchPayService dutchPayService;

    @Autowired
    public FriendController(FriendService friendService,
                            UserAccountService userAccountService,
                            DutchPayService dutchPayService) {
        this.friendService = friendService;
        this.userAccountService = userAccountService;
        this.dutchPayService = dutchPayService;
    }

    @GetMapping("/friend")
    public String friendList(@AuthenticationPrincipal LoginPrincipal loginPrincipal,
                             Model model) {

        model.addAttribute("friendList", friendService.searchFriend(loginPrincipal));
        model.addAttribute("newFriend", new FriendSaveDto());

        return "friend";
    }

    @PostMapping("/friend")
    public String dutchAddFriend(@AuthenticationPrincipal LoginPrincipal loginPrincipal,
                                 @Validated @ModelAttribute("newFriend") FriendSaveDto friendSaveDto,
                                 BindingResult bindingResult) {


        UserAccountDto userAccountDto = userAccountService.searchUser(loginPrincipal).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + loginPrincipal)
        );

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors={} ", bindingResult);
            return "/friend";
        }

        friendService.addFriend(friendSaveDto.toEntity(userAccountDto.toEntity()));

        return "redirect:/" + "friend";
    }

    @GetMapping("/friend/{id}")
    public String dutchDeleteFriend(@PathVariable Long id) {

        friendService.deleteFriend(id);

        return "redirect:/" + "friend";
    }
    
    @GetMapping("/dutch/friend")
    public String dutchFriendSelect(@AuthenticationPrincipal LoginPrincipal loginPrincipal,
                                    Model model) {

        friendService.addFriendSelectListBefore(friendService.searchFriend(loginPrincipal));
        model.addAttribute("friendList", friendSelectListBefore);

        return "friendSelect";
    }

    @PostMapping("/dutch/friend")
    public String dutchFriendSelectSave(@RequestParam("selectedfreind") List<Integer> selectedfreind) {
        friendService.addFriendSelectListAfter(selectedfreind);
        dutchPayService.dutchinit();

        return "redirect:/" + "dutch/dutchPayList";
    }
}