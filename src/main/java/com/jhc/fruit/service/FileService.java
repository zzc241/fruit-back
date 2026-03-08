package com.jhc.fruit.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

public interface FileService {
    String upload(MultipartFile file);

    void preview(String fileName, HttpServletResponse response);

    InputStream getMinioFileInputStream(String fileName);

    List<String> uploads(List<MultipartFile> files);

    String getImageUrl(String fileName);

    List<String> getImageUrls(List<String> files);

    String save(MultipartFile qrOfString, String fileName);
}
