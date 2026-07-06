# 基于 Spring Boot + Vue 的智能健康膳食推荐系统

本项目为计算机科学与技术本科毕业设计基础框架，包含：

- demo-backend：Spring Boot + MyBatis-Plus + MySQL 后端
- demo-frontend：Vue3 + Vite 前端
- database：数据库初始化 SQL

系统实现范围：用户注册登录、健康档案、食谱中心、食谱详情、智能膳食推荐、公告资讯、论坛交流、留言咨询、AI 问答、后台数据大屏。

系统不实现：在线支付、复杂商城、订单结算、物流配送、医疗诊断。

## 一、初始化数据库

1. 确认本机已经安装并启动 MySQL。
2. 使用 Navicat、DBeaver 或 MySQL 命令行执行：

```sql
source database/health_diet.sql;
```

也可以直接打开 `database/health_diet.sql`，复制全部内容到数据库工具中执行。

默认数据库名：

```text
health_diet
```

默认测试账号：

```text
管理员：admin / 123456
普通用户：user / 123456
```

## 二、修改后端数据库配置

打开：

```text
demo-backend/src/main/resources/application.yml
```

根据自己的 MySQL 修改：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/health_diet?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useSSL=false
    username: root
    password: 123456
```

## 三、运行后端

```bash
cd demo-backend
mvn spring-boot:run
```

默认地址：

```text
http://localhost:8080
```

## 四、运行前端

```bash
cd demo-frontend
npm install
npm run dev
```

默认地址：

```text
http://localhost:5173
```

## 五、后端持久化说明

当前版本已将原来的内存模拟数据改造为 MyBatis-Plus + MySQL 持久化实现，主要结构如下：

```text
demo-backend/src/main/java/com/example/healthdiet
├── common              统一返回结果、常量
├── config              跨域配置
├── controller          控制层接口
├── dto                 请求参数对象
├── entity              数据库实体类
├── mapper              MyBatis-Plus Mapper
├── service             业务接口
├── service/impl        业务实现类
└── util                工具类
```

核心接口示例：

```text
POST /api/user/login             用户登录
POST /api/user/register          用户注册
GET  /api/recipe/list            查询食谱列表
GET  /api/recipe/{id}            查询食谱详情
POST /api/recommend/generate     生成智能推荐
GET  /api/article/list           查询资讯列表
GET  /api/forum/list             查询论坛帖子
POST /api/message                提交留言
POST /api/ai/ask                 AI问答
GET  /api/dashboard/summary      数据大屏统计
```

## 六、页面风格

前台页面采用暖色健康轻食风格：米白色背景、暖橙色按钮、深棕色页脚、食谱卡片布局。整体风格参照“暖麦时光”模板，但业务逻辑已改造成健康膳食推荐系统。


## 后台暖色烘焙风格说明

管理员后台已调整为与前台一致的温馨烘焙感视觉：主色 `#D4856B`，辅色 `#F5E6D3`，背景 `#FFF8F0`。整体采用柔和米白背景、暖棕文字、圆角卡片、浅色侧边栏和暖色按钮，避免强烈橙红色后台风格。
