package com.jhc.fruit.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.common.R;
import com.jhc.fruit.dao.CommentDao;
import com.jhc.fruit.entity.Comment;
import com.jhc.fruit.entity.CommentImg;
import com.jhc.fruit.entity.FruitTree;
import com.jhc.fruit.entity.vo.CommentVo;
import com.jhc.fruit.entity.vo.FruitTreeVo;
import com.jhc.fruit.service.CommentService;
import com.jhc.fruit.service.EmployeeService;
import com.jhc.fruit.service.FileService;
import com.jhc.fruit.service.FruitTreeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {

    @Autowired
    private FruitTreeService fruitTreeService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private FileService fileService;
    @Autowired
    private CommentDao commentDao;

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
                Long commonId = comment.getId();
                List<CommentImg> commentImg = commentDao.selectImgByCommonId(commonId);

                List<String> multipleImgs = new ArrayList<>();
                for(CommentImg img : commentImg){
                    log.info("处理图片: {}", img.getImg() );
                    String imgUrl = fileService.getImageUrl(img.getImg());
                    log.info("图片URL: {}", imgUrl);
                    multipleImgs.add(imgUrl);
                }
                CommentVo commentVo = new CommentVo();
                BeanUtils.copyProperties(comment, commentVo);
                commentVo.setMultipleImgs(multipleImgs);

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
                commentVo.setCreateUserImg(fileService.getImageUrl(userImg));

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
    @Override
    public R<String> addImg(Long commentId , MultipartFile img){
        String url = null;
        try {
            url = fileService.save(img, "comment");
        } catch (Exception e) {
            log.error("新增报告及图片失败，报告ID：{}，异常信息：{}", commentId, e.getMessage(), e);
            return R.error("上传失败：" + e.getMessage());
        }
        // 将图片和commentId放到数据库
        commentDao.saveImg(url, commentId);
        return R.success(url);
    }
}
