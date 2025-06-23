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

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    @GetMapping("/update/kamis/data")
    public ResponseEntity<Void> updateKamisData() {
        return ResponseEntity.ok(regularExecutionService.updateKamisData());
    }

    @Scheduled(cron = "0 10 0 * * *", zone = "Asia/Seoul")
    @GetMapping("/update/pred/data")
    public ResponseEntity<Void> updatePredData() {
        return ResponseEntity.ok(regularExecutionService.updatePredData());
    }

}
