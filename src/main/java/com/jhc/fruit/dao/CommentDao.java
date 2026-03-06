package com.jhc.fruit.dao;


import com.jhc.fruit.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论数据访问接口（对应comment表）
 */
@Mapper
public interface CommentDao {

    /**
     * 条件查询评论列表
     * @param userId 用户ID（可选）
     * @param treeId 树ID（可选）
     * @param orchardId 果园ID（可选）
     * @param offset 分页偏移量（LIMIT起始值）
     * @param pageSize 每页条数
     * @return 评论列表
     */
    List<Comment> selectComments(
            @Param("userId") Long userId,
            @Param("treeId") Long treeId,
            @Param("orchardId") Long orchardId,
            @Param("offset") Integer offset,
            @Param("pageSize") Integer pageSize
    );

    /**
     * 查询评论总数（用于分页）
     * @param userId 用户ID（可选）
     * @param treeId 树ID（可选）
     * @param orchardId 果园ID（可选）
     * @return 总条数
     */
    Integer selectCommentCount(
            @Param("userId") Long userId,
            @Param("treeId") Long treeId,
            @Param("orchardId") Long orchardId
    );
}