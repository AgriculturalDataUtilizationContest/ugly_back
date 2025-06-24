package com.reactivespring.agriculture_contest.controller;

import com.reactivespring.agriculture_contest.util.WeeklyChatTask;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CheckController {

//    private final CropServiceImpl cropService;
    private final WeeklyChatTask weeklyChatTask;

    public CheckController(WeeklyChatTask weeklyChatTask) {
        this.weeklyChatTask = weeklyChatTask;
    }

    @GetMapping("/check")
    public ResponseEntity<?> check() {
        weeklyChatTask.generateWeeklyCropSummary();
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/wordCloud")
//    public String wordCloud() {
//        return cropService.getWordCloud("사과");
//    }

}
