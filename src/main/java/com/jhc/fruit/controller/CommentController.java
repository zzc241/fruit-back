package com.jhc.fruit.controller;

import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.Comment;
import com.jhc.fruit.service.CommentService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 评论接口控制器
 */
@RestController
@RequestMapping("/comment")
@Api(tags = "消费者-评论区接口")
public class CommentController {

    @Resource
    private CommentService commentService;

    /**
     * 前端调用该接口获取评论列表
     * @param userId 用户ID（可选）
     * @param treeId 树ID（可选）
     * @param orchardId 果园ID（可选）
     * @param pageNum 页码（可选，默认1）
     * @param pageSize 每页条数（可选，默认10）
     * @return 评论数据
     */
    @GetMapping("/list")
    public R<List<Comment>> getCommentList(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long treeId,
            @RequestParam(required = false) Long orchardId,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize
    ) {
        return commentService.getCommentList(userId, treeId, orchardId, pageNum, pageSize);
    }
}
