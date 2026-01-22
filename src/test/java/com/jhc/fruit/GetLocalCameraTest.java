package com.jhc.fruit;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;
import com.jhc.fruit.common.QiniuUtils;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author ：lcy
 * @description：获取本机摄像头测试
 * @date ：2022/5/27 0027 8:24
 */
@SpringBootTest
public class GetLocalCameraTest {
    private static JFrame window;
    private static int num = 0;

    @Test
    public void testLocal2Cloud() throws Exception {
        String url = "D:\\testdjl\\test1.jpg";
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
        QiniuUtils.upload2Qiniu(data,fileName);
        file.delete();
    }

    @Test
    public void test(){
        System.setProperty("java.awt.headless", "false");
        //1.1连接获取到本机的摄像头对象
        Webcam webcam = Webcam.getDefault();
        //1.2设置画面的窗口大小
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        //1.3获取WebcamPanel类，摄像头面板类为摄像头设置参数
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
                        JOptionPane.showMessageDialog(null,"拍照成功");
                        button.setEnabled(true);
                        return ;
                    }
                });
            }
        });
    }

    @Test
    public void test2(){
        System.setProperty("java.awt.headless", "false");
        final Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        //Webcam.setDriver(new NativeWebcamDriver());
        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setFPSDisplayed(true);
        panel.setDisplayDebugInfo(true);
        panel.setImageSizeDisplayed(true);
        panel.setMirrored(true);

        final JFrame window = new JFrame("摄像头");

        window.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e)//关闭时的操作，windowClosed是关闭后的操作
            {
                webcam.close();
                window.dispose();
            }
        });
        final JButton button = new JButton("截图");
        window.add(panel, BorderLayout.CENTER);
        window.add(button, BorderLayout.SOUTH);
        window.setResizable(true);
        window.pack();
        window.setVisible(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                button.setEnabled(false);
                String fileName = "D://" + num;
                WebcamUtils.capture(webcam, fileName, ImageUtils.FORMAT_PNG);
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run()
                    {
                        JOptionPane.showMessageDialog(null, "截图成功");
                        button.setEnabled(true);
                        num++;
                        return;
                    }
                });
            }
        });
    }

    /**
     * 测试摄像头
     * @throws Exception
     */
    @Test
    public void test3() throws Exception {
        System.setProperty("java.awt.headless", "false");
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);//新建opencv抓取器，一般的电脑和移动端设备中摄像头默认序号是0，不排除其他情况
        grabber.start();//开始获取摄像头数据

        CanvasFrame canvas = new CanvasFrame("摄像头预览");//新建一个预览窗口
        canvas.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //窗口是否关闭
        while(canvas.isDisplayable()){
            /*获取摄像头图像并在窗口中显示,这里Frame frame=grabber.grab()得到是解码后的视频图像*/
            canvas.showImage(grabber.grab());
        }
        grabber.close();//停止抓取
    }

    @Test
    public void utilTest(){
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
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
                        JOptionPane.showMessageDialog(null,"拍照成功");
                        button.setEnabled(true);
                        return ;
                    }
                });
            }
        });
    }
}
