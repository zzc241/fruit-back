## 2026.1.22开始项目规划文件



### 1.寒假前

#### 1.22开始配合

##### 1.mysql互联

- vpn

![屏幕截图 2026-01-22 142718](D:\中软果蔬项目资源\24013092源码\后端\fruits (1)\README.assets\屏幕截图 2026-01-22 142718.png)

- navicat连接

```
C:\Windows\System32>mysql -h 26.65.158.32 -uroot -p
Enter password: *
```



```mysql
--这是给所有人root权限
--1. 创建/修改root@%用户密码
CREATE USER IF NOT EXISTS 'root'@%' IDENTIFIED BY '123456';

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



![屏幕截图 2026-01-22 144414](D:\中软果蔬项目资源\24013092源码\后端\fruits (1)\README.assets\屏幕截图 2026-01-22 144414.png)



##### 2.github互联

下面只给简单代码，零基础git请去看视频自学

先创建仓库（创建仓库随便找视频看），再将合作人加入仓库

![屏幕截图 2026-01-22 145827](D:\中软果蔬项目资源\24013092源码\后端\fruits (1)\README.assets\屏幕截图 2026-01-22 145827.png)



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

![屏幕截图 2026-01-22 145221](D:\中软果蔬项目资源\24013092源码\后端\fruits (1)\README.assets\屏幕截图 2026-01-22 145221.png)

##### 3.需求分析



### 2.寒假时

### 3.寒假后