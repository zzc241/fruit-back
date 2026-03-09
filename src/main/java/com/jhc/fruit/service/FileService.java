package com.jhc.fruit.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface FileService {
    String upload(MultipartFile file);

    void preview(String fileName, HttpServletResponse response);

    InputStream getMinioFileInputStream(String fileName);

    List<String> uploads(List<MultipartFile> files);

    String getImageUrl(String fileName);

    List<String> getImageUrls(List<String> files);

    String save(MultipartFile file, String path);

    boolean isPresignedUrlExpired(String presignedUrl);

    Map<String, Object> parsePresignedUrl(String presignedUrl);

    Boolean exits(String fileName);

    Boolean remove(String fileName);
}
