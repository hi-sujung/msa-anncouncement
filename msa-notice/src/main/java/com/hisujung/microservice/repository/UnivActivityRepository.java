package com.hisujung.microservice.repository;

import com.hisujung.web.entity.UnivActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UnivActivityRepository extends JpaRepository<UnivActivity, Long> {

    //교내 공지사항 학과별로 검색
    List<UnivActivity> findByPostDepartmentContaining(String department);

    //교내 전체 공지사항 제목 키워드로 검색
    List<UnivActivity> findByTitleContaining(String keyword);

    //교재 공지사항 학과와 제목 키워드로 검색
    @Query("SELECT u FROM UnivActivity u WHERE u.postDepartment LIKE %:dep% AND u.title LIKE %:keyword% ORDER BY u.id DESC")
    List<UnivActivity> findByDepartmentAndTitle(@Param("dep") String department, @Param("keyword") String keyword);
}
