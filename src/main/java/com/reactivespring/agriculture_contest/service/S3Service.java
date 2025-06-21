package com.reactivespring.agriculture_contest.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public interface S3Service {
    public String uploadFiles(MultipartFile multipartFile, String dirName) throws IOException;
    String upload(File uploadFile, String dirName);
    void removeNewFile(File targetFile);
}
