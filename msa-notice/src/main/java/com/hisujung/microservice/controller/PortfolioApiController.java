package com.hisujung.microservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hisujung.microservice.ApiResponse;
import com.hisujung.microservice.dto.ActivitiesDto;
import com.hisujung.microservice.dto.AutoPortfolioDto;
import com.hisujung.microservice.dto.UnivActListResponseDto;
import com.hisujung.microservice.service.GptServiceImpl;
import com.hisujung.microservice.service.RateLimiterService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
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

    private final GptServiceImpl gptService;
    private final RateLimiterService rateLimiterService;

    public List<UnivActListResponseDto> fetchNoticeCheckedList(String memberId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/notice/univactivity/checked-list?memberId=" + memberId;

        ResponseEntity<List<UnivActListResponseDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UnivActListResponseDto>>() {}
        );

        return response.getBody();
    }

    //public

    public String fetchNoticeCheckedList() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/notice/univactivity/checked-list", String.class);
        return response.getBody();
    }

    // 처리율 제한 장치 적용하려는 API
    @PostMapping("/create-by-ai")
    public ApiResponse<AutoPortfolioDto> createByAi(@RequestParam String memberId, @RequestParam String careerField) throws JsonProcessingException {

        //String memberId = auth.getName();

        Bucket bucket = rateLimiterService.resolveBucket(memberId);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        long saveToken = probe.getRemainingTokens();

        if(probe.isConsumed()) {
            log.info("API Call Success");
            log.info("Available Token : {}", saveToken);

            // 변경된 부분 시작
            List<UnivActListResponseDto> noticeCheckedList = fetchNoticeCheckedList(memberId);
            //String titles = ""
            StringBuilder titles = new StringBuilder();
            for(UnivActListResponseDto dto: noticeCheckedList) {
                titles.append(dto.getTitle());
                titles.append(", ");
            }

            AutoPortfolioDto result = new AutoPortfolioDto(gptService.getAssistantMsg(titles.toString(), careerField));
             // 변경된 부분 끝

            //AutoPortfolioDto result = new AutoPortfolioDto(gptService.getAssistantMsg(dto.getActivities(), dto.getCareerField()));

//            if(result == -1L) {
//                return (ApiResponse<Long>) ApiResponse.createError("포트폴리오 생성에 실패했습니다.");
//            }

            return ApiResponse.createSuccess(result);
        }

        long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;

        log.info("TOO MANY REQUEST");
        log.info("Available Token : {}", saveToken);
        log.info("Wait Time {} Second ", waitForRefill);

        return (ApiResponse<AutoPortfolioDto>) ApiResponse.createError("HttpStatus.TOO_MANY_REQUESTS");
    }

}
