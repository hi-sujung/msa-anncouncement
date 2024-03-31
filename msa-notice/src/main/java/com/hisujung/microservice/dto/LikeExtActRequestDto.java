package com.hisujung.microservice.dto;

import com.hisujung.web.entity.ExternalAct;
import com.hisujung.web.entity.LikeExternalAct;
import com.hisujung.web.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
//@AllArgsConstructor
public class LikeExtActRequestDto {

    private Member member;
    private ExternalAct externalAct;

    @Builder
    public LikeExtActRequestDto(Member member, ExternalAct externalAct) {
        this.member = member;
        this.externalAct = externalAct;
    }

    public LikeExternalAct toEntity() {
        return LikeExternalAct.builder()
                .member(member)
                .activity(externalAct)
                .build();
    }
}
