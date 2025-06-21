package com.reactivespring.agriculture_contest.controller;

// AWS, IAM 설정: https://velog.io/@mingsound21/SpringBoot-AWS-S3%EB%A1%9C-%EC%9D%B4%EB%AF%B8%EC%A7%80-%EC%97%85%EB%A1%9C%EB%93%9C 이거 기반으로 하면 됨!

import com.reactivespring.agriculture_contest.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class S3Controller {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket; // S3 버킷 이름

    private final S3Service s3Service;

    // 단일 파일 업로드 처리
    @PostMapping("/image")
    public ResponseEntity<String> updateUserImage(@RequestParam("images") MultipartFile multipartFile) {
        try {
            if (multipartFile == null) {
                return ResponseEntity.badRequest().body("업로드할 파일이 없습니다.");
            }

            String uploadUrl = s3Service.uploadFiles(multipartFile, "likelion-study/");
            return ResponseEntity.ok(uploadUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이미지 업로드 중 예기치 않은 오류 발생: " + e.getMessage());
        }
    }

    // 다중 파일 업로드 처리
    @PostMapping("/images")
    public ResponseEntity<String> updateUserImages(@RequestParam("images") MultipartFile[] multipartFiles) {
        if (multipartFiles == null || multipartFiles.length == 0) {
            return ResponseEntity.badRequest().body("업로드할 파일이 없습니다.");
        }

        StringBuilder uploadUrls = new StringBuilder();
        try {
            for (MultipartFile multipartFile : multipartFiles) {
                if (multipartFile == null) {
                    continue;
                }
                String uploadUrl = s3Service.uploadFiles(multipartFile, "likelion-study/");
                uploadUrls.append(uploadUrl).append("\n");
            }
            return ResponseEntity.ok(uploadUrls.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이미지 업로드 중 예기치 않은 오류 발생: " + e.getMessage());
        }
    }
}
