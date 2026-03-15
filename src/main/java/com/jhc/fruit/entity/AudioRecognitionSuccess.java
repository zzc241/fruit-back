package com.jhc.fruit.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // 忽略未知字段（防止扩展字段报错）
public class AudioRecognitionSuccess {
    private boolean success; // Flask返回的success字段
    private String recognized_text; // 语音识别结果
    private String ai_response; // AI回复内容
}
