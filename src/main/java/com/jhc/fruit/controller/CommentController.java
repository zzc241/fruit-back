package com.jhc.fruit.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jhc.fruit.common.R;
import com.jhc.fruit.dao.FruitTreeDao;
import com.jhc.fruit.entity.Comment;
import com.jhc.fruit.entity.vo.CommentVo;
import com.jhc.fruit.service.CommentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/comment")
@RestController
@Api(tags = "评论管理接口")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private FruitTreeDao fruitTreeDao;
    //实现评论添加功能
    @ApiOperation(value = "添加评论接口")
    @PostMapping("/add")
    public R<Long> add(@RequestBody Comment comment) {
        comment.setOrchardId(fruitTreeDao.selectOrchardIdByTreeId(comment.getTreeId()));
        commentService.save(comment);
        Long id = comment.getId();
        return R.success(id);
    }
    @ApiOperation(value = "添加评论图片接口")
    @PostMapping("/addImg/{commentId}")
    public R<String> addImg(@PathVariable Long commentId,
            @RequestParam MultipartFile img) {
        return commentService.addImg(commentId, img);
    }
    //实现查询功能
    @ApiOperation(value = "查询评论接口")
    @GetMapping("/query/{treeId}")
    public R<List<CommentVo>> selectCommentByTreeId(@PathVariable Long treeId) {
        List<CommentVo> comments = commentService.selectCommentsByTreeId(treeId);
        if(comments.size() == 0){
            return R.error("没有找到任何评论");
        }
        return R.success(comments);
    }
    //实现删除功能
    @ApiOperation(value = "删除评论接口")
    @DeleteMapping("/delete/{id}")
    //利用前端给id实现删除评论
    public R<String> deleteById(@PathVariable Long id) {
        commentService.removeById(id);
        return R.success("删除成功");
    }
    //实现修改功能
    // @ApiOperation(value = "修改评论接口")
    @PostMapping("/update")
    public R<String> update(@RequestBody Comment comment) {
        boolean result = commentService.updateById(comment);
        if (result) {
            return R.success("修改成功");
        }
        return R.error("修改失败");
    }
}
