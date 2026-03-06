package com.jhc.fruit.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 评论实体类（对应数据库comment表）
 */
@Data
public class Comment {
    // 评论ID（主键）
    private Long id;
    // 用户ID（对应数据库user_id）
    private Long userId;
    // 树ID（对应数据库tree_id）
    private Long treeId;
    // 果园ID（对应数据库orchard_id）
    private Long orchardId;
    // 评论内容（对应数据库content）
    private String content;
    // 选择/评分类字段（对应数据库choice）
    private String choice;
    // 补充：若表中有创建时间字段可添加，无则删除
    // private LocalDateTime createTime;
}
