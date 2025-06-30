package com.reactivespring.agriculture_contest.service.impl;

import com.reactivespring.agriculture_contest.service.RegularExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final RegularExecutionService regularExecutionService;

    @Scheduled(cron = "0 5 15 * * *", zone = "Asia/Seoul")
    public void scheduledUpdateKamisData() {
        System.out.println("[스케줄러] Kamis 데이터 업데이트 실행됨");
        regularExecutionService.updateKamisData();
    }

    @Scheduled(cron = "0 35 15 * * *", zone = "Asia/Seoul")
    public void scheduledUpdatePredData() {
        System.out.println("[스케줄러] Pred 데이터 업데이트 실행됨");
        regularExecutionService.updatePredData();
    }
}
