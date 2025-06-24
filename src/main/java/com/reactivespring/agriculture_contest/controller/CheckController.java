package com.reactivespring.agriculture_contest.controller;

import com.reactivespring.agriculture_contest.config.WeeklyChatTask;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CheckController {

    private final WeeklyChatTask weeklyChatTask;

    public CheckController(WeeklyChatTask weeklyChatTask) {
        this.weeklyChatTask = weeklyChatTask;
    }

    @RequestMapping("/check")
    public ResponseEntity<?> check() {
        weeklyChatTask.generateWeeklyCropSummary();
        return ResponseEntity.ok().build();
    }

}
