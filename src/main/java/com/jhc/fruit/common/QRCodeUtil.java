package com.jhc.fruit.common;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author ：lcy
 * @description：二维码生成/解析类
 * @date ：2022/5/17 0017 14:16
 */
public class QRCodeUtil {

    private static final String treeUrl = "https://127.0.0.1:3388/fruitTree/";

    //生成二维码方法
    public static MultipartFile createTreeQRCode(Long id){
        //1.定义二维码属性
        int width = 300;    //定义图片宽度
        int height = 300;   //定义图片高度
        String format = "png";  //定义图片格式
        String content = treeUrl + id; //定义二维码内容，也可以是图片或者字符串

        //2.定义二维码中参数
        HashMap hashMap = new HashMap<>();
        hashMap.put(EncodeHintType.CHARACTER_SET,"utf-8");  //设置编码
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);   //设置容错等级，等级越高，容量越小
        hashMap.put(EncodeHintType.MARGIN,2);   //设置边距

        //3.生成二维码图片
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            //生成矩阵                                          存入二维码的内容    格式              宽     高
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE,width,height,hashMap);
            //将二维码写入字节输出流
            MatrixToImageWriter.writeToStream(bitMatrix, format, outputStream);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            // 构建 MockMultipartFile（参数：字段名、文件名、文件类型、输入流）
            MultipartFile multipartFile = new MockMultipartFile(
                    "QRPath",                // 请求中的字段名（可自定义）
                    "fruitTree_" + id + ".png", // 文件名
                    "image/png",             // 文件MIME类型
                    inputStream              // 二维码字节输入流
            );

            return multipartFile;
        } catch (WriterException e) {
            throw new RuntimeException("二维码生成失败：" + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("字节流处理失败：" + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("二维码生成异常：" + e.getMessage(), e);
        }
    }

    /**
     * 生成带文字的二维码（二维码下方展示指定文本）
     * @param str 二维码内容（同时也是下方要展示的文本）
     * @return 包含二维码+文字的MultipartFile
     */
    public static MultipartFile createQROfString(String str) {
        // 1. 基础配置
        int qrWidth = 300;          // 二维码宽度
        int qrHeight = 300;         // 二维码高度
        String format = "png";      // 图片格式
        String content = str;       // 二维码内容
        int textSpace = 20;         // 二维码和文字之间的间距
        int textHeight = 40;        // 文字区域高度
        int totalWidth = qrWidth;   // 整体图片宽度（和二维码一致）
        int totalHeight = qrHeight + textSpace + textHeight; // 整体高度=二维码+间距+文字区

        // 2. 二维码参数配置
        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");          // 编码格式
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M); // 容错等级
        hints.put(EncodeHintType.MARGIN, 2);                       // 二维码边距

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // 3. 生成二维码矩阵
            BitMatrix bitMatrix = new MultiFormatWriter()
                    .encode(content, BarcodeFormat.QR_CODE, qrWidth, qrHeight, hints);

            // 4. 将二维码矩阵转为BufferedImage
            MatrixToImageConfig config = new MatrixToImageConfig(0xFF000000, 0xFFFFFFFF); // 黑码白底
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, config);

            // 5. 创建新的BufferedImage，容纳二维码+文字
            BufferedImage finalImage = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = finalImage.createGraphics();

            // 5.1 设置背景为白色
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, totalWidth, totalHeight);

            // 5.2 绘制二维码（居中）
            int qrX = (totalWidth - qrWidth) / 2; // 二维码X坐标（居中）
            int qrY = 0;                          // 二维码Y坐标（顶部）
            g2d.drawImage(qrImage, qrX, qrY, null);

            // 5.3 绘制文字（二维码下方居中）
            g2d.setColor(Color.BLACK); // 文字颜色
            Font font = new Font("宋体", Font.PLAIN, 16); // 字体、样式、大小
            g2d.setFont(font);

            // 计算文字居中的坐标
            FontRenderContext frc = g2d.getFontRenderContext();
            Rectangle2D textBounds = font.getStringBounds(str, frc);
            int textX = (int) ((totalWidth - textBounds.getWidth()) / 2);
            int textY = qrHeight + textSpace + (int) (textBounds.getHeight()); // 文字Y坐标

            // 绘制文字（防止文字过长换行，这里做简单截断）
            String displayText = str.length() > 20 ? str.substring(0, 17) + "..." : str; // 过长截断
            g2d.drawString(displayText, textX, textY);

            // 释放资源
            g2d.dispose();

            // 6. 将最终图片写入字节流
            ImageIO.write(finalImage, format, outputStream);

            // 7. 转为MultipartFile返回
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            return new MockMultipartFile(
                    "QRPath",
                    UUID.randomUUID().toString().replace("-", "") + ".png",
                    "image/png",
                    inputStream
            );

        } catch (WriterException e) {
            throw new RuntimeException("二维码矩阵生成失败：" + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("图片流处理失败：" + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("二维码生成异常：" + e.getMessage(), e);
        }
    }

    //解析二维码方法
    public static Long readTreeQRCode(InputStream inputStream){
        try{
            /**
             * MultiFormatReader 多格式读取
             */
            MultiFormatReader formatReader = new MultiFormatReader();
            //转换数据类型
            BufferedImage bufferedImage = ImageIO.read(inputStream);

            //BinaryBitmap-二进制位图                    HybridBinarizer 混合二值化器      BufferedImageLuminanceSource-图像缓存区
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
            //定义二维码参数
            HashMap hashMap = new HashMap();
            hashMap.put(EncodeHintType.CHARACTER_SET, "utf-8");//编码方式
            //解码,然后获取内容
            String qrContent = formatReader.decode(binaryBitmap,hashMap).getText();
            //提取果树id
            Integer index = qrContent.lastIndexOf("/");
            Long treeId = Long.parseLong(qrContent.substring(index + 1));
            return treeId;
/*            System.out.println("解析结果：" + result.toString());
            System.out.println("二维码格式类型："+result.getBarcodeFormat());//BarcodeFormat   条形码格式
            System.out.println("二维码文本内容："+result.getText());*/
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("未识别到二维码，请上传有效的二维码图片", e);
        }
    }

    //解析二维码方法
    public static String readQRCode(InputStream inputStream){
        try{
            /**
             * MultiFormatReader 多格式读取
             */
            MultiFormatReader formatReader = new MultiFormatReader();
            //转换数据类型
            BufferedImage bufferedImage = ImageIO.read(inputStream);

            //BinaryBitmap-二进制位图                    HybridBinarizer 混合二值化器      BufferedImageLuminanceSource-图像缓存区
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
            //定义二维码参数
            HashMap hashMap = new HashMap();
            hashMap.put(EncodeHintType.CHARACTER_SET, "utf-8");//编码方式
            //解码,然后获取内容
            String qrContent = formatReader.decode(binaryBitmap,hashMap).getText();
            return qrContent;

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("未识别到二维码，请上传有效的二维码图片", e);
        }
    }

    public static List<MultipartFile> createQRList(List<String> strs) {
        List<MultipartFile> files = new ArrayList<>();
        for (String str : strs) {
            files.add(createQROfString(str));
        }
        return files;
    }
}
