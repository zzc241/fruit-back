package com.jhc.fruit.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jhc.fruit.aop.Log;
import com.jhc.fruit.common.*;
import com.jhc.fruit.entity.Employee;
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
 * @description：员工相关接口
 * @date ：2022/4/28 0028 14:04
 */
@Slf4j
@RestController
@RequestMapping("/employee")
@Api(tags = "后台：用户管理接口")
public class EmployeeController {
    private String openId;
    @Autowired
    private EmployeeServiceImpl employeeService;

    @GetMapping("/getWorkers")
    @ApiOperation("获取所有临时工")
    //获取所有临时工的方法
    public R<List<Employee>> getWorker(){
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Employee::getIsAdmin,2);
        List<Employee> workers = employeeService.list(wrapper);
        if(workers != null){
            return R.success(workers);
        }
        return null;
    }

    /**
     * 用户通过openId登录
     * @param code
     * @return
     */
    @GetMapping("/getOpenId")
    @ApiOperation("用户登录时获取openId方法")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code",value = "用户登录时获得的临时code"),
            @ApiImplicitParam(name = "appId",value = "每个微信小程序都有的appId"),
            @ApiImplicitParam(name = "appSecret",value = "前端传入的appSecret")
    })
    public R<Employee> getOpenId(String code,HttpServletRequest request){
        //拼接url
        StringBuilder url = new StringBuilder("https://api.weixin.qq.com/sns/jscode2session?");
        url.append("appid=");//appid设置
        url.append("wx8d7387dcf72d379e");
        url.append("&secret=");//secret设置
        url.append("1cb81e119574f97c341f794b6e545c7e");
        url.append("&js_code=");//code设置
        url.append(code);
        url.append("&grant_type=authorization_code");
        Map<String, Object> map = null;
        try {
            HttpClient client = HttpClientBuilder.create().build();//构建一个Client
            HttpGet get = new HttpGet(url.toString());    //构建一个GET请求
            HttpResponse response = client.execute(get);//提交GET请求
            HttpEntity result = response.getEntity();//拿到返回的HttpResponse的"实体"
            String content = EntityUtils.toString(result);
//            System.out.println(content);//打印返回的信息
            JSONObject res = JSONObject.parseObject(content);//把信息封装为json
            //把信息封装到map
            map = JsonUtil.parseJSON2Map(res);//使用工具类将JSON数据转换为Map类型
        } catch (Exception e) {
            e.printStackTrace();
        }

        openId = (String) map.get("openid");
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getOpenId,openId);
        //根据openId查询用户
        Employee emp = employeeService.getOne(wrapper);
        if(emp == null){
            //代表用户第一次登录，还没有存入openId
            return R.error(openId);
        }
        System.out.println("openId查出的用户id" + emp.getId());
        request.getSession().setAttribute("employee",emp.getId());
        System.out.println("openId登录时的request" + request);
        //清空openId
        openId = null;
        return R.success(emp,request);
    }

    /**
     * 用户通过openId登录
     */
    @Log
    @GetMapping("/unBind")
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
     * @param loginMap
     * @return
     */
    //将登录的用户存入session中，通过HttpServletRequest中来操作session
    @Log
    @PostMapping("/login")
    @ApiOperation(value="用户登录方法")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "map" , value = "JSON格式，当中存放用户输入的账号，密码"),
    })
    public R<Employee> login(@RequestBody Map<String,String> loginMap, HttpServletRequest request){
        //获取前端提交的数据
        String userName = loginMap.get("username");
        String password = loginMap.get("password");

        //1.将提交的密码进行md5处理
         password = DigestUtils.md5DigestAsHex(password.getBytes());
        System.out.println(password);

        //2.根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,userName);
        Employee employee = employeeService.getOne(queryWrapper);
        //3.如果没有查询到则返回登录失败结果
        if(employee == null){
            return R.error("用户名不存在");
        }

        //4.密码比对
        if(!employee.getPassword().equals(password)){
            return R.error("密码错误");
        }

        //5.查看员工状态
        if(employee.getStatus() != 1){
            return R.error("员工状态被禁用");
        }

        //登录成功，将用户id存入Session
        request.getSession().setAttribute("employee",employee.getId());
        //存入该员工的openId
        employee.setOpenId(openId);
        //防止重复登录，账号状态禁用
        //TODO:测试阶段，用户状态先注释掉
        // employee.setStatus(0);
        //更新用户信息
        employeeService.updateById(employee);
        return R.success(employee,request);
    }

    /**
     * 员工退出
     * @return
     */
    @Log
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
    @Log
    @PostMapping
    @ApiOperation("后：新增用户方法")
    public R<String> save(@RequestBody Employee employee,HttpServletRequest request){
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
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
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
            @ApiImplicitParam(name = "name", value= "搜索时输入的用户名称(可选)")
    })
    public R<Page> page(int page,int pageSize,String name){
        log.info("page = {},pageSize = {},name = {}",page,pageSize,name);

        //创建分页构造器
        Page pageInfo = new Page(page,pageSize);
        //创建条件构造器
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //进行分页查询
        employeeService.page(pageInfo,lambdaQueryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id修改用户信息
     * @param employee
     * @return
     */
    @PutMapping
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

    @Log
    @PutMapping("/reset")
    @ApiOperation("修改密码")
    public R<String> updatePassword(@RequestParam("oldPassword") String oldPassword,@RequestParam("newPassword") String newPassword){
        //获取当前已经登录用户的id
        Long id = BaseContext.getId();
        System.out.println(id);
        Employee emp = employeeService.getById(id);
        //将用户输入的新旧密码统一进行md5加密处理
        oldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        String password = emp.getPassword();
        newPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        if((!newPassword.equals(password)) && (!password.equals(oldPassword))){
            //生成更改内容对象
            Employee  employee = new Employee();
            employee.setId(id);
            employee.setPassword(newPassword);
            //创建条件构造器
            LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Employee::getId,id).eq(Employee::getPassword,oldPassword);

            //修改密码
            employeeService.updateById(employee);
            return R.success("成功修改密码");
        }
        return R.error("输入的原密码不匹配或新密码与原密码相同");

    }

    @ApiOperation("个人信息展示方法")
    @GetMapping("/getPeople")
    public R<Employee> getPeople(){
        Long id = BaseContext.getId();
        System.out.println("个人信息展示获取的id：" + BaseContext.getId());
        //1.获取员工id
        Long employeeId = BaseContext.getId();
        //2.根据员工id查询员工个人信息
        Employee employee = employeeService.getById(employeeId);
        return R.success(employee);
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
    @Log
    @DeleteMapping("/{id}")
    @ApiOperation("根据id删除员工信息")
    @ApiImplicitParam(name = "id", value = "要删除员工对应的id")
    public R<String> delete(@PathVariable Long id){
        log.info("根据id删除员工信息");
        Employee emp = employeeService.getById(id);
        if(emp == null){
            return R.error("找不到用户，刷新后再试");
        }
        String headImg = emp.getHeadImg();
        //删除服务器当中存储的员工头像信息
        QiniuUtils.deleteFileFromQiniu(headImg);
        //删除数据库中的对应员工数据
        boolean isRemove = employeeService.removeById(id);
        if(isRemove){
            return R.success("删除成功");
        }
        return R.error("删除失败");
    }

    /**
     * 上传图片
     * @param imgFile 将图片上传到七牛云服务器上
     * @return
     */
    @PostMapping("/upload/{id}")
    @ApiOperation("用户修改头像方法")
    @ApiImplicitParam(name = "imgFile",value = "传入的图片对象")
    public R<String> updateHead(@RequestParam("imgFile") MultipartFile imgFile,@PathVariable Long id){
        System.out.println(id);
        Employee emp = employeeService.getById(id);
        String headImg = emp.getHeadImg();
        if(headImg != null){
            //删除用户原有的头像避免内存浪费
            QiniuUtils.deleteFileFromQiniu(headImg);
        }
        if(emp == null){
            return R.error("找不到用户");
        }
        String fileName = QiniuUtils.formatUpload(imgFile);
        emp.setHeadImg(fileName);
        employeeService.updateById(emp);
        return R.success(fileName);
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

}
