//package com.reactivespring.agriculture_contest.service.impl;
//
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.reactivespring.agriculture_contest.service.S3Service;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.Optional;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class S3ServiceImpl implements S3Service {
//
//    private final AmazonS3 amazonS3;
//
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;
//
//    // MultipartFile -> File로 변환 후 업로드
//    public String uploadFiles(MultipartFile multipartFile, String dirName) throws IOException {
//        File uploadFile = convert(multipartFile)
//                .orElseThrow(() -> new IOException("MultipartFile -> File 변환 실패"));
//        return upload(uploadFile, dirName);
//    }
//
//    // S3에 파일 업로드
//    public String upload(File uploadFile, String dirName) {
//        String fileName = dirName + UUID.randomUUID() + "_" + uploadFile.getName(); // 파일명 생성
//        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile)); // S3 업로드 :: ACL 설정 해준 사람을 위해서 추가해 둠.
//        removeNewFile(uploadFile); // 임시 파일 삭제
//        return amazonS3.getUrl(bucket, fileName).toString();
//    }
//
//    // 로컬 임시 파일 삭제
//    // 임시 파일 만드는 이유 : MultipartFile(Spring의 파일 형식)은 바로 PutObjectRequest에 사용 불가!
//    // 따라서 MultipartFile을 File로 변환하는 과정이 있는 것! (file을 쓰는 것이 전통적인 방식!)
//    public void removeNewFile(File targetFile) {
//        if (targetFile.delete()) {
//            System.out.println("임시 파일 삭제 성공: " + targetFile.getName());
//        } else {
//            System.out.println("임시 파일 삭제 실패: " + targetFile.getName());
//        }
//    }
//
//    // MultipartFile -> File 변환
//    private Optional<File> convert(MultipartFile file) throws IOException {
//        File convertFile = File.createTempFile("temp", file.getOriginalFilename()); // 임시 파일 생성
//        try (FileOutputStream fos = new FileOutputStream(convertFile)) {
//            fos.write(file.getBytes());
//        }
//        return Optional.of(convertFile);
//    }
//}


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
