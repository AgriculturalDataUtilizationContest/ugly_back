package com.reactivespring.agriculture_contest.service;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface GrainV5Fetcher {
    Mono<Map<Integer, Double>> fetchAllV5(List<Integer> grainIds);
}
