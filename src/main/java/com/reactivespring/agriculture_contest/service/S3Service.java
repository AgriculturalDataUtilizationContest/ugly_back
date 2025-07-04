package com.reactivespring.agriculture_contest.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface S3Service {
    String uploadFiles(MultipartFile multipartFile, String dirName) throws IOException;
}
