package com.hisujung.microservice.entity;

import com.hisujung.web.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class LikeUnivAct extends BaseTimeEntity {

    @Id @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "univ_activity_id")
    private UnivActivity univActivity;

    @Builder
    public LikeUnivAct(Member member, UnivActivity univActivity) {
        this.member = member;
        this.univActivity = univActivity;
    }
}
