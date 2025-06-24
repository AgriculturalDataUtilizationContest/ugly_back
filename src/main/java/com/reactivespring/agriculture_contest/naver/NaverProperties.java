package com.reactivespring.agriculture_contest.naver;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "naver")
@Data
public class NaverProperties {
    private String baseUrl;
    private String clientId;
    private String clientSecret;
}
