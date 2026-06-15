# 我的第一个Java前端

全栈项目 - Vue 3 前端 + Spring Boot 后端

## 项目结构

```
vue-project1/
├── frontend/               Vue 3 前端项目
│   ├── src/                源代码
│   │   ├── api/            API 接口
│   │   ├── views/          页面组件
│   │   ├── router/         路由配置
│   │   ├── components/     公共组件
│   │   └── utils/          工具函数
│   ├── public/             静态资源
│   ├── index.html          入口 HTML
│   ├── vite.config.js      Vite 配置
│   └── package.json        依赖配置
└── backend/                Spring Boot 后端
    └── system-web-01/      Java 后端项目
        ├── src/main/java/   Java 源代码
        ├── src/main/resources/ 配置文件
        └── pom.xml         Maven 配置
```

## 技术栈

### 前端
- Vue 3 + Vite
- Element Plus UI 组件库
- ECharts 图表
- Vue Router 路由
- Axios HTTP 客户端

### 后端
- Spring Boot
- MyBatis
- MySQL 数据库
- JWT 认证
- 阿里云 OSS 对象存储

## 快速开始

### 前端

```sh
npm install
npm run dev
```

### 后端

使用 IntelliJ IDEA 打开 `backend/system-web-01` 目录，运行 `SystemWeb01Application` 启动类。

## 环境配置

后端需要配置以下环境变量：
- `DB_USERNAME` - 数据库用户名
- `DB_PASSWORD` - 数据库密码
- `JWT_SIGN_KEY` - JWT 签名密钥（至少32字符）
- `OSS_ACCESS_KEY_ID` - 阿里云 OSS Access Key
- `OSS_ACCESS_KEY_SECRET` - 阿里云 OSS Secret Key
