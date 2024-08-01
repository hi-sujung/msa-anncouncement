package com.hisujung.microservice.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CaffeineCacheManager  cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCacheNames(List.of("recommendUnivCache", "recommendExtCache"));  // 캐시 이름 설정
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.DAYS) // 캐시 1일후 삭제
                .maximumSize(100).recordStats()); // 캐시 크기 100
        cacheManager.setAsyncCacheMode(true);  // null 값 저장 여부 설정
        return cacheManager;
    }
}

