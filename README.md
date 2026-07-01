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

## 低糖杂粮粥图片修复说明

本版本已将“低糖杂粮粥”的失效外链图片改为前端本地图片：

```text
/images/recipe-porridge.svg
```

如果你的 MySQL 数据库已经导入过旧数据，不需要重建数据库，只需执行：

```sql
source database/fix_low_sugar_porridge_image.sql;
```

或直接执行：

```sql
UPDATE recipe
SET image = '/images/recipe-porridge.svg'
WHERE name = '低糖杂粮粥';
```

同时，食谱卡片、食谱详情、个人中心收藏、后台菜品管理页面均已增加图片加载失败时的默认图兜底逻辑。

## 健康资讯详情页更新

本版本完善了前台“健康资讯”模块：

- 资讯列表卡片可点击进入详情页
- 首页健康资讯卡片可点击进入详情页
- 新增前端路由：`/article/:id`
- 新增前端页面：`demo-frontend/src/views/front/ArticleDetail.vue`
- 新增接口：`GET /api/article/{id}`
- 进入详情页时自动增加浏览量
- 新增 `database/update_article_detail_content.sql`，用于给已有数据库中的资讯补充更完整正文

如果你已经导入过旧数据库，可以在 MySQL 中执行：

```sql
source database/update_article_detail_content.sql;
```

## 资讯详情页修复说明

本版本修复了前台健康资讯详情页一直显示“正在加载资讯内容...”的问题：

- 后端 ArticleController 新增兼容接口：GET /api/article/detail/{id}
- 后端仍兼容旧接口：GET /api/article/{id}
- 前端 ArticleDetail.vue 新增 loading / error / empty content 状态
- 前端 article.js 自动 fallback，避免接口路径不一致导致页面卡住
