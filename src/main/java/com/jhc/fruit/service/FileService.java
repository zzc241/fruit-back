package com.jhc.fruit.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

public interface FileService {
    String upload(MultipartFile file);

    void preview(String fileName, HttpServletResponse response);

    InputStream getMinioFileInputStream(String fileName);
}
