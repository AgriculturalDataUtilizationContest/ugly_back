package com.reactivespring.agriculture_contest.service.impl;//package com.reactivespring.agriculture_contest.service.impl;

import com.reactivespring.agriculture_contest.entity.TbCropSummary;
import com.reactivespring.agriculture_contest.repository.CropRepository;
import com.reactivespring.agriculture_contest.repository.CropSummaryRepository;
import com.reactivespring.agriculture_contest.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;
    private final CropSummaryRepository repository;
    private final CropRepository cropRepository;

    @Override
    public void askAndSave(List<String> cropLst) {
        for (String crop : cropLst) {
            try {
                // set prompt
                String prompt = LocalDate.now()
                        + " 기준으로 한국 농산물 중 " + crop
                        + "와 관련된 유통 구조 변화에 대해 요약해줘. 직거래, 산지 직송, 도매시장 등 중심으로.";

                // connect GPT API
                String content = chatClient.prompt()
                        .user(prompt)
                        .call()
                        .content();  // response content

                // save to database
                TbCropSummary summary = TbCropSummary.builder()
                        .cropId(cropRepository.findByCropKorName(crop).getCropId())
                        .summary(content)
                        .build();

                repository.save(summary);
            } catch (Exception e) {
                log.error("GPT 요청 실패 crop={}", crop, e);
            }
        }
    }
}
