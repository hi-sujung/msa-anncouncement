package com.hisujung.microservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hisujung.web.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class UnivActivity extends BaseTimeEntity {

    @Id @Column(name = "univ_activity_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String postDepartment;

    //올라온 날짜
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startDate;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
//    private LocalDateTime deadline;

    private String link;

    @OneToMany(mappedBy = "univActivity", cascade = CascadeType.ALL)
    private List<LikeUnivAct> likeList = new ArrayList<>();


    @Builder
    public UnivActivity(String title, String postDepartment, LocalDateTime startDate, String link) {
        this.title = title;
        this.postDepartment = postDepartment;
        this.startDate = startDate;
        //this.deadline = deadline;
        this.link = link;
    }
}
