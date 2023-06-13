package com.example.githubrepository.controller;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileService {
    public void uploadFile(MultipartFile file) throws IOException {
        file.transferTo(new File("D:\\Files\\"+file.getOriginalFilename()));
    }
}
