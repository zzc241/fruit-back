package com.jhc.fruit.common;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
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

    //生成二维码方法(定向到指定网页)
    public static MultipartFile createQROfURL(String url){
        //1.定义二维码属性
        int width = 300;    //定义图片宽度
        int height = 300;   //定义图片高度
        String format = "png";  //定义图片格式
        String content = url; //定义二维码内容

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
                    "QR" + UUID.randomUUID().toString().replace("-" , "") + ".png", // 文件名
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

    public static List<MultipartFile> createQRList(List<String> urls) {
        List<MultipartFile> files = new ArrayList<>();
        for (String url : urls) {
            files.add(createQROfURL(url));
        }
        return files;
    }
}
