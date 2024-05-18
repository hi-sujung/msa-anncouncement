package com.hisujung.microservice.service;

import com.hisujung.microservice.dto.ExtActListResponseDto;
import com.hisujung.microservice.dto.UnivActCrawlingDto;
import com.hisujung.microservice.dto.UnivActListResponseDto;
import com.hisujung.microservice.entity.*;
import com.hisujung.microservice.repository.LikeUnivActRepository;
import com.hisujung.microservice.repository.ParticipateUnivRepository;
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
    private final ParticipateUnivRepository participateUnivRepository;

    //모든 교내 공지사항 조회
    public List<UnivActListResponseDto> findAllByDesc() {
        return univActivityRepository.findAll().stream().map(UnivActListResponseDto::new).collect(Collectors.toList());
    }

    //해당되는 교내 공지사항 조회
    public UnivActListResponseDto findById(String memberId, Long id) {
        UnivActivity entity = univActivityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 교내 공지사항을 찾을 수 없습니다."));

        if (likeUnivActRepository.findByMemberAndAct(memberId, entity).isPresent()) {
            if (participateUnivRepository.findByMemberAndUnivAct(memberId, entity).isPresent()) {
                return new UnivActListResponseDto(entity, 1, 1);
            }
        }
        else {
            if (participateUnivRepository.findByMemberAndUnivAct(memberId, entity).isPresent()) {
                return new UnivActListResponseDto(entity, 0, 1);
            }
        }
        return new UnivActListResponseDto(entity, 0, 0); //회원이 좋아요 눌렀으면 1
    }

    //교내 공지사항 학과별로 조회
    public List<UnivActListResponseDto> findByDepartment(String department) {
        return univActivityRepository.findByDepartmentContaining(department).stream().map(UnivActListResponseDto::new).collect(Collectors.toList());
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


    //========= 대외활동 참여 여부 체크 ===========

    /**
     * 대외활동 참여 여부 저장
     * @param actId
     * @param memberId
     * @return
     */
    @Transactional
    public Long saveCheck(Long actId, String memberId) {
        UnivActivity e = univActivityRepository.findById(actId).orElseThrow();
        return participateUnivRepository.save(ParticipateUniv.builder().memberId(memberId).univActivity(e).build()).getId();
    }

    @Transactional
    //대외활동 좋아요 취소
    public void deleteCheck(String memberId, Long id) {
        UnivActivity e = univActivityRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 대외활동 정보를 조회할 수 없습니다."));
        ParticipateUniv participateUniv = participateUnivRepository.findByMemberAndUnivAct(memberId,e).orElseThrow(() -> new IllegalArgumentException(("해당 참여 체크 항목이 없습니다.")));
        participateUnivRepository.delete(participateUniv);
    }

    //회원의 참여 체크한 대외활동 목록 조회
    public List<UnivActListResponseDto> findCheckedByUser(String memberId) {
        List<ParticipateUniv> likeList = participateUnivRepository.findByMemberId(memberId);
        List<UnivActListResponseDto> resultList = new ArrayList<>();
        for(ParticipateUniv a: likeList) {
            UnivActivity e = a.getUnivActivity();
            resultList.add(new UnivActListResponseDto(e));
        }
        return resultList;
    }

    @Transactional
    public void saveActivity(UnivActCrawlingDto univActCrawlingDto) {
        univActivityRepository.save(univActCrawlingDto.toEntity());
    }
}
