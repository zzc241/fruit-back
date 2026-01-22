package com.jhc.fruit.common;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

/**
 * @author ：lcy
 * @description：七牛云工具类
 * @date ：2022/5/26 0026 8:53
 */
public class QiniuUtils {

    public  static String accessKey = "EDzV3KKFtQX9u1CBK5tn1BOwGhynCXXUgW0PnbH2";
    public  static String secretKey = "znztvg0KIN1I0xWxaHmhWHEXLZwvNTmNx7cwd3f-";
    public  static String bucket = "jhcfruits";

    /**
     * 长传图片到七牛云存储
     * @param bytes 文件的字节数组
     * @param fileName  文件名字
     */
    //上传图片到七牛云存储
    public static void upload2Qiniu(byte[] bytes,String fileName){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = fileName;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(bytes, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    /**
     * 删除七牛云中存储的文件
     * @param fileName 存储空间中文件的名称
     */
    public static void deleteFileFromQiniu(String fileName){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        String key = fileName;
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
            System.out.println("删除成功");
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }

    /**
     * 将本地文件转化为MultipartFile
     * @param localUrl
     * @return
     */
    public static MultipartFile transform2File(String localUrl){
        File file = null;
        FileInputStream fis = null;
        MultipartFile multipartFile = null;
        try{
            //1.创建File类
            file = new File(localUrl);
            //2.获取文件输入流
            fis = new FileInputStream(file);
            //3.创建MultipartFile对象
            multipartFile = new MockMultipartFile(file.getName(),file.getName(),ContentType.APPLICATION_OCTET_STREAM.toString(),fis);
        } catch (Exception e){
            e.printStackTrace();
        }
        return multipartFile;
    }

    /**
     * 格式化上传，使用MultipartFile类型接收文件上传至服务器
     * @param imgFile
     * @return
     */
    public static String formatUpload(MultipartFile imgFile){
        //获取传入文件的后缀名
        //①获取传入文件的原始文件名
        String originalFilename = imgFile.getOriginalFilename();
        //②获取文件名中最后一个.的位置（此位置+1就是后缀）
        int index = originalFilename.lastIndexOf(".");
        //③截取字符串（后缀名）
        String extention = originalFilename.substring(index - 1);

        //文件名如果重复则会进行覆盖（防止文件名重复）
        String fileName = UUID.randomUUID().toString() + extention;
        System.out.println(fileName);
        try {
            //将文件上川岛七牛云服务器
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            System.out.println(fileName);
            fileName = "http://fruitphtoto.lookyousmileface.icu/" + fileName;
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "上传失败";
        }
    }

    public static String downloadImg2Cloud(String sourceUrl){

        try {
            URL url = new URL(sourceUrl);
            // 打开连接
            URLConnection con = url.openConnection();
            //设置请求超时为5s
            con.setConnectTimeout(5*1000);
            // 输入流
            InputStream is = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            File sf=new File("D:\\testdjl");
            if(!sf.exists()){
                sf.mkdirs();
            }
            String location = sf.getPath()+"\\"+System.currentTimeMillis()+".jpg";
            //保存路径和文件名，图片类型
            OutputStream os = new FileOutputStream(location);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                //开始保存
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
            //将File转换为MultipartFile类型上传至服务器
            File file = new File(location);
            FileInputStream fis = new FileInputStream(file);
            MultipartFile imgFile = new MockMultipartFile(file.getName(),file.getName(),ContentType.APPLICATION_OCTET_STREAM.toString(),fis);
            //格式化上传至服务器
            String fileName = formatUpload(imgFile);
            //上传至服务器后，删除本地冗余文件
            file.delete();
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "失败";
        }
    }


}
