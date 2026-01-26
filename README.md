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
git init

git remote add origin git@github.com:zzc241/-1.0.git
//git@github.com:zzc241/-1.0.git在下面图片中

git remote -v//查看是否连接成功
//$ git remote -v
//origin  git@github.com:zzc241/-1.0.git (fetch)
//origin  git@github.com:zzc241/-1.0.git (push)
git pull origin main//拉取代码
git push origin main//上传代码
```

![](README.assets/git2.png)

```
27141@zc MINGW64 /d/中软果蔬项目资源/24013092源码/后端/fruits (1) (main)
$ git fetch origin zyr
From github.com:zzc241/-1.0

 * branch            zyr        -> FETCH_HEAD

27141@zc MINGW64 /d/中软果蔬项目资源/24013092源码/后端/fruits (1) (main)
$ git branch -r
  origin/main
  origin/zyr

27141@zc MINGW64 /d/中软果蔬项目资源/24013092源码/后端/fruits (1) (main)
$ git checkout -b zyr origin/zyr
branch 'zyr' set up to track 'origin/zyr'.
Switched to a new branch 'zyr'

27141@zc MINGW64 /d/中软果蔬项目资源/24013092源码/后端/fruits (1) (zyr)
$ git branch -v
  main d795273 [origin/main] 1.22(readme与时间+‘z’)

* zyr  6e36039 [origin/zyr] 修改：删除了多余属性typen

27141@zc MINGW64 /d/中软果蔬项目资源/24013092源码/后端/fruits (1) (zyr)
$ git pull origin zyr
remote: Enumerating objects: 52, done.
remote: Counting objects: 100% (52/52), done.
remote: Compressing objects: 100% (6/6), done.
remote: Total 24 (delta 12), reused 24 (delta 12), pack-reused 0 (from 0)
Unpacking objects: 100% (24/24), 1.67 KiB | 30.00 KiB/s, done.
From github.com:zzc241/-1.0

 * branch            zyr        -> FETCH_HEAD
   6e36039..16bd946  zyr        -> origin/zyr
   Updating 6e36039..16bd946
   Fast-forward
    ...\346\210\252\345\233\276 2026-01-22 142718.png" | Bin 0 -> 242454 bytes

27141@zc MINGW64 /d/中软果蔬项目资源/24013092源码/后端/fruits (1) (zyr)
$ git checkout main
Switched to branch 'main'
Your branch is up to date with 'origin/main'.

27141@zc MINGW64 /d/中软果蔬项目资源/24013092源码/后端/fruits (1) (main)
$ git merge zyr
Updating d795273..16bd946
Fast-forward

27141@zc MINGW64 /d/中软果蔬项目资源/24013092源码/后端/fruits (1) (main)
$ git push origin main
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

### 2.寒假时

### 3.寒假后