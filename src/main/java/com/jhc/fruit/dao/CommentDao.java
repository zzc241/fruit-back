package com.jhc.fruit.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jhc.fruit.entity.Comment;
import com.jhc.fruit.entity.CommentImg;

@Mapper
public interface CommentDao extends BaseMapper<Comment> {

    public void saveImg(String url, Long commentId);

    public List<CommentImg> selectImgByCommonId(Long commentId);


    
}
