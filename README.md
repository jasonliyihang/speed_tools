# Speed Tools

<p align="center">
  <b>Android 本地插件化 + 动态换肤 + 字体切换 一体化框架</b>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Android-5.0%2B-brightgreen" alt="minSdk 21">
  <img src="https://img.shields.io/badge/compileSdk-35-blue" alt="compileSdk 35">
  <img src="https://img.shields.io/badge/AGP-8.8.2-orange" alt="AGP 8.8.2">
  <img src="https://img.shields.io/badge/AndroidX-Yes-success" alt="AndroidX">
</p>

> **设计目标**：低侵入接入、多业务解耦、避免 Google Play 动态交付依赖。
> 
> **接入方式**：当前推荐 **源码依赖**，不再推荐旧 Maven 坐标 `com.liyihangjson:speed_tools:1.1.1`。

---

## 📋 目录

- [特性概览](#-特性概览)
- [架构原理](#-架构原理)
- [工程结构](#-工程结构)
- [环境要求](#-环境要求)
- [快速开始：10 分钟跑通 Demo](#-快速开始10-分钟跑通-demo)
- [插件化接入手册](#-插件化接入手册)
- [换肤与字体切换手册](#-换肤与字体切换手册)
- [API 速查表](#-api-速查表)
- [常见问题与排障](#-常见问题与排障)
- [生产落地建议](#-生产落地建议)
- [版本与兼容说明](#-版本与兼容说明)
- [相关文档](#-相关文档)

---

## ✨ 特性概览

| 特性 | 说明 | 典型场景 |
|---|---|---|
| **插件化** | 宿主动态加载未安装的 APK，代理启动插件页面 | 多业务模块独立演进、按插件解耦 |
| **动态换肤** | 运行时加载皮肤包 APK，替换颜色/图片/背景资源 | 夜间模式、节日主题、品牌定制 |
| **字体调节** | 运行时全局调整字体大小，支持用户偏好持久化 | 无障碍适配、老年模式 |

---

## 🏗️ 架构原理

```
┌─────────────────────────────────────────────────────────────┐
│                         宿主 APK                              │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐  │
│  │   Assets     │    │ SpeedUtils   │    │ 代理 Activity │  │
│  │  (插件APK)   │───▶│  (拷贝/加载) │───▶│(生命周期转发)│  │
│  └──────────────┘    └──────────────┘    └──────────────┘  │
│         │                   │                       │       │
│         ▼                   ▼                       ▼       │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              SpeedApkManager (单例)                  │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  │   │
│  │  │ 类加载器     │  │  Resources  │  │ PackageInfo │  │   │
│  │  │(内存/文件)  │  │ (资源桥接)  │  │ (元数据)    │  │   │
│  │  └─────────────┘  └─────────────┘  └─────────────┘  │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                       插件 APK (未安装)                       │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐  │
│  │ Plugin页面   │    │    res/      │    │ AndroidManifest│ │
│  │ (实现类)     │◀───│  (资源文件)  │    │  (入口声明)   │  │
│  └──────────────┘    └──────────────┘    └──────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

**核心流程**：
1. 插件 APK 置于宿主 `assets/`（或外部下载目录）；
2. 运行时拷贝到宿主私有目录（开发调试用 `assets`，生产建议下载+校验）；
3. **类加载**：Android 8.0+ 使用 `InMemoryDexClassLoader`（内存加载，规避 Android 14+ 文件权限限制），低版本回退 `DexClassLoader`；
4. **资源桥接**：反射创建 `AssetManager`，建立插件 `Resources` 上下文；
5. **代理转发**：宿主通过统一接口跳转，由代理 `Activity` 将生命周期转发给插件实现类。

---

## 📁 工程结构

```
speed_tools/
├── lib_speed_tools/          # 核心库（插件加载、代理、换肤、字体）
├── module_host_main/         # 宿主示例 App（加载并启动插件）
├── module_client_one/        # 插件示例 1
├── module_client_two/        # 插件示例 2
├── theme_demo/               # 换肤与字体切换演示 App
├── black_theme/              # 皮肤包示例（仅含资源，无代码逻辑）
└── lib_img_utils/            # 第三方图片库测试模块
```

| 模块 | 类型 | 作用 |
|---|---|---|
| `lib_speed_tools` | Library | 核心能力（插件加载、代理、换肤、字体） |
| `module_host_main` | App | 宿主示例（加载并启动插件） |
| `module_client_one` | App | 插件示例 1 |
| `module_client_two` | App | 插件示例 2 |
| `theme_demo` | App | 换肤与字体切换示例 |
| `black_theme` | App | 皮肤包示例 |
| `lib_img_utils` | Library | 第三方库测试模块 |

---

## 🛠️ 环境要求

- **JDK**：17+
- **Android Studio**：最新稳定版（推荐 Koala 及以上）
- **compileSdk**：35
- **minSdk**：21（Android 5.0）
- **targetSdk**：35
- **AGP**：8.8.2
- **Gradle**：8.10+
- **必须启用**：`android.useAndroidX=true`

---

## 🚀 快速开始：10 分钟跑通 Demo

> 以下步骤可直接在本仓库验证，无需额外新建工程。

### Step 1：编译插件 APK

```bash
# 编译两个插件示例
./gradlew :module_client_one:assembleDebug
./gradlew :module_client_two:assembleDebug

# 编译皮肤包
./gradlew :black_theme:assembleDebug
```

编译产物路径：
- `module_client_one/build/outputs/apk/debug/module_client_one-debug.apk`
- `module_client_two/build/outputs/apk/debug/module_client_two-debug.apk`
- `black_theme/build/outputs/apk/debug/black_theme-debug.apk`

### Step 2：放置插件到宿主

将上面编译出的两个插件 APK 复制到宿主 assets 目录：

```
module_host_main/src/main/assets/
    ├── module_client_one-debug.apk
    └── module_client_two-debug.apk
```

将皮肤包 APK 复制到 theme_demo assets 目录：

```
theme_demo/src/main/assets/
    └── black_theme-debug.apk
```

### Step 3：运行宿主

在 Android Studio 中选择运行配置 **`module_host_main`**，点击运行。

启动后宿主会自动在后台加载插件，加载成功后界面会显示两个按钮，分别点击进入两个插件页面。

### Step 4：运行换肤演示

选择运行配置 **`theme_demo`**，点击运行。

按界面提示依次体验：切换黑色主题 → 恢复默认 → 放大字体 → 恢复字体。

---

## 🔌 插件化接入手册

### 总体流程

```
宿主准备 APK (assets / 下载)
        │
        ▼
拷贝到私有目录（resolvePluginApk）
        │
        ▼
加载插件（SpeedApkManager.loadApk）
        │
        ▼
创建插件 ClassLoader + Resources
        │
        ▼
跳转插件页面（SpeedUtils.goActivity）
        │
        ▼
代理 Activity 转发生命周期
```

### 1. 宿主依赖

**`settings.gradle`**
```gradle
include ':lib_speed_tools'
```

**宿主 `build.gradle`**
```gradle
dependencies {
    implementation project(':lib_speed_tools')
}
```

### 2. 宿主加载插件（推荐模板）

```java
private static final String EXTERNAL_APK_DIR = "/sdcard/Download";
private static final String FIRST_PLUGIN_APK = "module_client_one-debug.apk";
private static final String FIRST_APK_KEY = "first_apk";

private boolean loadPlugin(String key, String assetFileName, String dexOutKey) {
    // 优先从外部目录查找，fallback 到 assets 拷贝
    File apkFile = SpeedUtils.resolvePluginApk(
            getApplicationContext(), EXTERNAL_APK_DIR, assetFileName);
    if (apkFile == null) {
        Log.e(TAG, "Plugin APK not found: " + assetFileName);
        return false;
    }
    return SpeedApkManager.getInstance().loadApk(
            key, apkFile.getAbsolutePath(), dexOutKey, getApplicationContext());
}
```

**参数说明**：

| 参数 | 含义 | 建议 |
|---|---|---|
| `key` | 插件唯一标识 | 按业务域命名，如 `biz_order_v1` |
| `apkPath` | APK 绝对路径 | 确保可读 |
| `dexOutKey` | dex 优化目录名 | 每个插件独立目录，避免冲突 |

### 3. 跳转插件页面

```java
SpeedUtils.goActivity(this, "first_apk", null);
```

| 参数 | 说明 |
|---|---|
| `activity` | 宿主 Activity |
| `"first_apk"` | 加载时的插件 key |
| `null` | 类标识，为空时走默认入口（需在插件 Manifest meta-data 中声明） |

### 4. 插件工程规范

**目录结构建议**：
```
插件模块/
├── src/main/java/
│   └── PluginMainActivity.java      # 页面实现
│   └── PluginEntry.java             # 入口逻辑（供代理调用）
├── src/main/res/                    # 插件资源
└── src/main/AndroidManifest.xml     # 声明入口 meta-data
```

**入口声明示例**（`AndroidManifest.xml`）：
```xml
<application>
    <meta-data
        android:name="root_class"
        android:value="com.example.clientdome.ClientMainActivity" />
</application>
```

宿主跳转时 `classTag` 传 `"root_class"`，代理层会通过 `meta-data` 解析到目标类。

### 5. 安全建议（生产必做）

- [ ] 校验插件 APK 签名或 SHA-256
- [ ] 校验插件版本与宿主兼容范围
- [ ] 禁止加载未知来源 APK
- [ ] 对加载异常做埋点告警
- [ ] 插件加载在后台线程执行，UI 更新切回主线程

---

## 🎨 换肤与字体切换手册

> 本章节可直接用于业务项目接入，不依赖 Demo 阅读。

### 核心对象

| 类 | 职责 |
|---|---|
| `SPThemeManager` | 主题管理（加载皮肤包、切换、刷新） |
| `SPFontManager` | 字体大小管理（缩放值、持久化） |
| `SPUpdateUIListener` | 刷新回调接口 |

### 1. Application 初始化

```java
@Override
public void onCreate() {
    super.onCreate();
    SPFontManager.getInstance().init(this);
    SPThemeManager.getInstance().init(this);
}
```

⚠️ **注意**：未初始化则后续换肤/字体 API 不生效。

### 2. Activity 基类接入

```java
public class BaseActivity extends AppCompatActivity implements SPUpdateUIListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SPThemeManager.getInstance().registerUpdateUI(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        SPThemeManager.getInstance().unRegisterUpdateUI(this);
        super.onDestroy();
    }

    @Override
    public void updateUI(boolean isFistLoading) {
        // 自定义控件状态刷新（如手动更新 View 颜色）
    }
}
```

### 3. 资源命名规则（必须遵守）

| 类型 | 前缀 | 示例 |
|---|---|---|
| 颜色/背景/图片 | `cxt_` | `@color/cxt_primary`、`@mipmap/cxt_logo` |
| 字体维度 | `cxf_` | `@dimen/cxf_normal`、`@dimen/cxf_large` |

**规则要点**：
- 主工程与皮肤包资源名必须**完全一致**（包括前缀和大小写）；
- 值可以不同（这正是换肤本质）。

### 4. 布局中引用

```xml
<TextView
    android:background="@color/cxt_primary"
    android:textSize="@dimen/cxf_normal"
    android:src="@mipmap/cxt_logo" />
```

### 5. 皮肤包工程

皮肤包是一个**只包含资源、不含业务代码**的普通 Android App 模块：

1. 新建 Android App 模块；
2. 在 `res/` 中放置与主工程**同名**的换肤资源；
3. 打包生成 APK，如 `black_theme-debug.apk`。

### 6. 触发换肤与恢复默认

```java
// 切换到皮肤包
SPThemeManager.getInstance()
        .changeTheme("black_theme-debug.apk")
        .sendUpdateUIAction();

// 恢复默认主题
SPThemeManager.getInstance()
        .changeTheme(SPThemeManager.DEFAULT_THEMES)
        .sendUpdateUIAction();
```

### 7. 触发字体大小调整

```java
// 放大（推荐将档位枚举化：小/中/大）
SPFontManager.getInstance().changeConfig(40).updateUI();

// 恢复默认
SPFontManager.getInstance().changeConfig(0).updateUI();
```

### 8. 换肤排障清单

| # | 检查项 | 常见错误 |
|---|---|---|
| 1 | Application 是否已初始化 | 忘记调用 `init()` |
| 2 | Activity 是否注册/注销监听 | `onCreate`/`onDestroy` 漏写 |
| 3 | 皮肤 APK 文件名是否一致 | 拼写错误、路径不对 |
| 4 | 资源名是否完全一致 | 大小写、前缀错误 |
| 5 | 是否主动触发刷新 | 漏调 `sendUpdateUIAction()` |

---

## 📚 API 速查表

### 插件化 API

| API | 说明 |
|---|---|
| `SpeedApkManager.getInstance().loadApk(key, apkPath, dexOutPath, context)` | 加载插件 |
| `SpeedApkManager.getInstance().getHelper(key)` | 获取已加载插件 helper |
| `SpeedApkManager.getInstance().isLoaded(key)` | 判断插件是否已加载 |
| `SpeedUtils.resolvePluginApk(ctx, externalDir, assetName)` | 解析插件 APK（外部目录优先，fallback assets） |
| `SpeedUtils.copyAssetToCache(ctx, assetName, cacheDir)` | 从 assets 拷贝 APK 到私有缓存 |
| `SpeedUtils.goActivity(activity, key, classTag)` | 跳转插件页面 |
| `SpeedUtils.createResourcesFromApk(ctx, apkPath)` | 读取 APK 资源 |

### 主题/字体 API

| API | 说明 |
|---|---|
| `SPThemeManager.getInstance().init(context)` | 初始化主题系统 |
| `SPThemeManager.getInstance().changeTheme(apkName)` | 切换皮肤包 |
| `SPThemeManager.getInstance().sendUpdateUIAction()` | 触发全局 UI 刷新 |
| `SPThemeManager.getInstance().registerUpdateUI(activity)` | Activity 注册监听 |
| `SPThemeManager.getInstance().unRegisterUpdateUI(activity)` | Activity 注销监听 |
| `SPThemeManager.DEFAULT_THEMES` | 默认主题常量 |
| `SPFontManager.getInstance().init(context)` | 初始化字体系统 |
| `SPFontManager.getInstance().changeConfig(size)` | 变更字号偏移量 |
| `SPFontManager.getInstance().updateUI()` | 触发全局 UI 刷新 |

---

## ❓ 常见问题与排障

### 插件加载失败

**现象**：按钮可见但点击无跳转 / Toast 提示加载失败。

| 排查项 | 方法 |
|---|---|
| APK 是否存在 | 检查 `assets/` 或外部目录是否有目标文件 |
| 插件 key 是否一致 | 对比 `loadApk()` 和 `goActivity()` 的 key |
| dexOutPath 是否可写 | 确保私有目录有写入权限 |
| 类加载异常 | 查看日志是否有 `ClassNotFoundException` |

### 页面空白或崩溃

| 排查项 | 方法 |
|---|---|
| 入口类声明 | 检查插件 `AndroidManifest.xml` 的 `meta-data` |
| 插件资源 | 确认插件 `res/` 资源齐全 |
| 接口版本 | 确认插件与宿主使用的 `lib_speed_tools` 版本一致 |

### 换肤无效果

| 排查项 | 方法 |
|---|---|
| 刷新触发 | 确认调用了 `sendUpdateUIAction()` |
| 资源前缀 | 确认使用 `cxt_` / `cxf_` 前缀 |
| 皮肤包路径 | 确认 APK 放入了可访问路径（assets 或下载目录） |

### 字体未变化

| 排查项 | 方法 |
|---|---|
| 布局引用 | 确认布局使用了 `@dimen/cxf_*` |
| 监听注册 | 确认 Activity 实现了 `SPUpdateUIListener` 并注册 |
| 刷新调用 | 确认调用了 `updateUI()` |

---

## 🏭 生产落地建议

1. **签名校验**：增加插件 APK 签名校验与白名单机制；
2. **版本矩阵**：建立插件与宿主的版本兼容矩阵（`hostMin` / `hostMax`）；
3. **失败回滚**：加入加载失败重试与回滚策略；
4. **CI 流水线**：`assembleDebug + lint + unitTest`；
5. **资源规范**：团队内统一 `cxt_` / `cxf_` 资源前缀规范；
6. **线程安全**：插件加载必须在后台线程，禁止阻塞主线程。

---

## ⚠️ 版本与兼容说明

| 项目 | 版本 |
|---|---|
| compileSdk | 35 |
| minSdk | 21（Android 5.0） |
| targetSdk | 35 |
| AGP | 8.8.2 |
| Kotlin（强制） | 1.8.22 |
| Java | 17 |

**Android 14+ 适配**：
- Android 14（API 34）禁止加载可写的 dex 文件；
- 本框架在 Android 8.0+ 已自动切换为 `InMemoryDexClassLoader`（内存加载），无需额外处理文件权限；
- 低版本（API 21~25）继续使用 `DexClassLoader`。

---

## 📎 相关文档

| 文档 | 说明 |
|---|---|
| `PROJECT_OPTIMIZATION_REPORT.md` | 项目早期优化路线与工程整洁度建议 |
| `THEME_MANAGER.md` | 历史主题文档（存在冲突时以本 README 为准） |

---

> 如有问题或建议，欢迎提交 Issue 或 PR。
