package com.reactivespring.agriculture_contest.controller;

import com.reactivespring.agriculture_contest.service.RegularExecutionService;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/py")
public class RegularExecutionController {

    private final RegularExecutionService regularExecutionService;

    public RegularExecutionController(RegularExecutionService regularExecutionService) {
        this.regularExecutionService = regularExecutionService;
    }

    @GetMapping("/update/kamis/data")
    public ResponseEntity<Void> updateKamisData() {
        return ResponseEntity.ok(regularExecutionService.updateKamisData());
    }

    @GetMapping("/update/pred/data")
    public ResponseEntity<Void> updatePredData() {
        return ResponseEntity.ok(regularExecutionService.updatePredData());
    }

    @Scheduled(cron = "0 5 15 * * *", zone = "Asia/Seoul")
    public void scheduledUpdateKamisData() {
        regularExecutionService.updateKamisData();
    }

    @Scheduled(cron = "0 35 15 * * *", zone = "Asia/Seoul")
    public void scheduledUpdatePredData() {
        regularExecutionService.updatePredData();
    }

}
