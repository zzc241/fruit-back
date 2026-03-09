package com.jhc.fruit.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.dao.CommentDao;
import com.jhc.fruit.entity.Comment;
import com.jhc.fruit.entity.vo.CommentVo;
import com.jhc.fruit.entity.vo.FruitTreeVo;
import com.jhc.fruit.service.CommentService;
import com.jhc.fruit.service.EmployeeService;
import com.jhc.fruit.service.FruitTreeService;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {

    @Autowired
    private FruitTreeService fruitTreeService;
    @Autowired
    private EmployeeService employeeService;
    public CommentVo selectCommentByTreeId(Long treeId){

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getTreeId, treeId);
        Comment comment = this.getOne(queryWrapper);
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment, commentVo);
        Integer choice = comment.getChoice();
        Integer goodRate = choice * 100 / 30;
        commentVo.setGoodRate(goodRate);
        //通过评论中的果树id查询果树种类
        FruitTreeVo fruitTreeVo = fruitTreeService.fruitToVo(fruitTreeService.getById(comment.getTreeId()));
        commentVo.setTreeType(fruitTreeVo.getType());
        //通过userId查询用户姓名、头像
        commentVo.setCreateUserName(employeeService.getEmpNameById(comment.getUserId()));
        commentVo.setCreateUserImg(employeeService.getEmpImgById(comment.getUserId()));
        return commentVo;
    }
}
