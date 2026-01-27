# 日志

## 2026.1.22

1.删除了fruitTree.java里面的多余属性typen和area_name,修改该类下的type属性为typeId属性,将FruitTreeController.java下的getType一并修改为getTypeId

2.在过滤器添加了未登录人员转跳到首页`/index.html`



## 2026.1.23

1.增加了FruitTreeVo类，用于回显数据和接受前端表单，修改了分页查询部分使用的类（用来是用FruitTree类，现在删除了里面的typen等属性，原来功能由FruitTreeVo接替），修改了Mapper文件里ResultMap部分的属性映射

2.增加了AreaVo类，修改内容同上,修改了对应的查询方法使用的类



## 2026.1.24

1.添加了系统操作日志表，创建了LogAspect.java，通过AOP留下操作记录，对要记录的方法打上注解@Log后就会开始记录,对controller下的FruitTree、Employee、Area的登录、修改相关方法添加了@Log（目前还不支持记录分页查询相关方法）

2.增加了FruitType的查询方法getTypeById（Long id）；修改了FruitTreeController的删除方法。修改了FruitTreeDao.xml里的方法getTreeById（原：select fruit_type.type... -> 现在：select fruit_tree.type)



## 2026.1.25

1.添加Minio上传方法、预览图片方法

2.添加创建二维码功能

3.暂时去除系统日志功能



## 2026.1.27

1.修改了TaskController，添加了TaskVo类用于回显，添加条件查询方法
2.将数据库转移到阿里云云服务器上,用户root1到root8，所有密码都是123456

3.修改了EmpGroupServiceImpl类，添加了查询方法

