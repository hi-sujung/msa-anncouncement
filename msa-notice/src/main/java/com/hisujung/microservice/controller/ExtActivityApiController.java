package com.hisujung.microservice.controller;

import com.hisujung.microservice.dto.ExtActCrawlingDto;
import com.hisujung.microservice.dto.ExtActListResponseDto;
import com.hisujung.microservice.dto.UnivActCrawlingDto;
import com.hisujung.microservice.service.ExtActService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("notice/externalact")
@RequiredArgsConstructor
@RestController
public class ExtActivityApiController {

    private final ExtActService extActService;
//    private final BasicMemberService basicMemberService;
//    private final UserService userService;

    @GetMapping("/")
    public List<ExtActListResponseDto> findAll() {
        return extActService.findAllByDesc();
    }

    @GetMapping("/keyword")
    public List<ExtActListResponseDto> findByTitle(@RequestParam String keyword) {
        return extActService.findByTitle(keyword);
    }

    @GetMapping("/id")
    public ExtActListResponseDto findById(String memberId, @RequestParam Long id) {
        return extActService.findById(memberId, id);
    }

    //====== 대외활동 좋아요 눌렀을 때 =======
    @PostMapping("like")
    public Long saveLike(String memberId, @RequestParam Long actId) {
        return extActService.saveLike(memberId, actId);
    }

    //대외활동 좋아요 삭제
    @DeleteMapping("/like-cancel")
    public Long deleteLike(String memberId, @RequestParam Long id) {
        extActService.deleteLike(memberId, id);
        return id;
    }

    //회원의 대외활동 좋아요 리스트 조회
    @GetMapping("/like-list")
    public List<ExtActListResponseDto> findByMember(String memberId) {
        return extActService.findLikedByUser(memberId);
    }

    // 대외활동 크롤링 데이터 저장
    @RabbitListener(queues = "external_act_queue")
    public void ExtProcessMessage(ExtActCrawlingDto extActCrawlingDto) {
        extActService.saveActivity(extActCrawlingDto);
    }
}
