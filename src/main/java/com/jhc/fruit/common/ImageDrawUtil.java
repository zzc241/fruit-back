package com.jhc.fruit.common;

import com.jhc.fruit.entity.AIWeeds;
import com.jhc.fruit.service.FileService;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component // 改为 Spring 管理的组件
public class ImageDrawUtil {

    @Resource
    private FileService fileService;

    /**
     * 核心流程：从 Minio 读原始图 → 画框 → 上传到 Minio
     * @param originalMinioPath 原始图片在 Minio 的相对路径（如 "AIWeed/origin/xxx.jpg"）
     * @param outputPrefix       输出图片的 Minio 前缀（如 "AIWeed/output"）
     * @param detections         杂草检测结果列表
     * @return 画好框的图片在 Minio 的相对路径
     * @throws IOException 图片处理/上传异常
     */
    public String drawWeedBoxesAndUploadToMinio(String originalMinioPath,
                                                String outputPrefix,
                                                List<AIWeeds.WeedDetection> detections) throws IOException {
        // 1. 从 Minio 读取原始图片
        try (InputStream originalInputStream = fileService.getMinioFileInputStream(originalMinioPath)) {
            BufferedImage originalImage = ImageIO.read(originalInputStream);
            if (originalImage == null) {
                throw new IOException("无法从 Minio 读取原始图片，请检查路径：" + originalMinioPath);
            }

            // 2. 执行画框逻辑
            BufferedImage drawnImage = drawWeedBoxes(originalImage, detections);

            // 3. 将画好框的 BufferedImage 转为 MultipartFile
            String originalFileName = originalMinioPath.substring(originalMinioPath.lastIndexOf("/") + 1);
            String outputFileName = "drawn_" + originalFileName; // 输出文件名加前缀
            MultipartFile drawnMultipartFile = bufferedImageToMultipartFile(drawnImage, outputFileName);

            // 4. 上传到 Minio，返回相对路径
            return fileService.save(drawnMultipartFile, outputPrefix);
        }
    }

    /**
     * 在图片上画杂草检测框（返回字节数组，适合直接返回给前端）
     * @param originalImage 原始图片的 BufferedImage 对象
     * @param detections    杂草检测结果列表
     * @param format        输出图片格式（如 "jpg", "png"）
     * @return 画好框的图片字节数组
     * @throws IOException 图片处理异常
     */
    public byte[] drawWeedBoxesToBytes(BufferedImage originalImage,
                                       List<AIWeeds.WeedDetection> detections,
                                       String format) throws IOException {
        BufferedImage drawnImage = drawWeedBoxes(originalImage, detections);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(drawnImage, format, baos);
        return baos.toByteArray();
    }

    /**
     * 核心画框逻辑（私有方法）
     */
    private BufferedImage drawWeedBoxes(BufferedImage image, List<AIWeeds.WeedDetection> detections) {
        BufferedImage drawnImage = new BufferedImage(
                image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = drawnImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);

        // 设置画笔属性
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(3));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 遍历画框
        if (detections != null && !detections.isEmpty()) {
            for (AIWeeds.WeedDetection detection : detections) {
                int x = detection.getX1().intValue();
                int y = detection.getY1().intValue();
                int width = detection.getX2().intValue() - x;
                int height = detection.getY2().intValue() - y;

                g2d.drawRect(x, y, width, height);

                // 画标签（杂草名称 + 置信度）
                String label = String.format("%s (%.2f%%)",
                        detection.getClassName(), detection.getConfidence() * 100);
                g2d.drawString(label, x, y - 5);
            }
        }

        g2d.dispose();
        return drawnImage;
    }

    /**
     * BufferedImage 转 MultipartFile（适配 FileService.save()）
     */
    private MultipartFile bufferedImageToMultipartFile(BufferedImage image, String fileName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String format = fileName.substring(fileName.lastIndexOf(".") + 1);
        ImageIO.write(image, format, baos);
        byte[] imageBytes = baos.toByteArray();

        // 用 MockMultipartFile 构造 MultipartFile
        return new MockMultipartFile(
                "file",                // 参数名
                fileName,              // 文件名
                "image/" + format,     // Content-Type
                new ByteArrayInputStream(imageBytes)
        );
    }
}