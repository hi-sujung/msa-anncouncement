package com.hisujung.microservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hisujung.microservice.entity.ExternalAct;
import com.hisujung.microservice.entity.LikeExternalAct;
import com.hisujung.microservice.entity.UnivActivity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class ExtActCrawlingDto {

    String title; //제목

    String category; //카테고리

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime startingDate; //시작일

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime deadline; //마감일

    String host; //주최

    String content; //내용

    String link; //링크

    public ExternalAct toEntity() {
        return ExternalAct.builder()
                .title(title)
                .category(category)
                .startingDate(startingDate)
                .deadline(deadline)
                .host(host)
                .content(content)
                .link(link).build();
    }
}
