package com.hisujung.microservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnivRecommendDto {
    Long univ_activity_id;
    String title; //제목
}
