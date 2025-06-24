package com.reactivespring.agriculture_contest.naver;

import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

// FeignClient는 interface로 선언하며, @FeignClient 어노테이션을 사용하여 Naver API와 통신할 수 있도록 설정합니다.
@FeignClient(
        name = "naverClient",
        url = "${naver.base-url}",
        configuration = NaverFeignConfig.class
)
public interface NaverClient {

    @GetMapping("/v1/search/news.json")
    NaverSearchResponse searchNews(
                                   // 밑의 것은 query parameter로 전달하는 것!
                                   @RequestParam("query") String query ,
                                   @RequestParam("display") Integer display,
                                   @RequestParam("start") Integer start,
                                   @RequestParam("sort") String sort);
                                   // 밑의 것은 header로 전달하는 것!  -> config에서 채워주니까!
//                                   @RequestHeader("X-Naver-Client-Id") String clientId,
//                                   @RequestHeader("X-Naver-Client-Secret") String clientSecret);


    // json, xml 둘 다 있어서 헷갈렸..
    @Data
    class NaverSearchResponse {
        private List<Item> items = Collections.emptyList();

        @Data
        public static class Item {
            private String originallink;   // 원문 URL
        }
    }
}
