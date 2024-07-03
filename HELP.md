



## 不要在 foreach 循环里进行元素的 remove/add 操作。

List<String> list = new ArrayList<>();
list.add("1");
list.add("2");
Iterator<String> iterator = list.iterator();
while (iterator.hasNext()) {
String item = iterator.next();
if (删除元素的条件) {
iterator.remove();
}
}



## 方法名命名规范

A) Service/DAO 层方法命名规约
1） 获取单个对象的方法用 get 做前缀。
2） 获取多个对象的方法用 list 做前缀，复数结尾，如：listObjects。
3） 获取统计值的方法用 count 做前缀。
4） 插入的方法用 save/insert 做前缀。
5） 删除的方法用 remove/delete 做前缀。
6） 修改的方法用 update 做前缀。
B) 领域模型命名规约
1） 数据对象：xxxDO，xxx 即为数据表名。
2） 数据传输对象：xxxDTO，xxx 为业务领域相关的名称。
3） 展示对象：xxxVO，xxx 一般为网页名称。
4） model 是 DO/DTO/BO/VO 的统称，禁止命名成 xxxPOJO。



## 路径规范

├─src
│  ├─main
│  │  ├─java
│  │  │  └─com
│  │  │      └─acer
│  │  │          ├─annotation // 自定义注解
│  │  │          ├─annotation // 全局（异常捕获，全局统一返回ReturnsResult）
│  │  │          ├─aspect // 自定义切面
│  │  │          ├─config // config
│  │  │          ├─constant // 静态变量
│  │  │          ├─controller // controller
│  │  │          ├─dao // mapper
│  │  │          ├─exception // 自定义异常
│  │  │          ├─kafka // 消息队列
│  │  │          ├─model // model
│  │  │          │  └─vo    // 接收前端发送的数据。
│  │  │          │  └─dto   // 后端传给前端数据传输对象载体。
│  │  │          │  └─bo    // service业务层数据转换。BO 对象不得用于 controller 层
│  │  │          │  └─po    // 数据库中的表一一对应。
│  │  │          │  └─excel // 进行excel下载时需要继承BaseExcel 
│  │  │          ├─security // 安全框架
│  │  │          ├─service // 接口和实现
│  │  │          │  └─impl
│  │  │          └─utils // 工具类
│  │  └─resources
│  │      └─mapper // mapper XML
│  └─test // 单元测试
│      └─java


## 配置规范

脚手架中有三个配置文件

- application.yml
- application-dev.yml
- application-prod.yml

`application.yml` 为基础配置类 这里是通用配置 以及选择当前是dev还是prod

`application-dev.yml` 开发环境用的配置文件

`application-prod.yml` 生产环境用的配置文件

dev与prod配置好后 开发和部署时 只需要在application.yml中进行切换就好 不需要每次修改配置文件 容易出现错误


## 日志规约

说明：以 mppserver 应用为例，日志保存在/home/admin/mppserver/logs/mppserver.log，历史日志名称为 mppserver.log.2016-08-01





