- ## 概要

  目标: 这是一个基于 Spring Boot 的果园管理后端，提供员工、果园、任务、新闻、气象等业务接口。
  启动类: [FruitApplication.java](src/main/java/com/jhc/fruit/FruitApplication.java)

  ## 技术栈

  框架: Spring Boot（@SpringBootApplication）、Spring 事务管理。
  持久层: MyBatis-Plus（mapper XML 存于 [src/main/resources/mappers](src/main/resources/mappers)），自动填充由 [MyMetaObjectHandler.java](src/main/java/com/jhc/fruit/common/MyMetaObjectHandler.java) 支持。
  数据库连接池: Druid（在 [src/main/resources/application.yml](src/main/resources/application.yml) 中配置）。
  对象映射/序列化: Jackson（自定义 [JacksonObjectMapper.java](src/main/java/com/jhc/fruit/common/JacksonObjectMapper.java)）。
  认证/鉴权: 简易 JWT 工具类 [JwtUtil.java](src/main/java/com/jhc/fruit/utils/JwtUtil.java) + 请求过滤器 [LoginCheckFilter.java](src/main/java/com/jhc/fruit/filter/LoginCheckFilter.java)。
  文件存储: MinIO（配置在 [src/main/resources/application.yml](src/main/resources/application.yml)）。
  文档/工具: Swagger（有 [SwaggerConfig.java](src/main/java/com/jhc/fruit/config/SwaggerConfig.java)），AOP 日志切面（[LogAspect.java](src/main/java/com/jhc/fruit/aop/LogAspect.java)）。
  构建: Maven（[pom.xml](pom.xml)）。

  ## 项目结构（高层）

  - **controller**: 对外 REST 接口（示例：`EmployeeController`、`TaskController` 等）
  - **service / service.impl**: 业务逻辑层，接口+实现分离
  - **dao**: MyBatis/Mapper 接口（配合 `src/main/resources/mappers/*.xml`）
  - **entity / entity.vo**: 实体与视图对象（VO）
  - **common**: 工具类与通用组件（[JwtUtil.java](src/main/java/com/jhc/fruit/utils/JwtUtil.java)、[R.java](src/main/java/com/jhc/fruit/common/R.java)、[MyMetaObjectHandler.java](src/main/java/com/jhc/fruit/common/MyMetaObjectHandler.java)、[GlobalExceptionHandler.java](src/main/java/com/jhc/fruit/common/GlobalExceptionHandler.java) 等）
  - **config**: 全局配置（[MybatisPlusConfig.java](src/main/java/com/jhc/fruit/config/MybatisPlusConfig.java)、[MinioConfig.java](src/main/java/com/jhc/fruit/config/MinioConfig.java)、[SwaggerConfig.java](src/main/java/com/jhc/fruit/config/SwaggerConfig.java)、[CorsConfig.java](src/main/java/com/jhc/fruit/config/CorsConfig.java)、[WebMvcConfig.java](src/main/java/com/jhc/fruit/config/WebMvcConfig.java) 等）
  - **filter / aop**: 请求拦截与切面（[LoginCheckFilter.java](src/main/java/com/jhc/fruit/filter/LoginCheckFilter.java)、[LogAspect.java](src/main/java/com/jhc/fruit/aop/LogAspect.java)）
  - **resources/mappers**: 自定义 SQL 与复杂查询的 XML 映射文件（例如 [FruitTreeDao.xml](src/main/resources/mappers/FruitTreeDao.xml)）
  - **test**: 单元/集成测试用例（[src/test/java](src/test/java)）

  ## 请求处理流（典型）

  1. 客户端 → 服务器: HTTP 请求到达 Spring 容器（可走 CORS 配置）。
  2. 过滤: [LoginCheckFilter.java](src/main/java/com/jhc/fruit/filter/LoginCheckFilter.java) 做鉴权/白名单检查（携带或校验 JWT）。
  3. 控制器: Controller 层接收并校验输入，调用 Service。
  4. 业务层: Service 层执行业务逻辑、事务控制（@Transactional）并调用 Dao。
  5. 持久层: Dao 使用 MyBatis-Plus 或 mapper XML 执行 SQL，并返回实体/VO。
  6. 响应: 统一响应类 [R.java](src/main/java/com/jhc/fruit/common/R.java) 封装返回结果；异常由 [GlobalExceptionHandler.java](src/main/java/com/jhc/fruit/common/GlobalExceptionHandler.java) 统一处理。

  ## 重要文件 / 参考位置

  - 主类: [FruitApplication.java](src/main/java/com/jhc/fruit/FruitApplication.java)
  - 配置: [application.yml](src/main/resources/application.yml)（数据源、MyBatis-Plus、MinIO、端口等）
  - MyBatis-Plus 配置: [MybatisPlusConfig.java](src/main/java/com/jhc/fruit/config/MybatisPlusConfig.java)
  - JWT 工具: [JwtUtil.java](src/main/java/com/jhc/fruit/utils/JwtUtil.java)
  - 鉴权过滤器: [LoginCheckFilter.java](src/main/java/com/jhc/fruit/filter/LoginCheckFilter.java)
  - AOP 日志: [LogAspect.java](src/main/java/com/jhc/fruit/aop/LogAspect.java)
  - 自动填充: [MyMetaObjectHandler.java](src/main/java/com/jhc/fruit/common/MyMetaObjectHandler.java)
  - 异常处理: [GlobalExceptionHandler.java](src/main/java/com/jhc/fruit/common/GlobalExceptionHandler.java)
  - 示例 mapper XML: [FruitTreeDao.xml](src/main/resources/mappers/FruitTreeDao.xml)
  - 构建文件: [pom.xml](pom.xml)

**架构特性与注意点**

- **混合使用 MP 与 XML**: 项目同时使用 MyBatis-Plus 注解/方法和传统 XML，适合简单 CRUD 与复杂 SQL 并存。
- **集中配置**: 大量外部依赖（MinIO、AI 接口、证书密码等）都在 application.yml，注意环境隔离与敏感信息管理。
- **鉴权简单**: JWT + 过滤器实现轻量鉴权，若有更高安全要求，建议引入 Spring Security。
- **日志与异常**: AOP + 全局异常处理实现统一监控与错误返回，便于运维。





## 2026.1.22开始项目规划文件



### 1.寒假前

#### 1.22开始配合

##### 1.mysql互联

- vpn

![](README.assets/vpn.png)

- navicat连接

```
C:\Windows\System32>mysql -h 26.65.158.32 -uroot -p
Enter password: *
```



```mysql
--这是给所有人root权限
--1. 创建/修改root@%用户密码
CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY '123456';

-- 2. 授权所有权限
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;

-- 3. 刷新权限
FLUSH PRIVILEGES;
```

```mysql
--这是给特定人root权限（ip时对方vpn的ip），如果不懂就用上面的代码即可
--1. 创建/修改root@26.95.158.32用户密码
CREATE USER IF NOT EXISTS 'root'@'26.95.158.32' IDENTIFIED BY '123456';

-- 2. 授权所有权限
GRANT ALL PRIVILEGES ON *.* TO 'root'@'26.95.158.32' WITH GRANT OPTION;

-- 3. 刷新权限
FLUSH PRIVILEGES;
```



![](README.assets/navicat.png)

##### 2.github互联

下面只给简单代码，零基础git请去看视频自学

先创建仓库（创建仓库随便找视频看），再将合作人加入仓库

![](README.assets/git1.png)



```git
//自己创建仓库
git init
//克隆远程仓库
git clone https://github.com/QYQY-QY/orchard-manger.git

git remote add origin git@github.com:zzc241/-1.0.git
//git@github.com:zzc241/-1.0.git在下面图片中

git remote -v//查看是否连接成功
//$ git remote -v
//origin  git@github.com:zzc241/-1.0.git (fetch)
//origin  git@github.com:zzc241/-1.0.git (push)
git pull origin main//拉取代码
git push origin main//上传代码

//合作时的基本步骤
合作者1(main分支)、合作者2(zyr分支)
合作者2先提交分支
git push origin zyr
合作者1拉取合作者2分支
git pull origin zyr
合作者1提交分支
git push origin main
合作者2拉取合作者1分支
git pull origin main

//自己上传代码
# 1. 添加所有修改
git add .

# 2. 提交到本地仓库
git commit -m "暂存本地修改"

# 3. 拉取远程代码
git pull origin zyr

# 4. 如果有冲突，解决冲突后再次提交
git add .
git commit -m "解决合并冲突"

//本地有更改但想要拉取别人的代码
# 1. 暂存本地修改
git stash

# 2. 拉取远程代码
git pull origin zyr

# 3. 恢复暂存的修改
git stash pop

//下面的git代码基本不需要用到，了解即可
```

![](README.assets/git2.png)

```
27141@zc MINGW64 /d/中软果蔬项目资源/24013092源码/后端/fruits (1) (main)
$ git fetch origin zyr      //这个代码适用于查看别人分支
From github.com:zzc241/-1.0

 * branch            zyr        -> FETCH_HEAD

27141@zc MINGW64 /d/中软果蔬项目资源/24013092源码/后端/fruits (1) (main)
$ git branch -r
  origin/main
  origin/zyr

27141@zc MINGW64 /d/中软果蔬项目资源/24013092源码/后端/fruits (1) (main)
$ git checkout -b zyr origin/zyr    //这是让合作方创建分支
branch 'zyr' set up to track 'origin/zyr'.
Switched to a new branch 'zyr'


27141@zc MINGW64 /d/中软果蔬项目资源/24013092源码/后端/fruits (1) (zyr)
$ git pull origin zyr       //合并分支
remote: Enumerating objects: 52, done.
remote: Counting objects: 100% (52/52), done.
remote: Compressing objects: 100% (6/6), done.
remote: Total 24 (delta 12), reused 24 (delta 12), pack-reused 0 (from 0)

27141@zc MINGW64 /d/中软果蔬项目资源/24013092源码/后端/fruits (1) (zyr)
$ git checkout main            //回到自己的主分支
Switched to branch 'main'
Your branch is up to date with 'origin/main'.

27141@zc MINGW64 /d/中软果蔬项目资源/24013092源码/后端/fruits (1) (main)
$ git merge zyr     //合并本地zyr分支          
Updating d795273..16bd946
Fast-forward

27141@zc MINGW64 /d/中软果蔬项目资源/24013092源码/后端/fruits (1) (main)
$ git push origin main  //推送本地分支
Total 0 (delta 0), reused 0 (delta 0), pack-reused 0 (from 0)
To github.com:zzc241/-1.0.git
   d795273..16bd946  main -> main

```



#### 1.25

##### 1.编写接口pickInfo

- 基本将所有代码都进行修改，在很多地方直接用mp，不通过dao进行查询。
- 保留（后台：查看全部未完成通知、后台：查看全部完成通知），因为后端测试https://127.0.0.1:3388/index.html有这两个接口方便调试
- 将分页查询进行复杂化（包括模糊查询、查询是否完成、是否已读）
- 实现基本增删改功能
- TODO：还需要进行身份判定是否能用这个功能

##### 2.修改用户登录逻辑

##### 总结

| 前端请求方式 | 用途       | 用途说明                 | 示例场景               |
| ------------ | ---------- | ------------------------ | ---------------------- |
| GET          | 简单查询   | 获取资源，不修改数据     | 查询列表、下载文件     |
| POST         | 复杂增改查 | 创建新资源，复杂操作     | 复杂查询（带分页过滤） |
| PUT          | 简单修改   | 完整更新资源（全部字段） | 更新用户全部信息       |
| DELETE       | 删除       | 删除资源                 |                        |

#### 1.26

##### 1.新增果园管理接口

- 实现果园的数据库表和类、Vo，（Vo内容比类多，多了分页的page、pagesize和有关id的name，表中有id，但是这个id的name在其他表中就需要放到Vo中）
- 方法中实现最基础的分页查、增删改

2.将id都改成Long类型、数据库表中改成bigint，方便在service中写



#### 1.27

##### 1.实现分页查询中多表查询的合并查询

- 先将想要查的id放到set中防止重复
- 用map给id和name实现映射
- 调用远程的getNameById接口（LambdaQureyWrapper的in和select）实现查找，如果i没有id返回nul
- 最后在Vo中找对应name，再传

##### 2.实现远程服务器的数据库部署

```
spring:
  datasource:
    druid:
      driver-class-name: com.mysql.jdbc.Driver
      url: jdbc:mysql://120.27.205.171:3306/fruit?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&allowPublicKeyRetrieval=true&useSSL=false
      username: root1
      password: 123456
```

##### 3.test如何通过mvn测试

```
暂时还没找到解决方案，选择跳过直接打包
mvn clean package "-Dmaven.test.skip=true"
```





### 2.寒假后

##### 解决时间问题

推荐用LocalDateTime，前后端都不用时区

| java.time.LocalDateTime | java.util.Date | java.sql.Date |
| ----------------------- | -------------- | ------------- |
| 高级                    | 低级           | 低级          |
| 无时区                  | 有时区         |               |

##### 解决数据库自增还是雪花算法

```sql
//数据库自增：
@TableId(type = IdType.AUTO)

//数据库自增从0开始：
ALTER TABLE 表名 AUTO_INCREMENT = 0;
//注意：如果表里有id就会从id中最大的开始自增防止重复

//雪花算法：
@TableId(type = IdType.ASSIGN_ID)
```

##### 经典数据库报错

###### json格式错误

```
1|fruit2   | 2026-03-08 09:59:13.862  WARN 2922 --- [nio-3388-exec-8] .w.s.m.s.DefaultHandlerExceptionResolver : Resolved [org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error: Unexpected character ('"' (code 34)): was expecting comma to separate Object entries; nested exception is com.fasterxml.jackson.core.JsonParseException: Unexpected character ('"' (code 34)): was expecting comma to separate Object entries<EOL> at [Source: (PushbackInputStream); line: 12, column: 4]]
//出现JsonParseException就是json格式错误
```

###### 空指针异常

```
0|fruit  | ==>  Preparing: SELECT id,orchard_id,name,size,type_id,create_time,update_time,description,create_user,update_user,emp_id FROM area_info WHERE (emp_id = ?) 
0|fruit  | ==> Parameters: 0(Long)
0|fruit  | <==      Total: 0
0|fruit  | Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@581a1cbb]
0|fruit  | 2026-03-07 17:01:36.742 ERROR 25992 --- [nio-3388-exec-9] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is java.lang.NullPointerException: Cannot invoke "com.jhc.fruit.entity.Area.getId()" because "area" is null] with root cause
0|fruit  | java.lang.NullPointerException: Cannot invoke "com.jhc.fruit.entity.Area.getId()" because "area" is null
0|fruit  | 	at com.jhc.fruit.service.impl.AreaServiceImpl.getAreaIdByEmpId(AreaServiceImpl.java:106) ~[classes!/:0.0.1-SNAPSHOT]
```

###### 语句不完整（一般会在插入的时候出现）

```
[org.apache.ibatis.session.defaults.DefaultSqlSession@6c170276]
2026-03-07 21:15:00.861 ERROR 28496 --- [nio-3388-exec-9] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is org.springframework.jdbc.BadSqlGrammarException:
//这里出现BadSqlGrammarException
### Error updating database.  Cause: com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near '' at line 1
### The error may exist in com/jhc/fruit/dao/FruitGrowDao.java (best guess)
### The error may involve com.jhc.fruit.dao.FruitGrowDao.insert-Inline
### The error occurred while setting parameters
### SQL: INSERT INTO fruit_grow    VALUES
### Cause: com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near '' at line 1
; bad SQL grammar []; nested exception is com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near '' at line 1] with root cause
```



##### 前后端联调报错

###### 前端登录被拦截

```
0|fruit  | 2026-03-06 09:52:35.781  INFO 20631 --- [io-3388-exec-10] com.jhc.fruit.filter.LoginCheckFilter    : 用户未登录拦截到请求/api/employee/login
```

原因：后端只放行employee/login，没有放行/api/employee/login

```
String[] urls = new String[]{
                //放行登录页面请求
                "/static/**",
                "/index.html",
                //放行用户登录登出方法
                "/employee/getOpenId",
                "/employee/login",
                "/employee/logout",
                //放行游客浏览新闻方法
                "/news/**",
                //放行Swagger
                "/swagger-ui.html/**",
                "/webjars/**",
                "/v2/**",
                "/swagger-resources/**",
                "/null/swagger-resources/**"
        };
```

解决方法：放行

```
String[] urls = new String[]{
                //放行登录页面请求
                "/static/**",
                "/index.html",
                //放行用户登录登出方法
                "/employee/getOpenId",
                "/employee/login",
                "/employee/logout",
                //放行扫描二维码
                "/fruitTree/showTree",
                //放行游客浏览新闻方法
                "/news/**",
                "/api/employee/getOpenId",
                "/api/employee/login",    
                "/api/employee/logout",   
                "/api/news/**",
                //放行Swagger
                "/swagger-ui.html/**",
                "/webjars/**",
                "/v2/**",
                "/swagger-resources/**",
                "/null/swagger-resources/**"
        };
```



##### 后端调AI接口报错

###### 接受类和接口返回类型不匹配

```
public class MaturityResponse {
    
    private Integer code;
    private String msg;
    private String answer;
    
    @JsonProperty("image_data")
    private String imageData;  // 新增：图片十六进制数据
    
    @JsonProperty("ripe_stats")
    private RipeStats ripeStats;
    
    @Data
    public static class RipeStats {
        @JsonProperty("total_fruits")
        private Integer totalFruits;
        
        @JsonProperty("maturity_distribution")
        private Map<String, Integer> maturityDistribution;  // 新增：成熟度分布
        
        @JsonProperty("ripe_fruits")
        private Integer ripeFruits;  // 新增：成熟果实数量
        
        @JsonProperty("ripe_rate")
        private Double ripeRate;  // 熟果率，JSON里是 50 表示 50%
    }
//如果这里面没有ai返回的对应类型就不会接收
```



###### 传给接口文件类型不匹配

```
public R<MaturityResponse> identifyMaturity(String maturityFile)
//如果ai接口要获取MultipartFile的数据格式，你给他传String的话ai接口报错
public R<MaturityResponse> identifyMaturity(MultipartFile maturityFile)
```

###### 传给接口文件名不匹配

```
fileParams.put("maturityFile", maturityFile);
//如果ai接口要获取file名称的数据，你给他传maturityFile的话ai接口报错
fileParams.put("file", maturityFile);  // 参数名根据接口文档调整
```



##### 项目优化

- 这是一段通过empid查询emp的task的代码。不难发现其笛卡尔积是三个表的数据相乘，三个表连起来效率非常低。


```sql
<select id="getTasksByEmpId" resultType="com.jhc.fruit.entity.Task">
        SELECT task.* from task,emp_group,empgroup_emp
        where
        empgroup_emp.emp_id = #{empId}
        and empgroup_emp.group_id = emp_group.id
        and task.group_id = emp_group.id
</select>
```

思路：可以优化成子查询，通过emp_group查到groupId再在task中查询。

```sql
<select id="getTasksByEmpId" resultType="com.jhc.fruit.entity.Task">
        SELECT task.* 
        FROM task
        WHERE task.group_id IN (
            SELECT group_id FROM empgroup_emp 
            WHERE emp_id = #{empId}
        )
</select>
```

- fileServiceImpl应该放在工具类中，由于一开始没有规划好导致。日后有时间可以优化
