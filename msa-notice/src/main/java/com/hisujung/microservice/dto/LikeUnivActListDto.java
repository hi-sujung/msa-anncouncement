package com.hisujung.microservice.dto;

import com.hisujung.web.entity.LikeUnivAct;
import com.hisujung.web.entity.Member;
import com.hisujung.web.entity.UnivActivity;
import lombok.Getter;

@Getter
public class LikeUnivActListDto {
    Long id;
    Member member;
    UnivActivity univActivity;


    public LikeUnivActListDto(LikeUnivAct entity) {
        this.id = entity.getId();
        this.member = entity.getMember();
        this.univActivity = entity.getUnivActivity();
    }
}
