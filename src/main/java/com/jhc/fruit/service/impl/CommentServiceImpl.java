package com.jhc.fruit.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.dao.CommentDao;
import com.jhc.fruit.entity.Comment;
import com.jhc.fruit.entity.FruitTree;
import com.jhc.fruit.entity.vo.CommentVo;
import com.jhc.fruit.entity.vo.FruitTreeVo;
import com.jhc.fruit.service.CommentService;
import com.jhc.fruit.service.EmployeeService;
import com.jhc.fruit.service.FruitTreeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {

    @Autowired
    private FruitTreeService fruitTreeService;
    @Autowired
    private EmployeeService employeeService;
    public List<CommentVo> selectCommentsByTreeId(Long treeId){
    LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Comment::getTreeId, treeId);
    List<Comment> comments = this.list(queryWrapper);
    log.info("查询到 {} 条原始评论", comments.size());
    if(comments.size() == 0){
        log.warn("没有找到任何评论, treeId={}", treeId);
        return new ArrayList<>(); // 返回空列表而不是 null
    }
    
    List<CommentVo> commentVos = new ArrayList<>();
    for (int i = 0; i < comments.size(); i++) {
        Comment comment = comments.get(i);
        log.info("处理第 {} 条评论, id={}", i+1, comment.getId());
        
        try {
            CommentVo commentVo = new CommentVo();
            BeanUtils.copyProperties(comment, commentVo);
            
            // 计算好评率
            Integer choice = comment.getChoice();
            Integer goodRate = choice * 100 / 30;
            commentVo.setGoodRate(goodRate);
            log.info("好评率计算完成: {}", goodRate);
            
            // 查询果树
            FruitTree fruitTree = fruitTreeService.getById(comment.getTreeId());
            log.info("果树查询结果: {}", fruitTree);
            
            FruitTreeVo fruitTreeVo = fruitTreeService.fruitToVo(fruitTree);
            log.info("果树Vo转换结果: {}", fruitTreeVo);
            
            if (fruitTreeVo != null) {
                commentVo.setTreeType(fruitTreeVo.getType());
                log.info("果树类型: {}", fruitTreeVo.getType());
            } else {
                log.warn("fruitTreeVo 为 null, treeId={}", comment.getTreeId());
            }
            
            // 查询用户
            String userName = employeeService.getEmpNameById(comment.getUserId());
            String userImg = employeeService.getEmpImgById(comment.getUserId());
            log.info("用户信息: name={}, img={}", userName, userImg);
            
            commentVo.setCreateUserName(userName);
            commentVo.setCreateUserImg(userImg);
            
            commentVos.add(commentVo);
            log.info("成功添加第 {} 条评论Vo", i+1);
            
        } catch (Exception e) {
            log.error("处理评论时出错, commentId={}", comment.getId(), e);
            // 继续处理下一条，不要中断循环
        }
    }
    
    log.info("最终返回 {} 条评论Vo", commentVos.size());
    return commentVos;
}
}
