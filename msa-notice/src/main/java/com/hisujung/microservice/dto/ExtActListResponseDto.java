package com.hisujung.microservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hisujung.microservice.entity.ExternalAct;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ExtActListResponseDto {

    private Long id;
    private String title;
    private String info;
    private String link;
    private String content;
    int isLiked;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime deadline;

    public ExtActListResponseDto(ExternalAct entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.info = entity.getInfo();
        this.link = entity.getLink();
        this.content = entity.getContent();
        this.startDate = entity.getStartDate();
        this.deadline = entity.getDeadline();
    }

    public ExtActListResponseDto(ExternalAct entity, int isLiked) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.info = entity.getInfo();
        this.link = entity.getLink();
        this.content = entity.getContent();
        this.startDate = entity.getStartDate();
        this.deadline = entity.getDeadline();
        this.isLiked = isLiked;
    }
}
