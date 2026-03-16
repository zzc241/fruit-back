package com.jhc.fruit.entity;

import lombok.Data;

@Data
public class BlockPosition {
    private String file_name;
    private Double latitude;
    private Double longitude;
    private Double altitude;
    private String full_block_code;
    private String big_block;
    private String sub_block;
}
