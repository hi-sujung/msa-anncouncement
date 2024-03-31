package com.hisujung.microservice.dto;

import com.hisujung.web.entity.LikeUnivAct;
import com.hisujung.web.entity.Member;
import com.hisujung.web.entity.UnivActivity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeUnivActRequestDto {

    private Member member;
    private UnivActivity univActivity;

    @Builder
    public LikeUnivActRequestDto(Member member, UnivActivity univActivity) {
        this.member = member;
        this.univActivity = univActivity;
    }

    public LikeUnivAct toEntity() {
        return LikeUnivAct.builder()
                .member(member)
                .univActivity(univActivity)
                .build();
    }
}
