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

    @Data
    class NaverSearchResponse {
        private List<Item> items = Collections.emptyList();

        @Data
        public static class Item {
            private String originallink;   // 원문 URL
        }
    }

    @GetMapping("/v1/search/shop.json")
    NaverShopSearchResponse searchShop(
            @RequestParam("query")   String  query         // 검색어
    );

    @Data
    class NaverShopSearchResponse {
        private Integer total;
        private Integer start;
        private Integer display;
        private List<Item> items = java.util.Collections.emptyList();

        @Data
        public static class Item {
            private String title;        // <b>태그</b> 포함 제목
            private String link;         // 상품 링크
            private String image;        // 섬네일
            private Integer lprice;      // 최저가
            private Integer hprice;      // 최고가
            private String mallName;     // 쇼핑몰명
            private String maker;        // 제조사 (필드 추가)
        }
    }
}
