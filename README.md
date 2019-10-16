**项目说明** 
- 本项目基于csdn博主smooth00的文章及其开源的代码进行二次开发,文章链接:https://blog.csdn.net/smooth00/article/details/83380879，

**内核function如下**
- 基于Jmeter-Api和Jmeter脚本实现在线性能压测(已有功能)。
- 基于Nose和TestNg实现在线接口测试(新增功能)
- 覆盖allure报告展示及自定义测试报告展示(新增功能)
- 覆盖三方工具的集成及管理(新增功能)
<br> 
 


**具有如下特点** 
- 友好的代码结构及注释，便于阅读及二次开发
- 实现前后端分离，通过token进行数据交互，前端再也不用关注后端技术
- 灵活的权限控制，可控制到页面或按钮，满足绝大部分的权限需求
- 页面交互使用Vue2.x，极大的提高了开发效率
- 完善的代码生成机制，可在线生成entity、xml、dao、service、html、js、sql代码，减少70%以上的开发任务
- 引入quartz定时任务，可动态完成任务的添加、修改、删除、暂停、恢复及日志查看等功能
- 引入API模板，根据token作为登录令牌，极大的方便了APP接口开发
- 引入Hibernate Validator校验框架，轻松实现后端校验
- 引入云存储服务，已支持：七牛云、阿里云、腾讯云等（暂时未变更这块代码）
- 引入swagger文档支持，方便编写API接口文档
- 引入路由机制，刷新页面会停留在当前页
- 引入最新版本Jmeter-Api，支持分布式压测，测试报告生成及在线查看下载。
- 引入Echarts，支持在线观测性能压测结果。
- 引入mock-runner支持平台修改配置文件(新增功能)
- 引入接口测试框架支持: 基于Java语言的TestNg测试框架及基于python语言的nose测试框架(新增功能)
- 引入三方工具接入机制进行服务的启停及web页面跳转，本质是将shell脚本service化，集成的时候通过service 服务名 start/stop(新增功能)
- 支持mock出来的接口文档以pdf形式生成(新增功能)
- 支持用例的增、删、改，支持Allure报告、自定义报告的下载、查看(新增功能)
<br> 

**项目结构** 
```
renren-fast
├─doc  项目SQL语句
│
├─common 公共模块
│  ├─aspect 系统日志
│  ├─exception 异常处理
│  ├─validator 后台校验
│  └─xss XSS过滤
│ 
├─config 配置信息
│ 
├─modules 功能模块
│  ├─api API接口模块(APP调用)
│  ├─job 定时任务模块
│  ├─oss 文件服务模块
│  ├─sys 权限模块
│  └─test 测试模块（该工程的主要改进点点如下）
	├── config
	│   ├── ExecutorConfig.java
	│   └── TestWebMvcConfig.java
	├── controller
	│   ├── ApiTestFileController.java
	│   ├── ApiTestReportsController.java
	│   ├── DebugTestReportsController.java
	│   ├── DockerToolsController.java
	│   ├── FireworkInformationController.java
	│   ├── InterfaceTestController.java
	│   ├── LogWebSocketController.java
	│   ├── MockApiInfoFullController.java
	│   └── ThirdToolsController.java
	├── dao
	│   ├── ApiTestFileDao.java
	│   ├── ApiTestReportsDao.java
	│   ├── DebugTestReportsDao.java
	│   ├── FireworkInformationDao.java
	│   ├── InterfaceTestDao.java
	│   ├── MockApiInfoFullDao.java
	│   ├── NodeDao.java
	│   └── ThirdToolsDao.java
	├── entity
	│   ├── ApiTestFileEntity.java
	│   ├── ApiTestReportsEntity.java
	│   ├── DebugTestReportsEntity.java
	│   ├── FireworkInformationEntity.java
	│   ├── InterfaceTestEntity.java
	│   ├── MockApiInfoFullEntity.java
	│   ├── NodeEntity.java
	│   └── ThirdToolsEntity.java
	├── handler
	│   ├── FileExecuteResultHandler.java
	│   ├── FileResultHandler.java
	│   ├── FileStopResultHandler.java
	│   └── ReportCreateResultHandler.java
	├── service
	│   ├── ApiTestFileService.java
	│   ├── ApiTestReportsService.java
	│   ├── DebugTestReportsService.java
	│   ├── FireworkInformationService.java
	│   ├── impl
	│   │   ├── ApiReportsServiceImpl.java
	│   │   ├── ApiTestFileServiceImpl.java
	│   │   ├── DebugTestReportsServiceImpl.java
	│   │   ├── FireworkInformationServiceImpl.java
	│   │   ├── InterfaceTestServiceImpl.java
	│   │   ├── MockApiInfoFullServiceImpl.java
	│   │   ├── NodeServiceImpl.java
	│   │   └── ThirdToolsServiceImpl.java
	│   ├── InterfaceTestService.java
	│   ├── MockApiInfoFullService.java
	│   ├── NodeService.java
	│   └── ThirdToolsService.java
	└── utils
		├── ApiTestUtils.java
		├── CommonUtils.java
		├── Constant.java
		├── JavaPythonUtils.java
		├── LogFilter.java
		├── LoggerMessage.java
		├── LoggerQueue.java
		├── SSH2Utils.java
		├── StreamHandler.java
		├── StressTestUtils.java
		├── ThirdToolsUtils.java
		├── WriteJsonFile.java
		├── WriteJsonFileThread.java
		├── WritePdfFile.java
		└── WritePdfFileThread.java
│ 
├─RenrenApplication 项目启动类
│  
├──resources 
│  ├─mapper SQL对应的XML文件
│  ├─static 第三方库、插件等静态资源
│  └─views  项目静态页面

```


**技术选型：** 
- 核心框架：Spring Boot 1.5
- 安全框架：Apache Shiro 1.3
- 视图框架：Spring MVC 4.3
- 持久层框架：MyBatis 3.3
- 定时器：Quartz 2.3
- 数据库连接池：Druid 1.0
- 日志管理：SLF4J 1.7、Log4j
- 页面交互：Vue2.x 
- 前端监控：ECharts 3.8
- 压测内核：Apache JMeter 4.0
- 脚本调用内核：Apache Commons Exec 1.3
- 远程执行命令：Ganymed build210及ssh工具组件
- 批量上传组件：bootstrap-fileinput v4.5.2
<br> 


 **本地部署**
- 通过git下载源码
- 创建数据库renren_fast，数据库编码为UTF-8
- 执行doc/db.sql文件，初始化数据
- 修改application.yml，更新server的ip、端口；如果引入缓存还需要修改redis的ip及端口信息
- 修改application-dev.yml，更新MySQL的url、账号和密码
- Eclipse、IDEA运行RenrenApplication.java，则可启动项目 
- 项目访问路径：http://localhost:8080/renren-fast
- 账号密码：admin/admin
- Swagger路径：http://localhost:8080/renren-fast/swagger/index.html
- 简单的启停脚本参见:shut_down.sh、start_up.sh


 **远程部署**
- 修改ansible-deploy.yml中的hosts、users、部署路径，运行ansible相关命令即可

 **docker化**
- 基于application.yml配置的服务端口，修改docker-compose.yml中的镜像名、端口映射，dockerfile中的端口，通过mvn的dockerfile-maven-plugin插件进行镜像构建及发布，具体可参见demo:pom.xml.docker文件，并修改其中的<configuration>下的<repository>信息

 **jenkins流水线方式部署服务于k8s**
- 在jenkins的Credentials设置中配置jenkins-harbor-creds、jenkins-k8s-config参数
- 修改Jenkinsfile中parameters下的相关参数信息及k8s-deployment.tpl中相关信息
- 提交git，出发jenkins拉取最新代码，进行流水线部署

<br> 
