package com.hisujung.microservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hisujung.microservice.ApiResponse;
import com.hisujung.microservice.dto.PortfolioSaveRequestDto;
import com.hisujung.microservice.dto.UnivActListResponseDto;
import com.hisujung.microservice.service.ExtActService;
import com.hisujung.microservice.service.UnivActService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("notice/portfolio")
public class PortfolioApiController {

    private final UnivActService univActService;
    private final ExtActService extActService;
    @Value("${portfolio.ms.url}")
    private String portfolioMsUrl;

    public List<UnivActListResponseDto> fetchNoticeCheckedList(String memberId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = portfolioMsUrl+"notice/univactivity/auth/checked-list?memberId=" + memberId;

        ResponseEntity<List<UnivActListResponseDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UnivActListResponseDto>>() {}
        );

        return response.getBody();
    }


    // 처리율 제한 장치 적용하려는 API
    @PostMapping(path="/create-by-ai", headers = "X-Authorization-Id")
    public ApiResponse<Void> createByAi(@RequestHeader("X-Authorization-Id") String memberId, @RequestParam String careerField,
                                                           @RequestParam String title) throws JsonProcessingException {

        List<UnivActListResponseDto> noticeCheckedList = univActService.findCheckedByUser(memberId);
        StringBuilder titles = new StringBuilder();
        for(UnivActListResponseDto dto: noticeCheckedList) {
            titles.append(dto.getTitle());
            titles.append(", ");
        }

        // RestTemplate을 사용하여 포트폴리오 저장 API 호출
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("X-Authorization-Id", memberId); // 헤더에 memberId 추가

        HttpEntity<String> entity = new HttpEntity<>(titles.toString(), headers);

        String saveUrl = portfolioMsUrl+ "portfolio/new?memberId=" + memberId;
        ResponseEntity<ApiResponse> saveResponseEntity = restTemplate.exchange(saveUrl, HttpMethod.POST, entity, ApiResponse.class);

        return (ApiResponse<Void>) ApiResponse.createSuccessWithNoContent();
    }

}
