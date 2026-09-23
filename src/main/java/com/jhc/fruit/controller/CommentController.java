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
@Api(tags = "??????")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private FruitTreeDao fruitTreeDao;
    //????????
    @ApiOperation(value = "??????")
    @PostMapping("/add")
    public R<Long> add(@RequestBody Comment comment) {
        comment.setOrchardId(fruitTreeDao.selectOrchardIdByTreeId(comment.getTreeId()));
        commentService.save(comment);
        Long id = comment.getId();
        return R.success(id);
    }
    @ApiOperation(value = "????????")
    @PostMapping("/addImg/{commentId}")
    public R<String> addImg(@PathVariable Long commentId,
                            @RequestParam MultipartFile img) {
        return commentService.addImg(commentId, img);
    }
    //??????
    @ApiOperation(value = "??????")
    @GetMapping("/query/{treeId}")
    public R<List<CommentVo>> selectCommentByTreeId(@PathVariable Long treeId) {
        List<CommentVo> comments = commentService.selectCommentsByTreeId(treeId);
        if(comments.size() == 0){
            return R.error("????????");
        }
        return R.success(comments);
    }
    //??????
    @ApiOperation(value = "??????")
    @DeleteMapping("/delete/{id}")
    //?????id??????
    public R<String> deleteById(@PathVariable Long id) {
        commentService.removeById(id);
        return R.success("????");
    }
    //??????
    // @ApiOperation(value = "??????")
    @PostMapping("/update")
    public R<String> update(@RequestBody Comment comment) {
        boolean result = commentService.updateById(comment);
        if (result) {
            return R.success("????");
        }
        return R.error("????");
    }
}

