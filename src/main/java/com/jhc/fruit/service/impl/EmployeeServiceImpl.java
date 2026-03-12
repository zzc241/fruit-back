package com.jhc.fruit.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jhc.fruit.common.BaseContext;
import com.jhc.fruit.common.JsonUtil;
import com.jhc.fruit.common.JwtUtil;
import com.jhc.fruit.common.R;
import com.jhc.fruit.dao.EmployeeDao;
import com.jhc.fruit.dao.FruitTreeDao;
import com.jhc.fruit.entity.Employee;
import com.jhc.fruit.entity.vo.EmployeeVo;
import com.jhc.fruit.entity.vo.loginEmployeeVo;
import com.jhc.fruit.service.AreaService;
import com.jhc.fruit.service.EmployeeService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.jhc.fruit.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/4/28 0028 14:04
 */
@Slf4j
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeDao, Employee> implements EmployeeService {
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private AreaService areaService;
    @Autowired
    private FileService fileService;
    @Autowired
    private FruitTreeDao fruitTreeDao;


    @Override
    public List<Employee> getWorkers(){
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getIsAdmin,2);
        List<Employee> workers = list(wrapper);
        if(workers != null){
            return workers;
        }
        return null;
    }

    @Override
    public String getEmpNameById(Long empId){
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getId, empId);
        Employee employee = getOne(queryWrapper);
        String empName = employee.getName();
        return empName;
    }
    @Override
    public Long getEmpIdByName(String empName){
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getName, empName);
        Employee employee = getOne(queryWrapper);
        Long empId = employee.getId();
        return empId;
    }
    @Override
    public Map<Long, String> getEmpNameMapByIds(Set<Long> empIds){
        if (empIds == null || empIds.isEmpty()) {
            return new HashMap<>(); // 空集合直接返回空Map
        }
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Employee::getId, empIds);
        queryWrapper.select(Employee::getId, Employee::getName);
        List<Employee> employees = list(queryWrapper);
        Map<Long, String> empNameMap = employees.stream().collect(
            Collectors.toMap(
                Employee::getId, 
                Employee::getName,
                (existingValue, newValue) -> existingValue));
        return empNameMap;
    }

    public String getOpenId(String code, HttpServletRequest request){
        //拼接url
        if (code == null || code.isEmpty()) {
            return null;
        }else {
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
                log.error("code:001-" + e.getMessage());
            }
            if (map.get("openid") == null) {
                return null;
            } else {
                return map.get("openid").toString();
            }
        }
    }

    //通过微信id登录/游客登录
    @Override
    public R<Employee> openIdLogin(String code, HttpServletRequest request) {
        //获取微信id
        String openId = getOpenId(code, request);

        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getOpenId,openId);
        //根据openId查询用户
        Employee emp = employeeDao.selectOne(wrapper);
        if(emp == null){
            //代表用户第一次登录，还没有存入openId
            //存入数据库作为游客账户
            Employee employee = new Employee();
            employee.setOpenId(openId);
            employee.setCreateTime(LocalDateTime.now());
            //生成默认用户名
            employee.setUsername(UUID.randomUUID().toString());
            //生成默认密码
            employee.setPassword("123456");
            //设置为游客账户
            employee.setIsAdmin(5);

            log.info("游客登录：" + openId);
            //保存用户数据
            employeeDao.insert(employee);

            //登录成功，将用户id存入Session
            request.getSession().setAttribute("employee",employee.getId());
            //openId
            request.getSession().setAttribute("openId",openId);
            //果园id
            request.getSession().setAttribute("orchardId", employee.getOrchardId());
            //账户类型
            request.getSession().setAttribute("isAdmin", employee.getIsAdmin());

            return R.success(emp);
        }

        //登录成功，将用户id存入Session
        request.getSession().setAttribute("employee",emp.getId());
        //openId
        request.getSession().setAttribute("openId",openId);
        //果园id
        request.getSession().setAttribute("orchardId", emp.getOrchardId());
        //账户类型
        request.getSession().setAttribute("isAdmin", emp.getIsAdmin());

        //清空openId
        openId = null;
        return R.success(emp);
    }

    //用户密码登录
    @Override
    public R<Employee> login(loginEmployeeVo loginEmployeeVo, HttpServletRequest request) {
        //获取前端提交的数据
        String userName = loginEmployeeVo.getUsername();
        String password = loginEmployeeVo.getPassword();
        String code = loginEmployeeVo.getCode();

        //1.将提交的密码进行md5处理
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        System.out.println(password);

        //2.根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,userName);
        Employee employee = employeeDao.selectOne(queryWrapper);
        //3.如果没有查询到则返回登录失败结果
        if(employee == null){
            return R.error("账户不存在");
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
        //果园id
        request.getSession().setAttribute("orchardId", employee.getOrchardId());
        //账户类型
        request.getSession().setAttribute("isAdmin", employee.getIsAdmin());

        Map<String, Object> claim = new HashMap<>();
        claim.put("id", employee.getId());
        claim.put("username", employee.getUsername());
        String jwt = JwtUtil.generateJwt(claim);

        //获取用户微信id
        if(getOpenId(code, request) != null){
            String openId = getOpenId(code, request);
            //openId
            request.getSession().setAttribute("openId",openId);

            //存入该员工的openId
            employee.setOpenId(openId);


        }
        //防止重复登录，账号状态禁用
        //TODO:测试阶段，用户状态先注释掉
        // employee.setStatus(0);
        //更新用户信息
        employeeDao.updateById(employee);
        return R.success(employee,jwt);
    }

    @Override
    public R<String> deleteList(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return R.error("没有选择员工");
        }
        try {
            LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Employee::getId, ids);
            employeeDao.delete(queryWrapper);
            return R.success("操作成功");
        }catch (Exception e){
            log.info(e.getMessage());
            return R.error("操作失败");
        }
    }

    @Override
    public Long getOrchardIdByEmpId(Long empId) {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getId, empId);
        Employee employee = employeeDao.selectOne(queryWrapper);
        return employee.getOrchardId();
    }

    @Override
    public List<EmployeeVo> getEmpByOrchardId(Long orchardId){
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getOrchardId, orchardId);
        List<Employee> employees = employeeDao.selectList(queryWrapper);
        List<EmployeeVo> employeeVos = new ArrayList<>();
        for (Employee employee : employees) {
            EmployeeVo employeeVo = new EmployeeVo();
            BeanUtils.copyProperties(employee, employeeVo);
            employeeVo.setAreaId(areaService.getAreaIdByEmpId(employee.getId()));
            employeeVos.add(employeeVo);
        }
        return employeeVos;
    }

    @Override
    public R<String> saveHeadImg(MultipartFile file, String headImg) {
        String url = fileService.save(file, headImg);
        //获取当前登录用户
        Long id = BaseContext.getId();
        if (id == null){
            return R.error("非法id");
        }
        if (file == null){
            return R.error("非法图片");
        }

        //更新头像
        Employee employee = employeeDao.selectById(id);
        if (employee == null){
            return R.error("ERROR, user is no exits");
        }
        employee.setHeadImg(url);
        employeeDao.updateById(employee);
        return R.success(url);
    }

    @Override
    public R<String> updateStatus(Long id) {
        Employee employee = employeeDao.selectById(id);
        if (employee.getStatus() == 1) {
            employee.setStatus(0);
            employeeDao.updateById(employee);
            return R.success("user:" + employee.getUsername() + " disable");
        }else {
            employee.setStatus(1);
            employeeDao.updateById(employee);
            return R.success("user:" + employee.getUsername() + " enable");
        }
    }

    @Override
    public EmployeeVo getInfo(Employee employee) {
        Long orchardId = employee.getOrchardId();
        String orchardName = fruitTreeDao.getOrchardNameById(orchardId);
        List<String> areaList = areaService.listName(employee.getId());
        EmployeeVo employeeVo = new EmployeeVo();
        BeanUtils.copyProperties(employee, employeeVo);
        employeeVo.setOrchardName(orchardName);
        employeeVo.setAreaList(areaList);
        return employeeVo;
    }

    @Override
    public R<String> auth(Employee employee) {
        //实名认证的前提是已经有账户了
        Long id = employee.getId();
        if(id == null){
            //说明账户不存在
            return R.error("账户不存在");
        }
        //账户存在
        //校验手机号和身份证
        if (isValidPhone(employee.getPhone())) {
            if (isValidIdCard(employee.getIdCard())){
                employeeDao.updateById(employee);
                return R.success("认证成功");
            }else {
                return R.error("身份证校验失败");
            }
        }else {
            return R.error("手机号校验失败");
        }

    }

    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    /**
     * 校验手机号是否合法
     * @param phone 手机号（支持空字符串、null、带空格/横线的格式）
     * @return true=合法，false=不合法
     */
    public static boolean isValidPhone(String phone) {
        // 1. 空值/空白校验
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        // 2. 去除空格、横线等干扰字符
        String cleanPhone = phone.replaceAll("\\s+|-", "");
        // 3. 正则匹配
        return PHONE_PATTERN.matcher(cleanPhone).matches();
    }

    private static final Pattern ID_CARD_PATTERN = Pattern.compile("^[1-9]\\d{5}(19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$");
    /**
     * 身份证号校验码权重
     */
    private static final int[] ID_CARD_WEIGHT = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    /**
     * 身份证号校验码对应值
     */
    private static final char[] ID_CARD_CHECK_CODE = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    /**
     * 校验18位身份证号是否合法（含格式+校验码双重校验）
     * @param idCard 身份证号（支持空字符串、null、带空格的格式）
     * @return true=合法，false=不合法
     */
    public static boolean isValidIdCard(String idCard) {
        // 1. 空值/空白校验
        if (StringUtils.isBlank(idCard)) {
            return false;
        }
        // 2. 去除空格
        String cleanIdCard = idCard.trim();
        // 3. 格式正则校验
        if (!ID_CARD_PATTERN.matcher(cleanIdCard).matches()) {
            return false;
        }

        // 4. 校验码验证（核心：防止伪造身份证号）
        try {
            // 前17位数字求和
            int sum = 0;
            for (int i = 0; i < 17; i++) {
                sum += (cleanIdCard.charAt(i) - '0') * ID_CARD_WEIGHT[i];
            }
            // 计算校验码
            int mod = sum % 11;
            char checkCode = ID_CARD_CHECK_CODE[mod];
            // 对比最后一位（忽略大小写）
            char lastChar = cleanIdCard.charAt(17);
            return Character.toUpperCase(lastChar) == checkCode;
        } catch (Exception e) {
            // 数字转换异常（理论上不会触发，因正则已校验）
            return false;
        }
    }

    @Override
    public String getEmpImgById(Long empId) {
        Employee employee = employeeDao.selectById(empId);
        return employee.getHeadImg();
    }



    public List<EmployeeVo> getAllEmployees(){
        List<Employee> list = employeeDao.selectList(null);
        List<EmployeeVo> employeeVoList = new ArrayList<>();
        for (Employee employee : list) {
            EmployeeVo employeeVo = new EmployeeVo();
            String orchardName = fruitTreeDao.getOrchardNameById(employee.getOrchardId());
            employeeVo.setOrchardName(orchardName);
            employeeVoList.add(getInfo(employee));
        }
        return employeeVoList;
    }
    public List<EmployeeVo> getAllEmployeesByOrchardId(Long orchardId){
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getOrchardId, orchardId);
        List<Employee> list = employeeDao.selectList(queryWrapper);
        List<EmployeeVo> employeeVoList = new ArrayList<>();
        for (Employee employee : list) {
            EmployeeVo employeeVo = new EmployeeVo();
            String orchardName = fruitTreeDao.getOrchardNameById(employee.getOrchardId());
            employeeVo.setOrchardName(orchardName);
            employeeVoList.add(getInfo(employee));
        }
        return employeeVoList;
    }


}
