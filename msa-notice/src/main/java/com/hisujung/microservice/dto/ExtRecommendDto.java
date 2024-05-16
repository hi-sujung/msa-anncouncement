package com.hisujung.microservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtRecommendDto {
    Long external_act_id;
    String title; //제목
}
