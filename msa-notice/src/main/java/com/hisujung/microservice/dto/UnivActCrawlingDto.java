package com.hisujung.microservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hisujung.microservice.entity.LikeUnivAct;
import com.hisujung.microservice.entity.UnivActivity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class UnivActCrawlingDto {

    String title;

    String department;

    String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime startingDate; //시작일

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime deadline; //마감일

    String link;

    public UnivActivity toEntity() {
        return UnivActivity.builder()
                .title(title)
                .department(department)
                .content(content)
                .startingDate(startingDate)
                .deadline(deadline)
                .link(link).build();
    }
}

