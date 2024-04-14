package com.hisujung.microservice.controller;

import com.hisujung.microservice.dto.UnivActCrawlingDto;
import com.hisujung.microservice.dto.UnivActListResponseDto;
import com.hisujung.microservice.service.UnivActService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("notice/univactivity")
public class UnivActivityApiController {

    private final UnivActService univActService;

    //전체 교내 공지사항 조회
    @GetMapping("/")
    public List<UnivActListResponseDto> findAll() {
        return univActService.findAllByDesc();
    }

    //학과별 교내 공지사항 조회
    @GetMapping("/department")
    public List<UnivActListResponseDto> findByDepartment(@RequestParam String department) {
        return univActService.findByDepartment(department);
    }

    //제목 키워드별 교내 공지사항 조회
    @GetMapping("/keyword")
    public List<UnivActListResponseDto> findByTitle(@RequestParam String keyword) {
        return univActService.findByTitle(keyword);
    }

    //제목 및 학과별 교내 공지사항 조회
    @GetMapping("/department/keyword")
    public List<UnivActListResponseDto> findByDepAndTitle(@RequestParam String department, @RequestParam String keyword) {
        return univActService.findByDepAndTitle(department, keyword);
    }

    //========회원이 교내 공지사항 좋아요 눌렀을 때========
    @PostMapping("/like")
    public Long saveLike(@RequestParam Long actId, @RequestParam String memberId) {
        //String memberId = auth.getName();
        return univActService.saveLike(actId, memberId);
    }

    //교내 공지사항 상세페이지 조회
    @GetMapping("/id")
    public UnivActListResponseDto findById(@RequestParam Long actId, @RequestParam String memberId) {
        return univActService.findById(memberId, actId);
    }

    //교내 공지사항 좋아요 취소
    @DeleteMapping("/like-cancel")
    public Long deleteLike(@RequestParam Long id, @RequestParam String memberId) {
        //String memberId = auth.getName();
        univActService.deleteLike(memberId, id);
        return id;
    }

    //회원의 교내 공지사항 좋아요 목록
    @GetMapping("/like-list")
    public List<UnivActListResponseDto> findByUser(@RequestParam String memberId) {
        //String memberId = auth.getName();
        return univActService.findByUser(memberId);
    }


    //====== 대외활동 참여 체크 눌렀을 때 =======
    @PostMapping("/check")
    public Long saveCheck(String memberId, @RequestParam Long actId) {
        return univActService.saveCheck(actId, memberId);
    }

    @DeleteMapping("/check-cancel")
    public Long deleteCheck(String memberId, @RequestParam Long id) {
        univActService.deleteCheck(memberId, id);
        return id;
    }

    @GetMapping("/checked-list")
    public List<UnivActListResponseDto> findCheckedByMember(String memberId) {
        return univActService.findCheckedByUser(memberId);
    }

    //교내 공지사항 크롤링 데이터 저장
    @RabbitListener(queues = "univ_activity_queue")
    public void univProcessMessage(UnivActCrawlingDto univActCrawlingDto) {
        univActService.saveActivity(univActCrawlingDto);
    }
}
