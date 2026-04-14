# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**gaisoft MES** — 基于若依框架（RuoYi）的制造执行系统，Spring Boot 2.5.15 多模块项目，Java 21，groupId `com.gaisoft`，version `3.8.9`。

## Build & Run

```bash
# 全量构建（跳过测试）
mvn clean package -DskipTests

# 仅构建某个模块（含依赖）
mvn clean package -pl gaisoft-admin -am -DskipTests

# 运行主应用
java -jar gaisoft-admin/target/gaisoft-admin.jar

# 安装所有模块到本地仓库（其他模块开发前必须先执行）
mvn clean install -DskipTests
```

## Module Architecture

依赖层次（从底到顶）：

```
gaisoft (parent pom)
├── gaisoft-common      ← 无内部依赖；JWT、Redis、POI、Security、PageHelper等工具
├── gaisoft-system      ← depends: common；SysUser/Role/Menu/Dept等系统Domain + Mapper + Service
├── gaisoft-framework   ← depends: system；Spring Security配置、AOP切面、Druid多数据源、Kaptcha验证码
├── gaisoft-generator   ← depends: common；代码生成（Velocity模板在 src/main/resources/vm/）
├── gaisoft-quartz      ← depends: common；Quartz定时任务
├── gaisoft-aftersales  ← depends: framework + common；售后/知识库(kb包)、维修(repair包)、AI(ai包)
└── gaisoft-admin       ← depends: framework + quartz + generator + aftersales；启动入口 + Web Controller
```

**主应用入口：** `gaisoft-admin/src/main/java/com/gaisoft/RuoYiApplication.java`

## Key Configuration

| 文件 | 说明 |
|------|------|
| `gaisoft-admin/src/main/resources/application.yml` | 主配置：端口8080、Redis、MyBatis、token |
| `gaisoft-admin/src/main/resources/application-druid.yml` | 数据库连接（MySQL `equipment_iqas`）|
| `gaisoft-admin/src/main/resources/mybatis/mybatis-config.xml` | MyBatis全局设置 |
| `gaisoft-generator/src/main/resources/generator.yml` | 代码生成配置 |

MyBatis mapperLocations 配置为 `classpath*:mapper/**/*Mapper.xml`，各模块的 Mapper XML 分别位于各自的 `src/main/resources/mapper/<module>/` 目录下。

## Package Conventions

- `com.gaisoft.common.*` — 跨模块共享的注解、常量、工具类、核心基类
- `com.gaisoft.system.*` — 系统管理（用户/角色/菜单/部门）的 domain/mapper/service
- `com.gaisoft.framework.*` — 框架配置（Security、AOP、多数据源切换、WebSocket）
- `com.gaisoft.generator.*` — 代码生成器逻辑
- `com.gaisoft.quartz.*` — 定时任务管理
- `com.gaisoft.kb.*` — 知识库业务（售后模块内）
- `com.gaisoft.repair.*` — 维修服务业务（售后模块内）
- `com.gaisoft.ai.*` — AI/Deepseek接口（售后模块内）
- `com.gaisoft.web.*` — HTTP入口层（Controller，仅在 gaisoft-admin）

## Runtime Dependencies

- **数据库：** MySQL，库名 `equipment_iqas`，默认连接 `ragflow-mysql:3306`
- **Redis：** 默认 `ragflow-redis:6379`，database=8，密码 `infini_rag_flow`
- **文件上传路径：** `/ragflow/uploadPath`（可在 application.yml 的 `ruoyi.profile` 修改）
