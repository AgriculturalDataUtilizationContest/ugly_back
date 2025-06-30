package com.reactivespring.agriculture_contest.service.impl;

import com.reactivespring.agriculture_contest.dto.CropDto;
import com.reactivespring.agriculture_contest.service.GrainV5Fetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GrainV5FetcherImpl implements GrainV5Fetcher {

    private final WebClient webClient;

    // 지금은 max로 한 page에 20번 호출.
    // 앞으로 얼마나 늘어날지 모르니, thread를 사용하고자함.
    @Override
    public Mono<Map<Integer, Double>> fetchAllV5(List<Integer> grainIds) {
        return Flux.fromIterable(grainIds)
                .flatMap(grain_id ->
                        webClient.get()
                                .uri("/pyapi/today_v5/{grainId}", grain_id)
                                .retrieve()
                                .bodyToMono(CropDto.V5Response.class)
                                .map(res -> Map.entry(grain_id, res.getV5()))
                                .onErrorReturn(Map.entry(grain_id, -1.0)) // 실패 시 -1.0 반환 -> 확인이 용이
                )
                .collectMap(Map.Entry::getKey, Map.Entry::getValue);
    }
}
