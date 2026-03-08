package com.jhc.fruit.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.entity.Comment;
import com.jhc.fruit.entity.vo.CommentVo;

public interface CommentService extends IService<Comment> {

    public CommentVo selectCommentByTreeId(Long treeId);
}
