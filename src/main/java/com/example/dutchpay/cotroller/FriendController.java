package com.example.dutchpay.cotroller;

import com.example.dutchpay.domain.Friend;
import com.example.dutchpay.dto.FriendSaveDto;
import com.example.dutchpay.dto.FriendSelectSaveDto;
import com.example.dutchpay.service.DutchResultService;
import com.example.dutchpay.service.FriendRepository;
import com.example.dutchpay.service.FriendService;
import com.example.dutchpay.service.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.dutchpay.service.FriendService.*;

@Controller
public class FriendController {
    private final FriendRepository friendRepository;
    private final FriendService friendService;
    private final UserAccountRepository userAccountRepository;
    private final DutchResultService dutchResultService;

    @Autowired
    public FriendController(FriendRepository friendRepository,
                            FriendService friendService,
                            UserAccountRepository userAccountRepository
                            , DutchResultService dutchResultService) {
        this.friendRepository = friendRepository;
        this.friendService = friendService;
        this.userAccountRepository = userAccountRepository;
        this.dutchResultService = dutchResultService;
    }

    @GetMapping("/friend")
    public String friendList(Model model) {
        // todo: 추후에, userAccount.getId() 변경
        model.addAttribute("friendList", friendRepository.findAllByUserAccountId(1L));
        model.addAttribute("newFriend", new FriendSaveDto());
        return "friend";
    }

    @PostMapping("/friend")
    public String dutchAddFriend(@ModelAttribute("newFriend") FriendSaveDto friendSaveDto) {
        friendService.addFriend(friendSaveDto.toEntity(userAccountRepository.findById(1L).orElse(null)));
        return "redirect:/" + "friend";
    }

    @GetMapping("/friend/{id}")
    public String dutchDeleteFriend(@PathVariable Long id) {
        friendService.deleteFriend(id);
        return "redirect:/" + "friend";
    }
    
    @GetMapping("/dutch/friend")
    public String dutchFriendSelect(Model model) {
        friendService.addFriendSelectListBefore(friendRepository.findAllByUserAccountId(1L));
        model.addAttribute("friendList", friendSelectListBefore);
        return "friendSelect";
    }

    @PostMapping("/dutch/friend")
    public String dutchFriendSelectSave(@RequestParam("selectedfreind") List<Integer> selectedfreind) {
        friendService.addFriendSelectListAfter(selectedfreind);
        dutchResultService.dutchinit();

        return "redirect:/" + "dutch/dutchPayList";
    }
}