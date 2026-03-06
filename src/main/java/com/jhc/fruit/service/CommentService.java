package com.jhc.fruit.service;

import com.jhc.fruit.entity.Comment;
import com.jhc.fruit.common.R;
import java.util.List;

/**
 * 评论业务逻辑接口
 */
public interface CommentService {

    /**
     * 多条件分页查询评论列表
     * @param userId 用户ID（可选）
     * @param treeId 树ID（可选）
     * @param orchardId 果园ID（可选）
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页条数
     * @return 评论列表
     */
    R<List<Comment>> getCommentList(Long userId, Long treeId, Long orchardId, Integer pageNum, Integer pageSize);
}