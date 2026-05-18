# Speed Tools（本地插件化/换肤框架）

> 当前仓库定位：**源码工程 + 示例工程**，推荐以源码方式集成和调试，不再推荐旧的远程坐标 `implementation 'com.liyihangjson:speed_tools:1.1.1'`。

---

## 1. 项目简介

Speed Tools 是一个基于代理模式的 Android 插件化框架，核心能力包括：

- 动态加载外部/内置 APK（无需安装插件 APK）
- 宿主与插件间基础通信能力
- 换肤能力（主题包）
- 字体大小控制能力

当前示例代码以 Android Support 体系为基础（`compileSdkVersion 28`），用于演示插件化加载流程和主题能力。

---

## 2. 仓库架构（模块说明）

| 模块 | 类型 | 说明 |
|---|---|---|
| `lib_speed_tools` | Library | 插件化核心库（加载、代理、基础能力） |
| `module_host_main` | App | 宿主工程，负责加载并启动插件 APK |
| `module_client_one` | App | 示例插件 1 |
| `module_client_two` | App | 示例插件 2 |
| `theme_dome` | App | 换肤示例工程 |
| `black_theme` | App | 换肤皮肤包示例 |
| `lib_img_utils` | Library | 第三方图片能力测试库 |

---

## 3. 当前推荐集成方式（替代旧 Maven 坐标）

## 3.1 本地源码依赖（推荐）

在你的业务工程中：

1. 将 `lib_speed_tools` 拷贝/作为 git submodule 引入。
2. 在 `settings.gradle` 中加入：

```gradle
include ':lib_speed_tools'
```

3. 在宿主 app 的 `build.gradle` 中加入：

```gradle
dependencies {
    implementation project(':lib_speed_tools')
}
```

> 本仓库 `module_host_main` 已按这种方式引用。 

## 3.2 为什么不再使用 `com.liyihangjson:speed_tools:1.1.1`

- 该坐标版本较老，无法准确反映本仓库当前代码状态；
- 历史发布链路依赖已过时平台（如 Bintray/JCenter 生态）；
- 对早期项目建议直接使用源码依赖，便于定制和问题排查。

---

## 4. 快速启动（以本仓库示例为准）

## 4.1 插件包准备

宿主会从 `module_host_main/src/main/assets/` 读取插件 APK：

- `module_client_one-debug.apk`
- `module_client_two-debug.apk`

运行宿主前，请确保 assets 中插件包与加载代码中的文件名一致。

## 4.2 运行流程

1. 启动 `module_host_main`。
2. `HostMainActivity` 在后台线程读取 assets 中插件 APK 到私有目录。
3. 通过 `SpeedApkManager.loadApk(...)` 完成 Dex/资源加载。
4. 点击页面按钮进入对应插件首页（通过代理路由）。

---

## 5. 核心调用示例

## 5.1 宿主加载插件

```java
File pluginApk = SpeedUtils.getNativeApkPath(getApplicationContext(), "module_client_one-debug.apk");
SpeedApkManager.getInstance().loadApk(
        "first_apk",
        pluginApk.getAbsolutePath(),
        "dex_output2",
        this
);
```

## 5.2 宿主跳转插件页面

```java
SpeedUtils.goActivity(this, "first_apk", null);
```

---

## 6. 工程使用建议（面向早期项目）

- **先固化协议再扩展插件**：统一插件入口、路由参数、版本校验；
- **调试与发布分离**：debug 插件包不要直接复用到 release 流程；
- **加强异常与日志治理**：插件加载失败要有明确错误码与可视化提示；
- **建立最小 CI**：至少覆盖 `assembleDebug + lint + unitTest`；
- **逐步现代化升级**：后续可从 Support 迁移到 AndroidX，并升级 AGP/Gradle。

---

## 7. 已知现状

- 当前仓库保留历史 Gradle 配置（示例工程可运行优先）；
- 文档和代码会持续向“源码集成 + 可维护性”方向收敛；
- 若用于生产，请在安全、签名、资源更新策略上做进一步加固。

---

## 8. 相关文档

- `PROJECT_OPTIMIZATION_REPORT.md`：项目早期优化建议与落地路线。
- `THEME_MANAGER.md`：主题管理相关说明。
