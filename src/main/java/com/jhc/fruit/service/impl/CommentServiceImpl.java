package com.jhc.fruit.service.impl;

import com.jhc.fruit.common.R;
import com.jhc.fruit.dao.CommentDao;
import com.jhc.fruit.entity.Comment;
import com.jhc.fruit.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 评论业务逻辑实现类
 */
@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentDao commentDao;

    @Override
    public R<List<Comment>> getCommentList(Long userId, Long treeId, Long orchardId, Integer pageNum, Integer pageSize) {
        // 记录查询参数
        log.info("查询评论列表，参数：userId={}, treeId={}, orchardId={}, pageNum={}, pageSize={}",
                userId, treeId, orchardId, pageNum, pageSize);

        // 分页参数校验，设置默认值
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        // 计算分页偏移量（适配MySQL LIMIT 偏移量, 条数）
        Integer offset = (pageNum - 1) * pageSize;

        try {
            // 查询评论列表
            List<Comment> commentList = commentDao.selectComments(userId, treeId, orchardId, offset, pageSize);
            // 查询总条数（供前端分页使用）
            Integer total = commentDao.selectCommentCount(userId, treeId, orchardId);

            // 记录查询结果
            log.info("查询评论列表，结果：commentList.size={}, total={}", commentList.size(), total);

            // 封装返回结果，添加总条数
            R<List<Comment>> result = R.success(commentList);
            result.add("total", total);
            return result;
        } catch (Exception e) {
            log.error("查询评论列表失败：", e);
            return R.error("查询评论列表失败：" + e.getMessage());
        }
    }
}