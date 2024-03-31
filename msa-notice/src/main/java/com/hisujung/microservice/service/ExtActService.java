package com.hisujung.microservice.service;

import com.hisujung.web.dto.ExtActListResponseDto;
import com.hisujung.web.dto.LikeExtActRequestDto;
import com.hisujung.web.entity.ExternalAct;
import com.hisujung.web.entity.LikeExternalAct;
import com.hisujung.web.entity.Member;
import com.hisujung.web.jpa.ExternalActRepository;
import com.hisujung.web.jpa.LikeExternalActRepository;
import lombok.RequiredArgsConstructor;
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
    public Long saveLike(Member member, Long actId) {
        ExternalAct e = externalActRepository.findById(actId).orElseThrow();
        LikeExtActRequestDto dto = LikeExtActRequestDto.builder()
                .member(member)
                .externalAct(e)
                .build();

        return likeExternalActRepository.save(dto.toEntity()).getId();
    }


    @Transactional
    //대외활동 좋아요 취소
    public void deleteLike(Member m, Long id) {
        ExternalAct e = externalActRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 대외활동 정보를 조회할 수 없습니다."));
        LikeExternalAct likeExternalAct = likeExternalActRepository.findByMemberAndAct(m,e).orElseThrow(() -> new IllegalArgumentException(("해당 좋아요 항목이 없습니다.")));
        likeExternalActRepository.delete(likeExternalAct);
    }

    //회원의 대외활동 좋아요 목록 조회
    public List<ExtActListResponseDto> findByUser(Member member) {
        List<LikeExternalAct> likeList = likeExternalActRepository.findByMember(member);
        List<ExtActListResponseDto> resultList = new ArrayList<>();
        for(LikeExternalAct a: likeList) {
            ExternalAct e = a.getActivity();
            resultList.add(new ExtActListResponseDto(e));
        }
        return resultList;
    }

    public ExtActListResponseDto findById(Member m, Long id) {
        ExternalAct e = externalActRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 대외활동 정보가 존재하지 않습니다."));

        if (likeExternalActRepository.findByMemberAndAct(m, e).isPresent()) {
            return new ExtActListResponseDto(e, 1); // 회원이 좋아요 눌렀으면 1 보냄
        }
        return new ExtActListResponseDto(e, 0); // 회원이 좋아요 안 눌렀으면 0 보냄
    }
}
