package com.jhc.fruit.common;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/27 0027 16:47
 */
public class CameraUtil {
    /**
     * 获取本地摄像头画面并拍照
     * @return
     */
    public static String getPhoto(){
        //否则会报异常
        System.setProperty("java.awt.headless", "false");
        //1.1连接获取到本机的摄像头对象
        Webcam webcam = Webcam.getDefault();
        //1.2设置画面的窗口大小
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        //1.3获取WebcamPanel类，获取摄像头画面
        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setFPSDisplayed(true);
        panel.setDisplayDebugInfo(true);
        panel.setImageSizeDisplayed(true);
        panel.setMirrored(true);

        //2.1JFrame实例初始化
        JFrame window = new JFrame("Test webcam panel");
        //2.2window对象添加上WebcamPanel对象
        window.add(panel);
        //2.3window对象设置窗口可调节
        window.setResizable(true);
        //2.4设置退出应用程序默认窗口关闭
        //window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //该方法连当前应用也会直接结束
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //2.5适应其子组件的首选大小和布局
        window.pack();
        //2.6设置窗口可视
        window.setVisible(true);

        //3.1创建拍照按钮
        final JButton button = new JButton("拍照");
        window.add(panel, BorderLayout.CENTER);
        window.add(button,BorderLayout.SOUTH);
        window.setResizable(true);
        window.pack();
        window.setVisible(true);
        //在窗口关闭时关闭摄像机
        window.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                webcam.close();
            }
        });
        final String[] result = {null};
        //为点击拍照按钮添加事件
        button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //button.setEnabled(false);  //设置按钮不可点击

                //实现拍照保存
                String fileName = "D://testdjl//" + System.currentTimeMillis();     //保存路径即图片名称(不需要加后缀)
                WebcamUtils.capture(webcam,fileName, ImageUtils.FORMAT_PNG);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //将图片上传至服务器
                           result[0] = local2Cloud(fileName + ".png");
                            JOptionPane.showMessageDialog(null,"拍照成功,图片：" + result[0]);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        System.out.println("拍照成功");
                        button.setEnabled(true);
                        return ;
                    }
                });
            }
        });
        while (webcam.isOpen() && result[0] == null){
            try{
                Thread.sleep(50000);
            } catch(Exception e){
                e.printStackTrace();
            }

        }
        webcam.close();
        return result[0];
    }

    /**
     * 将本地拍摄得到照片上传至服务器
     * @param url 本地文件目录
     * @return  服务器上文件的地址
     * @throws Exception
     */
    public static String local2Cloud(String url) throws Exception{
        File file = new File(url);
        String fileName = file.getName();
        System.out.println(fileName);
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
        byte[] b = new byte[1000];
        int n;
        while((n = fis.read(b))!=-1){
            bos.write(b,0,n);
        }
        fis.close();
        byte[] data = bos.toByteArray();
        bos.close();
        System.out.println(data);
        // QiniuUtils.upload2Qiniu(data,fileName);
        file.delete();
        System.out.println("图片上传成功");
        String destination = "http://fruitphoto.lookyousmileface.icu/" + fileName;
        return destination;
    }
}
