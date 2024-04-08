package com.hisujung.microservice.repository;

import com.hisujung.microservice.entity.ExternalAct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExternalActRepository extends JpaRepository<ExternalAct, Long> {

    //대외활동 제목 키워드로 검색
    List<ExternalAct> findByTitleContaining(String title);

}
