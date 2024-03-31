package com.hisujung.microservice.controller;

import com.hisujung.web.dto.ExtActListResponseDto;
import com.hisujung.web.entity.Member;
import com.hisujung.web.service.BasicMemberService;
import com.hisujung.web.service.ExtActService;
import com.hisujung.web.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/externalact")
@RequiredArgsConstructor
@RestController
public class ExtActivityApiController {

    private final ExtActService extActService;
    private final BasicMemberService basicMemberService;
    private final UserService userService;

    @GetMapping("/")
    public List<ExtActListResponseDto> findAll() {
        return extActService.findAllByDesc();
    }

    @GetMapping("/keyword")
    public List<ExtActListResponseDto> findByTitle(@RequestParam String keyword) {
        return extActService.findByTitle(keyword);
    }

    @GetMapping("/id")
    public ExtActListResponseDto findById(Authentication auth, @RequestParam Long id) {
        Member m = userService.getLoginUserByLoginId(auth.getName());
        return extActService.findById(m, id);
    }

    //====== 대외활동 좋아요 눌렀을 때 =======
    @PostMapping("like")
    public Long saveLike(Authentication auth, @RequestParam Long actId) {
        Member m = userService.getLoginUserByLoginId(auth.getName());
        return extActService.saveLike(m, actId);
    }

    //대외활동 좋아요 삭제
    @DeleteMapping("/likecancel")
    public Long deleteLike(Authentication auth, @RequestParam Long id) {
        Member m = userService.getLoginUserByLoginId(auth.getName());
        extActService.deleteLike(m, id);
        return id;
    }

    //회원의 대외활동 좋아요 리스트 조회
    @GetMapping("/likelist")
    public List<ExtActListResponseDto> findByMember(Authentication auth) {
        Member m = userService.getLoginUserByLoginId(auth.getName());
        return extActService.findByUser(m);
    }
}
