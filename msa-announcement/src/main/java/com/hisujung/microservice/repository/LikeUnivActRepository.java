package com.hisujung.microservice.repository;

import com.hisujung.web.entity.LikeUnivAct;
import com.hisujung.web.entity.Member;
import com.hisujung.web.entity.UnivActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeUnivActRepository extends JpaRepository<LikeUnivAct, Long> {

    List<LikeUnivAct> findByMember(Member member);
    @Query("SELECT l FROM LikeUnivAct l WHERE l.member = :member AND l.univActivity = :activity")
    Optional<LikeUnivAct> findByMemberAndAct(@Param("member") Member member, @Param("activity") UnivActivity activity);
}
