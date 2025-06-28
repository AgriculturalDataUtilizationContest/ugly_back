package com.reactivespring.agriculture_contest.service.impl;

import com.reactivespring.agriculture_contest.service.RegularExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class RegularExecutionServiceImpl implements RegularExecutionService {

    private final WebClient webClient;

    public RegularExecutionServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Void updateKamisData() {
        webClient.get()
                .uri("/pyapi/kamis")
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        return null;
    }

    @Override
    public Void updatePredData() {
        webClient.get()
                .uri("/pyapi/pred_all")
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        return null;
    }

}
