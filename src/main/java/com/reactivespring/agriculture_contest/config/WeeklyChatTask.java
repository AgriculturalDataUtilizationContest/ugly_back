package com.reactivespring.agriculture_contest.config;

import com.reactivespring.agriculture_contest.service.ChatService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WeeklyChatTask {

    private final ChatService chatService;

    public WeeklyChatTask(ChatService chatService) {
        this.chatService = chatService;
    }

    // TODO : 작동하는지 확인!
    // 매주 월요일 오전 9시에 실행해도 될 듯
    @Scheduled(cron = "0 0 9 ? * MON", zone = "Asia/Seoul")
    public void generateWeeklyCropSummary() {
        chatService.askAndSave(List.of("고구마","감자","배추","양배추","상추","수박","참외","호박","딸기","무","양파","파","사과","배","감귤")); // 매주 월에 대해 요청
    }
}