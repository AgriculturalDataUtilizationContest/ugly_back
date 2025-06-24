package com.reactivespring.agriculture_contest.naver;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NaverFeignConfig {

    private final NaverProperties props;

    public NaverFeignConfig(NaverProperties props) {
        this.props = props;
    }

    @Bean
    public RequestInterceptor naverAuthInterceptor(NaverProperties props) {
        return template -> {
            template.header("X-Naver-Client-Id",     props.getClientId());
            template.header("X-Naver-Client-Secret", props.getClientSecret());
        };
    }
}
