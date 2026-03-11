package com.jhc.fruit;

import com.jhc.fruit.common.BaseContext;
// import com.jhc.fruit.common.QiniuUtils;
import com.jhc.fruit.common.R;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
// import org.bytedeco.javacv.FrameGrabber;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/26 0026 9:11
 */
@Slf4j
@SpringBootTest
public class ImgTest {
    /**
     * 图片上传七牛云测试
     * @throws Exception
     */
    // @Test
    // public void test() throws Exception{
    //     for(int i = 1; i <=1 ; i ++){
    //         String strUrl = "D:\\testdjl\\test3";
    //         strUrl =  strUrl + ".jpg";
    //         File file = new File(strUrl);
    //         InputStream inputStream = new FileInputStream(file);
    //         MultipartFile imgFile = new MockMultipartFile(file.getName(), inputStream);
    //         log.info("file转multipartFile成功. {}",imgFile);
    //         //获取传入文件的后缀名
    //         //①获取传入文件的原始文件名
    //         String name = imgFile.getName();
    //         //②获取文件名中最后一个.的位置（此位置+1就是后缀）
    //         int index = name.lastIndexOf(".");
    //         //③截取字符串（后缀名）
    //         String extention = name.substring(index - 1);

    //         //文件名如果重复则会进行覆盖（防止文件名重复）
    //         String fileName = UUID.randomUUID().toString() + extention;
    //         System.out.println(fileName);
    //         System.out.println("=======fileName=====");
    //         try {
    //             //将文件上川岛七牛云服务器
    //             QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
    //             System.out.println("上传成功");
    //         } catch (IOException e) {
    //             e.printStackTrace();
    //             System.out.println("上传失败");
    //         }
    //     }

    // }

    // /**
    //  * 根据名字删除七牛云中存储文件
    //  */
    // @Test
    // public void test1() throws Exception {
    //     String strUrl = "http://192.168.72.209:5000/tmp/draw/fruit.jpg";
    //     String s = QiniuUtils.downloadImg2Cloud(strUrl);
    //     System.out.println(s);
    // }

    // @Test
    // public void test3(){
    //     BaseContext.setCurrentId(3L);
    //     System.out.println(BaseContext.getId());

    // }
}
