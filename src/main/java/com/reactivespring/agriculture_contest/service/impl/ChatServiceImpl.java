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

    // transaction으로 바꿔야함..
    @Override
    public void askAndSave(List<String> cropLst) {
        for (String crop : cropLst) {
            try {
                // cropId 가져오기
                Integer cropId = cropRepository.findByCropKorName(crop).getCropId();

                // 기존 요약이 존재하는지 확인
                TbCropSummary existing = repository.findByCropId(cropId);
                if (existing == null) {
                    log.info("요약이 존재하지 않으므로 저장하지 않음: crop={}", crop);
                    continue;  // 존재하지 않으면 무시
                }

                // 프롬프트 구성
                String prompt = LocalDate.now()
                        + " 기준으로 한국 농산물 중 " + crop
                        + "와 관련된 유통 구조 변화에 대해 요약해줘. 직거래, 산지 직송, 도매시장 등 중심으로."
                        + "토큰수가 250정도 다다르면, 그 문장을 마지막으로 마무리해줘! (중간에 문장이 끊기지 않도록!)";

                // GPT 호출
                String content = chatClient.prompt()
                        .user(prompt)
                        .call()
                        .content();

                // 기존 객체에 값만 수정
                existing.setSummary(content);
                repository.save(existing);  // 변경 내용 저장
            } catch (Exception e) {
                log.error("GPT 요청 실패 crop={}", crop, e);
            }
        }
    }
}
