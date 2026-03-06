package com.jhc.fruit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jhc.fruit.common.BaseContext;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.EmpGroup;
import com.jhc.fruit.entity.Employee;
import com.jhc.fruit.entity.IdentifyInfo;
import com.jhc.fruit.entity.AnnounceInfo;
import com.jhc.fruit.service.AnnounceInfoService;
import com.jhc.fruit.service.EmpGroupService;
import com.jhc.fruit.service.EmployeeService;
import com.jhc.fruit.service.impl.EmpGroupServiceImpl;
import com.jhc.fruit.service.impl.EmployeeServiceImpl;
import com.jhc.fruit.service.impl.AnnounceInfoServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ：lcy
 * @description：后台组间关系表
 * @date ：2022/6/13 0013 9:23
 */
@RequestMapping("/group")
@RestController
@Api(tags = "后台：组-人关系管理接口")
public class EmpGroupController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmpGroupService empGroupService;

    @Autowired
    private AnnounceInfoService pickInfoService;

    @GetMapping
    @ApiOperation("查询本果园所有员工组")
    public R<List<EmpGroup>> findAll(@RequestParam Long orchardId) {
        LambdaQueryWrapper<EmpGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EmpGroup::getOrchardId, orchardId);
        List<EmpGroup> empGroups = empGroupService.list(queryWrapper);
        return R.success(empGroups);
    }

    @PostMapping("/add")
    @ApiOperation("新增员工组,返回组id")
    private R<Long> add(@RequestBody EmpGroup empGroup) {
        empGroupService.save(empGroup);
        return R.success(empGroup.getId());
    }

    @PostMapping("/addEmployee")
    @ApiOperation("添加员工到员工组")
    public R<Boolean> addEmployee(@RequestBody List<Long> empIds, @RequestParam Long groupId) {
        Boolean flag = empGroupService.addEmployee(empIds, groupId);
        return R.success(flag);
    }
}
