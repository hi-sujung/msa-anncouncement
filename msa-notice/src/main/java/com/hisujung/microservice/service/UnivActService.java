package com.hisujung.microservice.service;

import com.hisujung.microservice.dto.UnivActListResponseDto;
import com.hisujung.microservice.entity.LikeUnivAct;
import com.hisujung.microservice.entity.UnivActivity;
import com.hisujung.microservice.repository.LikeUnivActRepository;
import com.hisujung.microservice.repository.UnivActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UnivActService {

    private final UnivActivityRepository univActivityRepository;
    private final LikeUnivActRepository likeUnivActRepository;

    //모든 교내 공지사항 조회
    public List<UnivActListResponseDto> findAllByDesc() {
        return univActivityRepository.findAll().stream().map(UnivActListResponseDto::new).collect(Collectors.toList());
    }

    //해당되는 교내 공지사항 조회
    public UnivActListResponseDto findById(String memberId, Long id) {
        UnivActivity entity = univActivityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 교내 공지사항을 찾을 수 없습니다."));

        if (likeUnivActRepository.findByMemberAndAct(memberId, entity).isPresent()) {
            return new UnivActListResponseDto(entity, 1); //회원이 좋아요 눌렀으면 1
        }

        return new UnivActListResponseDto(entity, 0); //회원이 좋아요 안 눌렀으면 0
    }

    //교내 공지사항 학과별로 조회
    public List<UnivActListResponseDto> findByDepartment(String department) {
        return univActivityRepository.findByPostDepartmentContaining(department).stream().map(UnivActListResponseDto::new).collect(Collectors.toList());
    }

    //교내 공지사항 제목으로 조회
    public List<UnivActListResponseDto> findByTitle(String keyword) {
        return univActivityRepository.findByTitleContaining(keyword).stream().map(UnivActListResponseDto::new).collect(Collectors.toList());
    }

    //교내 공지사항 학과와 제목으로 조회
    public List<UnivActListResponseDto> findByDepAndTitle(String department, String keyword) {
        return univActivityRepository.findByDepartmentAndTitle(department, keyword).stream().map(UnivActListResponseDto::new).collect(Collectors.toList());
    }

    //============= 회원이 교내활동 좋아요 버튼 눌렀을 때 =============

   @Transactional
    public Long saveLike(Long univActId, String memberId) {
        UnivActivity a = univActivityRepository.findById(univActId).orElseThrow();
        return likeUnivActRepository.save(LikeUnivAct.builder()
                .memberId(memberId)
                .univActivity(a)
                .build()).getId();
    }

    @Transactional
    //교내 공지사항 좋아요 취소
    public void deleteLike(String memberId, Long id) {
        UnivActivity u = univActivityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 교내 공지사항이 없습니다."));
        LikeUnivAct likeUnivAct = likeUnivActRepository.findByMemberAndAct(memberId, u).orElseThrow(() -> new IllegalArgumentException(("해당 좋아요 항목이 없습니다.")));
        likeUnivActRepository.delete(likeUnivAct);
    }


    //회원의 교내 공지사항 좋아요 목록 조회
    public List<UnivActListResponseDto> findByUser(String memberId) {
        List<LikeUnivAct> likeList = likeUnivActRepository.findByMemberId(memberId);
        List<UnivActListResponseDto> resultList = new ArrayList<>();
        for(LikeUnivAct a: likeList) {
            UnivActivity u = a.getUnivActivity();
            resultList.add(new UnivActListResponseDto(u));
        }
        return resultList;
    }



}
