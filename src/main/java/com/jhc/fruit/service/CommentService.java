package com.jhc.fruit.service;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.Comment;
import com.jhc.fruit.entity.vo.CommentVo;

public interface CommentService extends IService<Comment> {

    public List<CommentVo> selectCommentsByTreeId(Long treeId);

    public R<String> addImg(Long commentId , MultipartFile img);
}
