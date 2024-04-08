package com.hisujung.microservice.entity;

import com.hisujung.microservice.BaseTimeEntity;
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

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "member_id")
    //private Member member;

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "univ_activity_id")
    private UnivActivity univActivity;

    @Builder
    public LikeUnivAct(Member member, UnivActivity univActivity) {
        //this.member = member;
        this.univActivity = univActivity;
    }
}
