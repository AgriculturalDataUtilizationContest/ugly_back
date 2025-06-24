package com.reactivespring.agriculture_contest.service.impl;

import com.reactivespring.agriculture_contest.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final S3Client s3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.region.static}")
    private String region;

    /* ────────── public API ────────── */

    @Override
    public String uploadFiles(MultipartFile multipart, String dir) throws IOException {
        File temp = convert(multipart)
                .orElseThrow(() -> new IOException("Multipart → File 변환 실패"));
        return upload(temp, dir);
    }

    /* ────────── 내부 메서드 ────────── */

    private String upload(File file, String dir) {
        String key = dir + UUID.randomUUID() + "_" + file.getName();

        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType("image/png")
                .build();

        s3.putObject(req, RequestBody.fromFile(file.toPath()));
        removeTempFile(file);

        // 리전은 클라이언트에 이미 들어 있으므로 그대로 사용
        return "https://%s.s3.%s.amazonaws.com/%s".formatted(bucket, region, key);
    }

    private void removeTempFile(File target) {
        if (target.delete()) {
            System.out.println("임시 파일 삭제 성공: " + target.getName());
        } else {
            System.out.println("임시 파일 삭제 실패: " + target.getName());
        }
    }

    private Optional<File> convert(MultipartFile src) throws IOException {
        File temp = File.createTempFile("temp", src.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(temp)) {
            fos.write(src.getBytes());
        }
        return Optional.of(temp);
    }
}
