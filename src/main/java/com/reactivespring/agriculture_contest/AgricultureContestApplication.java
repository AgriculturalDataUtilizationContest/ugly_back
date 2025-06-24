package com.reactivespring.agriculture_contest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
// Feign 클라이언트 활성화
@EnableFeignClients(
        // basePackages 속성은 Feign 클라이언트가 위치한 패키지를 지정합니다.
        // 지정 안 하면, 무시됨.
        basePackages = "com.reactivespring.agriculture_contest.naver"
)
public class AgricultureContestApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgricultureContestApplication.class, args);
    }

}
