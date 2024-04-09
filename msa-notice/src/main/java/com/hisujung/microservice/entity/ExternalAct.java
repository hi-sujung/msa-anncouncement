package com.hisujung.microservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hisujung.microservice.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class ExternalAct extends BaseTimeEntity {

    @Id @Column(name = "external_act_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String info;

    private String link;

    @Column(columnDefinition = "TEXT")
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime deadline;
    
    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL)
    private List<LikeExternalAct> likeList;

    @Builder
    public ExternalAct(String title, String info, String link, LocalDateTime startDate, LocalDateTime deadline, String content) {
        this.title = title;
        this.info = info;
        this.link = link;
        this.startDate = startDate;
        this.deadline = deadline;
        this.content = content;

    }



}
