package com.hisujung.microservice.service;

import com.hisujung.microservice.dto.ExtActCrawlingDto;
import com.hisujung.microservice.dto.ExtActListResponseDto;
import com.hisujung.microservice.dto.UnivActCrawlingDto;
import com.hisujung.microservice.entity.ExternalAct;
import com.hisujung.microservice.entity.LikeExternalAct;
import com.hisujung.microservice.entity.ParticipateEx;
import com.hisujung.microservice.repository.ExternalActRepository;
import com.hisujung.microservice.repository.LikeExternalActRepository;
import com.hisujung.microservice.repository.ParticipateExRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ExtActService {

    private final ExternalActRepository externalActRepository;
    private final LikeExternalActRepository likeExternalActRepository;
    private final ParticipateExRepository participateExRepository;

    //전체 대외활동 조회
    public List<ExtActListResponseDto> findAllByDesc() {
        return externalActRepository.findAll().stream().map(ExtActListResponseDto::new).collect(Collectors.toList());
    }

    //제목 키워드별 대외활동 조회
    public List<ExtActListResponseDto> findByTitle(String keyword) {
        return externalActRepository.findByTitleContaining(keyword).stream().map(ExtActListResponseDto::new).collect(Collectors.toList());
    }

    //========= 대외활동 좋아요 ========
    @Transactional
    public Long saveLike(String memberId, Long actId) {
        ExternalAct e = externalActRepository.findById(actId).orElseThrow();
        return likeExternalActRepository.save(LikeExternalAct.builder().memberId(memberId).activity(e).build()).getId();
    }


    @Transactional
    //대외활동 좋아요 취소
    public void deleteLike(String memberId, Long id) {
        ExternalAct e = externalActRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 대외활동 정보를 조회할 수 없습니다."));
        LikeExternalAct likeExternalAct = likeExternalActRepository.findByMemberAndAct(memberId,e).orElseThrow(() -> new IllegalArgumentException(("해당 좋아요 항목이 없습니다.")));
        likeExternalActRepository.delete(likeExternalAct);
    }

    //회원의 대외활동 좋아요 목록 조회
    public List<ExtActListResponseDto> findLikedByUser(String memberId) {
        List<LikeExternalAct> likeList = likeExternalActRepository.findByMemberId(memberId);
        List<ExtActListResponseDto> resultList = new ArrayList<>();
        for(LikeExternalAct a: likeList) {
            ExternalAct e = a.getActivity();
            resultList.add(new ExtActListResponseDto(e));
        }
        return resultList;
    }

    public ExtActListResponseDto findById(String memberId, Long id) {
        ExternalAct e = externalActRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 대외활동 정보가 존재하지 않습니다."));

        if (likeExternalActRepository.findByMemberAndAct(memberId, e).isPresent()) {
            if (participateExRepository.findByMemberAndExtAct(memberId, e).isPresent()) {
                return new ExtActListResponseDto(e, 1, 1); // 회원이 좋아요/참여 체크 눌렀으면 1 보냄, 안 눌렀으면 0
            }
            return new ExtActListResponseDto(e, 1, 0);
        }

        else {
            if (participateExRepository.findByMemberAndExtAct(memberId, e).isPresent()) {
                return new ExtActListResponseDto(e, 0, 1); // 회원이 좋아요/참여 체크 눌렀으면 1 보냄, 안 눌렀으면 0
            }
            return new ExtActListResponseDto(e, 0, 0);

        }
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
        ExternalAct e = externalActRepository.findById(actId).orElseThrow();
        return participateExRepository.save(ParticipateEx.builder().memberId(memberId).activity(e).build()).getId();
    }

    @Transactional
    //대외활동 좋아요 취소
    public void deleteCheck(String memberId, Long id) {
        ExternalAct e = externalActRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 대외활동 정보를 조회할 수 없습니다."));
        ParticipateEx participateEx = participateExRepository.findByMemberAndExtAct(memberId,e).orElseThrow(() -> new IllegalArgumentException(("해당 참여 체크 항목이 없습니다.")));
        participateExRepository.delete(participateEx);
    }

    //회원의 참여 체크한 대외활동 목록 조회
    public List<ExtActListResponseDto> findCheckedByUser(String memberId) {
        List<ParticipateEx> likeList = participateExRepository.findByMemberId(memberId);
        List<ExtActListResponseDto> resultList = new ArrayList<>();
        for(ParticipateEx a: likeList) {
            ExternalAct e = a.getActivity();
            resultList.add(new ExtActListResponseDto(e));
        }
        return resultList;
    }

    @Transactional
    public void saveActivity(ExtActCrawlingDto extActCrawlingDto) {
        externalActRepository.save(extActCrawlingDto.toEntity());
    }
}
