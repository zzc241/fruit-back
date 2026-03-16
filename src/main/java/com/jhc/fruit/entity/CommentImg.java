package com.jhc.fruit.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("comment_img")
public class CommentImg {
    private Long id;
    private Long commentId;
    private String img;
    
}
