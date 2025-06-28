package com.reactivespring.agriculture_contest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${fastapi.base-url}")
    public String fastApiBaseUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(fastApiBaseUrl)
                .build();
    }
}
