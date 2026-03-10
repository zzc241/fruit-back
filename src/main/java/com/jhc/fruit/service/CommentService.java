package com.jhc.fruit.service;


import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.entity.Comment;
import com.jhc.fruit.entity.vo.CommentVo;

public interface CommentService extends IService<Comment> {

    public List<CommentVo> selectCommentsByTreeId(Long treeId);
}
