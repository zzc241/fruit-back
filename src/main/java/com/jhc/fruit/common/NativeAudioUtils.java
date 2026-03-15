package com.jhc.fruit.common;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 音频文件处理工具类（原生 Java 实现，仅支持 WAV 格式）
 * 核心功能：读取音频、解析格式、播放、截取、格式转换、字节数组互转
 */
public class NativeAudioUtils {

    // ==================== 基础读取与格式解析 ====================

    /**
     * 读取 WAV 文件为 AudioInputStream
     * @param wavFile WAV 音频文件
     * @return 音频输入流
     * @throws UnsupportedAudioFileException 文件格式不支持（非 WAV）
     * @throws IOException 文件读取失败
     */
    public static AudioInputStream readWavFile(File wavFile)
            throws UnsupportedAudioFileException, IOException {
        if (!wavFile.exists() || !wavFile.isFile()) {
            throw new IOException("文件不存在或不是有效文件：" + wavFile.getAbsolutePath());
        }
        return AudioSystem.getAudioInputStream(wavFile);
    }

    /**
     * 从字节数组读取音频（适用于内存中的 WAV 数据）
     * @param audioBytes 音频字节数组
     * @return 音频输入流
     * @throws UnsupportedAudioFileException 格式不支持
     * @throws IOException 流读取失败
     */
    public static AudioInputStream readWavFromBytes(byte[] audioBytes)
            throws UnsupportedAudioFileException, IOException {
        InputStream inputStream = new ByteArrayInputStream(audioBytes);
        return AudioSystem.getAudioInputStream(inputStream);
    }

    /**
     * 解析音频格式信息并打印（调试用）
     * @param format 音频格式对象
     */
    public static void printAudioFormat(AudioFormat format) {
        System.out.println("===== 音频格式信息 =====");
        System.out.println("采样率(Hz)：" + format.getSampleRate());
        System.out.println("位深(bits)：" + format.getSampleSizeInBits());
        System.out.println("声道数：" + format.getChannels() + "（1=单声道，2=立体声）");
        System.out.println("编码格式：" + format.getEncoding());
        System.out.println("帧大小(字节/帧)：" + format.getFrameSize());
        System.out.println("是否大端序：" + format.isBigEndian());
        System.out.println("=======================");
    }

    /**
     * 将音频输入流转为字节数组
     * @param audioInputStream 音频输入流
     * @return 音频原始字节数组
     * @throws IOException 流读取失败
     */
    public static byte[] audioToBytes(AudioInputStream audioInputStream) throws IOException {
        AudioFormat format = audioInputStream.getFormat();
        long frameLength = audioInputStream.getFrameLength();
        int frameSize = format.getFrameSize();

        // 计算总字节数（防止溢出）
        if (frameLength == AudioSystem.NOT_SPECIFIED || frameSize == AudioSystem.NOT_SPECIFIED) {
            throw new IOException("无法获取音频帧信息，无法转换为字节数组");
        }
        long totalBytesLong = frameLength * frameSize;
        if (totalBytesLong > Integer.MAX_VALUE) {
            throw new IOException("音频文件过大，字节数超过 Integer 最大值");
        }
        int totalBytes = (int) totalBytesLong;

        // 读取字节数组
        byte[] audioBytes = new byte[totalBytes];
        int bytesRead = audioInputStream.read(audioBytes);
        if (bytesRead != totalBytes) {
            throw new IOException("读取音频数据不完整，预期：" + totalBytes + "，实际：" + bytesRead);
        }
        return audioBytes;
    }

    // ==================== 音频播放 ====================

    /**
     * 播放 WAV 音频文件（阻塞式，播放完成后返回）
     * @param wavFile WAV 文件
     * @throws UnsupportedAudioFileException 格式不支持
     * @throws IOException 读取失败
     * @throws LineUnavailableException 音频设备不可用
     */
    public static void playWavFile(File wavFile)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream audioInputStream = readWavFile(wavFile);
        playAudio(audioInputStream);
    }

    /**
     * 播放音频输入流（通用方法）
     * @param audioInputStream 音频输入流
     * @throws LineUnavailableException 音频设备不可用
     * @throws IOException 读取失败
     */
    public static void playAudio(AudioInputStream audioInputStream)
            throws LineUnavailableException, IOException {
        AudioFormat format = audioInputStream.getFormat();
        // 检查音频格式是否支持播放
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            throw new LineUnavailableException("不支持的音频格式，无法播放：" + format);
        }

        // 打开音频输出行并播放
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();

        // 分块读取播放（避免内存溢出）
        byte[] buffer = new byte[4096]; // 4KB 缓冲区，可根据需求调整
        int bytesRead;
        while ((bytesRead = audioInputStream.read(buffer)) != -1) {
            line.write(buffer, 0, bytesRead);
        }

        // 释放资源
        line.drain(); // 清空缓冲区
        line.stop();
        line.close();
        audioInputStream.close();
    }

    // ==================== 音频截取（裁剪） ====================

    /**
     * 截取 WAV 音频片段
     * @param sourceFile 源 WAV 文件
     * @param targetFile 目标文件（保存截取后的音频）
     * @param startTime 起始时间（秒）
     * @param duration 截取时长（秒）
     * @throws UnsupportedAudioFileException 格式不支持
     * @throws IOException 读写失败
     */
    public static void cutWavFile(File sourceFile, File targetFile, double startTime, double duration)
            throws UnsupportedAudioFileException, IOException {
        try (AudioInputStream sourceAis = readWavFile(sourceFile)) {
            AudioFormat format = sourceAis.getFormat();
            float sampleRate = format.getSampleRate();
            int frameSize = format.getFrameSize();

            // 计算起始帧和截取总帧数
            long startFrame = (long) (startTime * sampleRate);
            long totalFrames = (long) (duration * sampleRate);

            // 跳过起始帧之前的数据
            long skipBytes = startFrame * frameSize;
            long actualSkip = sourceAis.skip(skipBytes);
            if (actualSkip != skipBytes) {
                throw new IOException("跳过音频数据失败，预期：" + skipBytes + "，实际：" + actualSkip);
            }

            // 截取指定帧数的音频流
            AudioInputStream targetAis = new AudioInputStream(sourceAis, format, totalFrames);

            // 写入目标文件
            AudioSystem.write(targetAis, AudioFileFormat.Type.WAVE, targetFile);
            targetAis.close();
        }
    }

    // ==================== 音频格式转换 ====================

    /**
     * 转换 WAV 音频格式（示例：立体声→单声道，16位→8位，44100Hz→22050Hz）
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     * @param targetSampleRate 目标采样率（如 22050.0f）
     * @param targetChannels 目标声道数（1=单声道，2=立体声）
     * @param targetSampleSize 目标位深（8 或 16）
     * @throws UnsupportedAudioFileException 格式不支持
     * @throws IOException 读写失败
     * @throws LineUnavailableException 音频线路不可用
     */
    public static void convertWavFormat(File sourceFile, File targetFile,
                                        float targetSampleRate, int targetChannels, int targetSampleSize)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        try (AudioInputStream sourceAis = readWavFile(sourceFile)) {
            AudioFormat sourceFormat = sourceAis.getFormat();

            // 定义目标格式（PCM 编码，WAV 通用格式）
            AudioFormat targetFormat = new AudioFormat(
                    sourceFormat.getEncoding(), // 保持编码（PCM_SIGNED/PCM_UNSIGNED）
                    targetSampleRate,          // 目标采样率
                    targetSampleSize,          // 目标位深
                    targetChannels,            // 目标声道数
                    calculateFrameSize(targetSampleSize, targetChannels), // 帧大小
                    targetSampleRate,          // 帧速率（与采样率一致）
                    sourceFormat.isBigEndian() // 保持端序
            );

            // 转换音频流
            AudioInputStream targetAis = AudioSystem.getAudioInputStream(targetFormat, sourceAis);

            // 写入目标文件
            AudioSystem.write(targetAis, AudioFileFormat.Type.WAVE, targetFile);
            targetAis.close();
        }
    }

    /**
     * 计算音频帧大小（字节/帧）= 位深 * 声道数 / 8
     * @param sampleSize 位深（8/16）
     * @param channels 声道数
     * @return 帧大小
     */
    private static int calculateFrameSize(int sampleSize, int channels) {
        return (sampleSize * channels) / 8;
    }

    // ==================== 资源释放工具 ====================

    /**
     * 安全关闭音频输入流（避免空指针和异常）
     * @param audioInputStream 音频输入流
     */
    public static void closeAudioStream(AudioInputStream audioInputStream) {
        if (audioInputStream != null) {
            try {
                audioInputStream.close();
            } catch (IOException e) {
                System.err.println("关闭音频流失败：" + e.getMessage());
            }
        }
    }

    /**
     * 安全关闭音频输出行（播放用）
     * @param line 音频输出行
     */
    public static void closeSourceDataLine(SourceDataLine line) {
        if (line != null) {
            line.drain();
            line.stop();
            line.close();
        }
    }

    // ==================== 示例：工具类使用 ====================
    public static void main(String[] args) {
        try {
            // 1. 读取并解析音频
            File sourceWav = new File("test.wav");
            AudioInputStream ais = NativeAudioUtils.readWavFile(sourceWav);
            NativeAudioUtils.printAudioFormat(ais.getFormat());

            // 2. 播放音频
            // NativeAudioUtils.playWavFile(sourceWav);

            // 3. 截取音频（前5秒）
            File cutWav = new File("cut_test.wav");
            NativeAudioUtils.cutWavFile(sourceWav, cutWav, 0, 5);
            System.out.println("音频截取完成：" + cutWav.getAbsolutePath());

            // 4. 转换格式（立体声→单声道，44100Hz→22050Hz，16位→8位）
            File convertWav = new File("convert_test.wav");
            NativeAudioUtils.convertWavFormat(sourceWav, convertWav, 22050.0f, 1, 8);
            System.out.println("音频格式转换完成：" + convertWav.getAbsolutePath());

            // 5. 释放资源
            NativeAudioUtils.closeAudioStream(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
