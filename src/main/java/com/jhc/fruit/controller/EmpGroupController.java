package com.jhc.fruit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jhc.fruit.common.BaseContext;
import com.jhc.fruit.common.R;
import com.jhc.fruit.entity.EmpGroup;
import com.jhc.fruit.entity.Employee;
import com.jhc.fruit.entity.IdentifyInfo;
import com.jhc.fruit.entity.PickInfo;
import com.jhc.fruit.service.impl.EmpGroupServiceImpl;
import com.jhc.fruit.service.impl.EmployeeServiceImpl;
import com.jhc.fruit.service.impl.PickInfoServiceImpl;
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
    private EmployeeServiceImpl employeeService;

    @Autowired
    private EmpGroupServiceImpl empGroupServiceImpl;

    @Autowired
    private PickInfoServiceImpl pickInfoServiceImpl;

    @GetMapping("/getMyGroups")
    @ApiOperation("查询根据当前登陆用户的所在组")
    public R<List<List<EmpGroup>>> getMyGroups(){
        Long empId = BaseContext.getId();
        List<EmpGroup> groupsByEmp = empGroupServiceImpl.getGroupsByEmp(empId);
        System.out.println(groupsByEmp);
        List<List<EmpGroup>> groups = new ArrayList<>();
        for(EmpGroup empGroup:groupsByEmp){
            List<EmpGroup> groupsByContentEmp = empGroupServiceImpl.getGroupsByContentEmp(empGroup);
            System.out.println("列表的单一对象："+ groupsByContentEmp);
            //获取出存入返回结果中的数据
            groups.add(groupsByContentEmp);
        }
        System.out.println("groups:" + groups);
        return R.success(groups);
    }

    @GetMapping("/getGroups/{groupId}")
    @ApiOperation("主任务下各个分任务对应的组")
    @ApiImplicitParam(name = "groupId", value = "组id[也就是任务id]")
    public R<List<List<EmpGroup>>> getGroups(@PathVariable Long groupId){
        List<Map<String, String>> groupByContent = empGroupServiceImpl.getGroupByContent(groupId);
        if(groupByContent == null){
            return R.error("当前任务不存在");
        }
        //创建对应属性,用于接收查询出来的值
        Long gId = null;
        String content = null;
        String empId = null;
        EmpGroup empGroup = null;
        String infoId = null;
        List<List<EmpGroup>> sumEmpGroups = new ArrayList<>();
        //遍历查出来有几条数据，内容不同
        for(int i = 0;i < groupByContent.size();i++){
            List<EmpGroup> empGroups = new ArrayList<>();
            //获取当前循环的Map对象
            Map<String, String> map = groupByContent.get(i);
            System.out.println(map.toString());
            //对属性变量进行初始化
            gId = Long.parseLong(String.valueOf(map.get("groupId")));
            content = map.get("content");
            empId = map.get("empId");
            infoId = map.get("infoId");
            String[] splitEmp = empId.split(",");
            String[] splitInfo = infoId.split(",");
            //将切割好的临时工及其任务通知id进行遍历
            for(int j = 0; j < splitEmp.length; j++){
                //在循环内创建引用型实例，不然会覆盖
                empGroup = new EmpGroup();
                empGroup.setGroupId(gId);
                empGroup.setContent(content);
                empGroup.setInfoId(Long.parseLong(splitInfo[j]));
                // 替代pickInfoServiceImpl.getIsFinish()的逻辑
                Long currentInfoId = Long.parseLong(splitInfo[j]);
                PickInfo pickInfo = pickInfoServiceImpl.getById(currentInfoId);
                Boolean isFinish = pickInfo != null ? pickInfo.getIsFinish() : false;
                empGroup.setIsFinish(isFinish);
                // empGroup.setIsFinish(pickInfoServiceImpl.getIsFinish(Long.parseLong(splitInfo[j])));
                empGroup.setEmpId(Long.parseLong(splitEmp[j]));
                empGroup.setEmployee(employeeService.getById(Long.parseLong(splitEmp[j])));
                //将临时工添加到列表中
                empGroups.add(empGroup);

                if(j == splitEmp.length -1){
                    //临时工列表
                    sumEmpGroups.add(empGroups);
                }
            }
        }
        return R.success(sumEmpGroups);
    }


    @PostMapping("/addGroup")
    @ApiOperation("发布任务，新建一个组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "empIds",value = "要添加的员工id列表"),
            @ApiImplicitParam(name = "pickInfo",value = "采摘任务信息")
    })
    public R<String> addGroup(@RequestBody Map<String,List<String>> variable){
        //获取Map中的参数
        List<String> empIds = variable.get("empIds");
        List<String> content = variable.get("content");
        List<String> infoId = variable.get("infoId");

        //将传入的字符串列表转为Long型
        List<Long> lEmpIds = new ArrayList<>();
        for(int i = 0; i < empIds.size(); i ++){
            lEmpIds.add(Long.parseLong(empIds.get(i)));
        }
        //向选中的临时工发送通知
        PickInfo pickInfo;
        for (Long empId:lEmpIds) {
            pickInfo = new PickInfo();
            //将通知转发至临时工
            pickInfo.setContent(content.get(0));
            pickInfo.setEmpId(empId);
            boolean save = pickInfoServiceImpl.save(pickInfo);
            if(!save){
                return R.error("通知转发失败");
            }
            //新建一个组对象
            EmpGroup empGroup = new EmpGroup();
            empGroup.setEmpId(empId);
            empGroup.setContent(content.get(0));
            empGroup.setInfoId(pickInfo.getId());
            //使用果农的主任务id作为组id
            empGroup.setGroupId(Long.parseLong(infoId.get(0)));
            boolean isSave = empGroupServiceImpl.save(empGroup);
            if(!isSave){
                return R.success("新建组失败");
            }
        }

        return R.success("成功新建组");
    }


    @GetMapping("/showWorker/{groupId}")
    @ApiOperation("用来展示各个组之间的临时工")
    @ApiImplicitParam(name = "groupId",value = "合作组的Id")
    public R<List<Long>> showWorker(Long groupId){
        List<Long> list = empGroupServiceImpl.getEmpId(groupId);
        if(list == null){
            return R.error("查找失败，没有创建对应组");
        }
        return R.success(list);
    }

    /**
     * 已完成任务通知方法
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    @ApiOperation("已完成任务通知方法，完成后自动删除对应的组")
    @ApiImplicitParam(name = "id",value = "通知对应的id")
    public R<String> finishInfo(@PathVariable Long id){
        //查询到对应通知信息
        PickInfo info = pickInfoServiceImpl.getById(id);
        Employee employee = employeeService.getById(info.getEmpId());
        //设置属性为完成
        info.setIsFinish(true);
        //更新信息
        pickInfoServiceImpl.updateById(info);

        //判断是否为果农，是果农则会删除主任务通知对应组
        if(employee.getIsAdmin() == 0){
            //删除组
            Boolean isDelete = empGroupServiceImpl.deleteGroup(id);
            if(isDelete){
                return R.error("删除失败：找不到对应组");
            }
            return R.success("已成功删除对应组，组别信息已更新");
        }
        return R.success("组别信息已更新");
    }
}
