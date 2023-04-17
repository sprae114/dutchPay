package com.example.dutchpay.service;

import com.example.dutchpay.domain.Friend;
import com.example.dutchpay.dto.FriendSelectSaveDto;
import com.example.dutchpay.dto.LoginPrincipal;
import com.example.dutchpay.repository.FriendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class FriendService {
    public static ArrayList<FriendSelectSaveDto> friendSelectListBefore;
    public static ArrayList<FriendSelectSaveDto> friendSelectListAfter;
    public FriendRepository friendRepository;

    @Autowired
    public FriendService(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
        friendSelectListBefore = new ArrayList<>();
        friendSelectListAfter = new ArrayList<>();
    }

    public void addFriend(Friend friend) {
        if (friend == null) {
            throw new NullPointerException("친구가 존재하지 않습니다.");
        }

        friendRepository.save(friend);
    }

    public List<Friend> searchFriend(Long id) {
        return friendRepository.findAllByUserAccountId(id);
    }

    public List<Friend> searchFriend(LoginPrincipal loginPrincipal) {
        return friendRepository.findAllByUserAccountId(loginPrincipal.getId());
    }

    public void deleteFriend(Long id) {
        friendRepository.deleteById(id);
    }

    public void addFriendSelectListBefore(List<Friend> friendList) {
        initFriendSelectListBefore();

        for (Friend friend : friendList) {
            friendSelectListBefore.add(friend.toFriendSelectSaveDto(friend));
        }
    }

    public void addFriendSelectListAfter(List<Integer> selectedfreind) {
        initFriendSelectListAfter();
        selectedfreind.forEach(i -> friendSelectListAfter.add(friendSelectListBefore.get(i)));
    }

    private void initFriendSelectListBefore() {
        friendSelectListBefore.clear();
    }

    private void initFriendSelectListAfter() {
        friendSelectListAfter.clear();
    }
}
