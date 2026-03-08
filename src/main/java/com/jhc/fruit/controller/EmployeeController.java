package com.jhc.fruit.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jhc.fruit.aop.Log;
import com.jhc.fruit.common.*;
import com.jhc.fruit.entity.Employee;
import com.jhc.fruit.entity.vo.EmployeeVo;
import com.jhc.fruit.entity.vo.loginEmployeeVo;
import com.jhc.fruit.service.EmployeeService;
import com.jhc.fruit.service.impl.EmployeeServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author ：lcy
 * @description ：员工相关接口
 * @date ：2022/4/28 0028 14:04
 */
@Slf4j
@RestController
@RequestMapping("/employee")
@Api(tags = "用户管理接口")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/getWorkers")
    @ApiOperation("获取所有临时工")
    //获取所有临时工的方法
    public R<List<Employee>> getWorkers(){
        List<Employee> workers = employeeService.getWorkers();
        return R.success(workers);
    }

    /**
     * 用户通过openId登录
     * @param code
     * @return
     *      */
    @GetMapping("/OpenIdLogin")
    @ApiOperation("小程序微信登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code",value = "用户登录时获得的临时code"),
            @ApiImplicitParam(name = "appId",value = "每个微信小程序都有的appId"),
            @ApiImplicitParam(name = "appSecret",value = "前端传入的appSecret")
    })
    public R<Employee> openIdLogin(String code,HttpServletRequest request){
        return employeeService.openIdLogin(code,request);
    }

    /**
     * 用户通过openId登录
     */
//    @GetMapping("/unBind")
    @ApiOperation("用户登录时获取openId方法")
    @ApiImplicitParams({
    })
    public R<String> unBind(){
        Long id = BaseContext.getId();
        Employee employee = new Employee();
        employee.setId(id);
        employee.setOpenId("");
        employee.setStatus(1);
        boolean update = employeeService.updateById(employee);
        System.out.println(update);
        System.out.println("employee-OpenId:" + employeeService.getById(id).getOpenId());
        return R.success("解绑成功");
    }



    /**
     * 员工登录
     * 账户、密码、code
     * @param request
     * @return
     */
    //将登录的用户存入session中，通过HttpServletRequest中来操作session
    @PostMapping("/login")
    @ApiOperation(value="用户密码登录方法")
    public R<Employee> login(@RequestBody loginEmployeeVo loginEmployeeVo , HttpServletRequest request){
        return employeeService.login(loginEmployeeVo,request);
    }

    /**
     * 员工退出
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("用户登出方法")
    public R<String> logout(HttpServletRequest request){
        Long id = (Long)request.getSession().getAttribute("employee");
        //用户退出，账号状态恢复可用
        Employee emp = employeeService.getById(id);
        if(emp == null){
            return R.error("登出失败");
        }
        emp.setStatus(1);
        employeeService.updateById(emp);

        //清理当前session中保存的当前登录员工id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("新增用户方法")
    public R<String> save(@RequestBody Employee employee){
        log.info("新增员工，员工信息：",employee.toString());
        //用户名校验
        String username = employee.getUsername();
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Employee::getUsername,username);
        Employee emp = employeeService.getOne(lambdaQueryWrapper);
        if(emp != null){
            return R.error("用户名已经存在");
        }
        //设置初始密码123456，使用md5加密处理
        if(employee.getPassword() == null || "".equals(employee.getPassword())){
            employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        }else {
            employee.setPassword(DigestUtils.md5DigestAsHex(employee.getPassword().getBytes()));
        }
        if(employee.getUsername() == null || "".equals(employee.getUsername())){
            employee.setUsername("newUser" + UUID.randomUUID().toString().replace("-", ""));
        }
        if (employee.getIsAdmin() == null || "".equals(employee.getIsAdmin())){
            //如果没有设置过用户类型，默认用户为游客5
            employee.setIsAdmin(5);
        }
        //设置创建时间（新增时创建时间和修改时间同步）
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        //设置创建人（同上）
//        employee.setCreateUser((Long)request.getSession().getAttribute("employee"));
//        employee.setUpdateUser((Long)request.getSession().getAttribute("employee"));

        employeeService.save(employee);

        return R.success("新增员工成功");
    }

    @GetMapping("/page")
    @ApiOperation("后：查询所有用户以分页展示方法")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "当前页码数"),
            @ApiImplicitParam(name = "pageSize",value = "最大页码数"),
            @ApiImplicitParam(name = "name", value= "搜索时输入的用户名称(可选)"),
            @ApiImplicitParam(name = "orchardId", value = "所属果园")
    })
    public R<Page> page(int page,int pageSize,String name, Long orchardId){
        log.info("page = {},pageSize = {},name = {}, orchardId = {}",page,pageSize,name, orchardId);

        if(orchardId == null){
            orchardId = BaseContext.getId();
        }
        //创建分页构造器
        Page pageInfo = new Page(page,pageSize);
        //创建条件构造器
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        lambdaQueryWrapper.eq(Employee::getOrchardId,orchardId);
        //进行分页查询
        pageInfo = employeeService.page(pageInfo,lambdaQueryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id修改用户信息
     * @param employee
     * @return
     */
    @PutMapping("/update")
    @ApiOperation("后：修改用户信息")
    public R<String> update(@RequestBody Employee employee){
        //Long empId = (Long)request.getSession().getAttribute("emplopyee");
        //其中前端页面会对数字进行四舍五入，丢失了精度，导致id不一样
        //解决：将long型数据统一转为String字符串
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
        System.out.println(BaseContext.getId());
        employeeService.updateById(employee);

        return R.success("员工信息修改成功");
    }

    @PutMapping("/reset")
    @ApiOperation("修改密码")
    public R<String> updatePassword(@RequestParam("oldPassword") String oldPassword,@RequestParam("newPassword") String newPassword){
        Long id = BaseContext.getId();
        
        // 查询用户
        Employee emp = employeeService.getById(id);
        if (emp == null) {
            return R.error("用户不存在");
        }
        
        // 加密处理
        String oldPasswordMd5 = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        String newPasswordMd5 = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        
        // 验证旧密码
        if (!emp.getPassword().equals(oldPasswordMd5)) {
            return R.error("原密码不正确");
        }
        
        // 验证新旧密码是否相同
        if (emp.getPassword().equals(newPasswordMd5)) {
            return R.error("新密码不能与原密码相同");
        }
        
        // 更新密码
        Employee updateEmp = new Employee();
        updateEmp.setId(id);
        updateEmp.setPassword(newPasswordMd5);
        
        // updateById 会根据id更新
        employeeService.updateById(updateEmp);
        
        return R.success("成功修改密码");
    }

    /**
     * 根据员工id查询员工信息;用于在修改员工信息时的回溯信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询员工信息，用于修改员工时回溯信息")
    @ApiImplicitParam(name = "id", value = "要修改员工对应的id")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息");
        Employee employee = employeeService.getById(id);
        if(employee != null){
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息");
    }

    /**
     * 根据员工id删除员工信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation("根据id删除员工信息")
    @ApiImplicitParam(name = "id", value = "要删除员工对应的id")
    public R<String> delete(@PathVariable Long id){
        Long currentUserId = BaseContext.getId();
        Employee currentUser = employeeService.getById(currentUserId);
        if (currentUser.getIsAdmin() != 1) {
            return R.error("只有超级管理员才能删除员工");
        }
        log.info("根据id删除员工信息");
        Employee emp = employeeService.getById(id);
        if(emp == null){
            return R.error("找不到用户，刷新后再试");
        }
        //删除数据库中的对应员工数据
        boolean isRemove = employeeService.removeById(id);
        if(isRemove){
            return R.success("删除成功");
        }
        return R.error("删除失败");
    }


    @GetMapping("/getAdmin")
    @ApiOperation("查询可以园区负责人候选列表[能成为园区负责人的人]")
    public R<List<Employee>> getAdmin(){
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //判断员工的身份为0，则成为园区负责人候选人
        lambdaQueryWrapper.eq(Employee::getIsAdmin,0);
        List<Employee> admins = employeeService.list(lambdaQueryWrapper);
        return R.success(admins);
    }

    /**
     * @param employee
     * 实名认证，更新idCard身份证号和name实际姓名字段
     */
    @PostMapping("/auth")
    @ApiOperation("实名认证")
    public R<Employee> authentication(@RequestBody Employee employee){
        //实名认证的前提是已经有账户了
        Long id = employee.getId();
        if(id == null){
            //说明账户不存在
            return R.error("账户不存在");
        }
        //账户存在
        employeeService.updateById(employee);
        return R.success();
    }

    @ApiOperation("用户列表")
    @GetMapping("/getAll")
    public R<List<Employee>> getAll(@RequestParam Long orchardId){
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Employee::getOrchardId,orchardId);
        List<Employee> employees = employeeService.list(lambdaQueryWrapper);
        return R.success(employees);
    }

    @ApiOperation("批量删除员工")
    @DeleteMapping("deleteList")
    public R<String> deleteList(@RequestBody List<Long> ids){
        return employeeService.deleteList(ids);
    }

    @ApiOperation("根据果园id查询员工姓名")
    @GetMapping("/getEmpNameByOrchardIds")
    public R<List<EmployeeVo>> getEmpNameByOrchardId(@RequestParam Long orchardId){
        List<EmployeeVo> employees = employeeService.getEmpByOrchardId(orchardId);
        return R.success(employees);
    }

    @ApiOperation("保存用户头像")
    @PostMapping("/saveHeadImg")
    public R<String> saveHeadImg(@RequestParam("file") MultipartFile file) {
        String fileUrl = employeeService.saveHeadImg(file, "headImg");
        return R.success(fileUrl);
    }

    @ApiOperation("修改账户状态")
    @PutMapping("/status")
    public R<String> updateStatus(@RequestBody Employee employee){
        return employeeService.updateStatus(employee);
    }
}
