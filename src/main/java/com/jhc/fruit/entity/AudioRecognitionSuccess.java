package com.jhc.fruit.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // ????????????????
public class AudioRecognitionSuccess {
    private boolean success; // Flask???success??
    private String recognized_text; // ??????
    private String ai_response; // AI????
}
