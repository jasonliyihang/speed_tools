# Speed Tools 使用手册（插件化 + 换肤 + 字体大小）

> 目标：让你**不看 demo 工程**也能从 0 到 1 接入并跑通。  
> 说明：本项目当前推荐 **源码依赖接入**，不再推荐旧坐标 `implementation 'com.liyihangjson:speed_tools:1.1.1'`。

---

## 目录

- [1. 框架概览](#1-框架概览)
- [2. 能力边界与适用场景](#2-能力边界与适用场景)
- [3. 模块说明](#3-模块说明)
- [4. 快速开始（10 分钟跑通）](#4-快速开始10-分钟跑通)
- [5. 插件化接入手册（完整）](#5-插件化接入手册完整)
- [6. 换肤与字体切换手册（完整）](#6-换肤与字体切换手册完整)
- [7. 常见问题与排障手册](#7-常见问题与排障手册)
- [8. 生产落地建议](#8-生产落地建议)
- [9. API 速查](#9-api-速查)
- [10. 版本与兼容说明](#10-版本与兼容说明)

---

## 1. 框架概览

Speed Tools 是一套基于代理模式的 Android 本地插件化方案，主要能力：

1. 宿主动态加载插件 APK（插件无需安装）；
2. 宿主代理跳转插件页面；
3. 运行时主题切换（换肤）；
4. 运行时字体大小切换。

核心思路：

- 插件 APK 放在宿主 `assets`（或你自定义来源）；
- 运行时拷贝到宿主私有目录；
- 使用 DexClassLoader + 资源桥接完成类与资源加载；
- 通过统一代理接口转发生命周期。

---

## 2. 能力边界与适用场景

### 2.1 适用场景

- 多业务模块独立演进，希望按插件方式解耦；
- 需要“不安装皮肤包”的动态换肤；
- 需要低侵入接入现有宿主工程。

### 2.2 不适用场景

- 对 Google Play 动态交付有强依赖（建议评估 PAD/Feature Module）；
- 对严格安全审计要求极高但没有签名校验链路；
- 需要跨进程插件隔离（当前不是隔离进程架构）。

---

## 3. 模块说明

| 模块 | 类型 | 作用 |
|---|---|---|
| `lib_speed_tools` | Library | 核心能力（插件加载、代理、换肤、字体） |
| `module_host_main` | App | 宿主示例（加载并启动插件） |
| `module_client_one` | App | 插件示例 1 |
| `module_client_two` | App | 插件示例 2 |
| `theme_dome` | App | 换肤与字体切换示例 |
| `black_theme` | App | 皮肤包示例 |
| `lib_img_utils` | Library | 第三方库测试模块 |

---

## 4. 快速开始（10 分钟跑通）

> 你可以先按这个最短路径确认“框架可用”，再看后续完整手册。

### 步骤 1：引入核心库

在你的工程 `settings.gradle`：

```gradle
include ':lib_speed_tools'
```

在宿主 `app/build.gradle`：

```gradle
dependencies {
    implementation project(':lib_speed_tools')
}
```

### 步骤 2：准备插件 APK

把插件 APK 放到宿主 `src/main/assets/`，例如：

- `module_client_one-debug.apk`
- `module_client_two-debug.apk`

### 步骤 3：宿主启动时加载插件

```java
File apk = SpeedUtils.getNativeApkPath(getApplicationContext(), "module_client_one-debug.apk");
if (apk != null) {
    SpeedApkManager.getInstance().loadApk("first_apk", apk.getAbsolutePath(), "dex_output2", this);
}
```

### 步骤 4：点击按钮跳转插件

```java
SpeedUtils.goActivity(this, "first_apk", null);
```

到这里你已完成最小可运行链路。

---

## 5. 插件化接入手册（完整）

## 5.1 总体流程图（文字版）

1. 宿主准备插件 APK（assets/下载）；
2. 宿主将 APK 拷贝到私有目录；
3. 宿主调用 `SpeedApkManager.loadApk(...)`；
4. 代理层创建插件实例并建立资源上下文；
5. 宿主通过 `SpeedUtils.goActivity(...)` 进入插件；
6. 生命周期由代理 Activity 转发给插件实现类。

## 5.2 宿主工程接入要求

### 必须项

- 宿主依赖 `lib_speed_tools`；
- 有一个用于承载插件页面的 Activity（建议继承库中的宿主基类）；
- 有稳定的插件 key 命名策略（不可重复）；
- 插件加载路径可访问且可读。

### 推荐项

- 插件 key 按业务域命名：`biz_order_v1`；
- 每个插件独立 `dexOutPath`；
- 对加载失败进行 UI 提示 + 结构化日志记录。

## 5.3 插件 APK 准备规范

### 文件名规范

- 允许自定义，但必须与加载代码一致；
- 推荐格式：`{pluginId}-{buildType}.apk`。

### 放置方式

- 开发调试：放 `assets/`；
- 生产建议：下载到私有目录后再加载（并做签名/哈希校验）。

## 5.4 宿主加载实现（建议模板）

```java
private void loadPlugin(String key, String assetApkName, String dexOutPath) {
    File pluginApk = SpeedUtils.getNativeApkPath(getApplicationContext(), assetApkName);
    if (pluginApk == null) {
        // 1) 用户可见提示
        // 2) 技术日志：key、assetApkName、phase=copy
        return;
    }
    SpeedApkManager.getInstance().loadApk(key, pluginApk.getAbsolutePath(), dexOutPath, this);
}
```

## 5.5 页面跳转与入口策略

```java
SpeedUtils.goActivity(this, "first_apk", null);
```

参数说明：

- 参数 1：Context；
- 参数 2：插件 key（加载时同名）；
- 参数 3：类标识（可空，空时走默认入口）。

建议：

- 对外统一封装一层 Router，不要在业务层散落 key 字符串；
- key 与版本绑定，避免“同 key 不同包”导致错配。

## 5.6 插件实现规范（建议）

建议目录结构：

- `PluginMainActivity`：页面壳
- `PluginEntry`：插件入口逻辑（供代理调用）
- `res/`：插件资源

建议编码规范：

- 不要在入口实现里做重 I/O（避免阻塞首帧）；
- 插件内部资源访问统一封装；
- 对可失败流程（网络、文件）给降级方案。

## 5.7 生命周期与线程建议

- 插件加载在后台线程执行；
- UI 更新必须切回主线程；
- Activity 销毁时及时取消加载任务。

## 5.8 安全建议（强烈推荐）

- 校验插件 APK 的签名或 SHA-256；
- 校验插件版本与宿主兼容范围；
- 禁止加载未知来源 APK；
- 对加载异常做埋点告警。

---

## 6. 换肤与字体切换手册（完整）

> 本章节可直接用于业务项目接入，不依赖 demo 阅读。

## 6.1 核心对象

- `SPThemeManager`：主题管理（切换/刷新）
- `SPFontManager`：字体大小管理
- `SPUpdateUIListener`：刷新回调接口

## 6.2 初始化（Application）

在自定义 `Application` 中尽早执行：

```java
@Override
public void onCreate() {
    super.onCreate();
    SPFontManager.getInstance().init(this);
    SPThemeManager.getInstance().init(this);
}
```

注意：如果未初始化，后续换肤/字体 API 不生效。

## 6.3 Activity 基类接入

创建业务基类 `BaseActivity` 并注册监听：

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
        // 在这里处理局部刷新（如自定义控件状态）
    }
}
```

## 6.4 资源命名规则（必须遵守）

- 颜色/背景/图片等换肤资源前缀：`cxt_`
- 字体维度资源前缀：`cxf_`

示例：

- `@color/cxt_test`
- `@mipmap/cxt_server`
- `@dimen/cxf_normal`

规则要点：

- **主工程与皮肤包资源名必须一致**；
- 值可以不同（这正是换肤本质）。

## 6.5 主工程资源准备

确保主工程包含可替换资源：

- `colors.xml`：定义 `cxt_*`
- `dimens.xml`：定义 `cxf_*`
- `mipmap/drawable`：放置 `cxt_*` 对应图片

布局中按普通资源引用：

```xml
android:background="@color/cxt_test"
android:textSize="@dimen/cxf_normal"
android:src="@mipmap/cxt_server"
```

## 6.6 皮肤包工程准备（black_theme 同理）

皮肤包必须包含同名资源：

- `cxt_test`
- `cxt_server`
- `cxf_normal`

并打包生成 `black_theme-debug.apk`（或你自定义命名）。

## 6.7 放置皮肤 APK

调试方式：把皮肤 APK 放入主题宿主工程 `assets/`。  
生产方式：下载到本地目录并校验后加载。

## 6.8 触发换肤与恢复默认

```java
SPThemeManager.getInstance().changeTheme("black_theme-debug.apk").sendUpdateUIAction();
SPThemeManager.getInstance().changeTheme(SPThemeManager.DEFAULT_THEMES).sendUpdateUIAction();
```

- 第一行：切换到皮肤包；
- 第二行：恢复默认主题。

## 6.9 触发字体大小调整

```java
SPFontManager.getInstance().changeConfig(40).updateUI(); // 放大
SPFontManager.getInstance().changeConfig(0).updateUI();  // 恢复
```

建议：

- 将字号档位做枚举/配置化（小/中/大），避免魔法值；
- 字号变化后同步持久化用户设置。

## 6.10 换肤模块排障（逐条检查）

1. Application 是否已初始化 Theme/Font Manager；
2. Activity 是否 register/unregister 监听；
3. 皮肤 APK 文件名是否与 `changeTheme(...)` 一致；
4. 资源名是否完全一致（包括前缀和大小写）；
5. 是否调用 `sendUpdateUIAction()` 主动触发刷新。

---

## 7. 常见问题与排障手册

## 7.1 插件加载失败

现象：按钮可见但点击无跳转 / 提示加载失败。  
排查：

- assets 文件是否存在；
- 插件 key 是否一致；
- dexOutPath 是否可写；
- 日志里是否有 ClassNotFound。

## 7.2 页面空白或崩溃

现象：进入插件后白屏/崩溃。  
排查：

- 入口类声明是否正确；
- 插件资源是否齐全；
- 插件与宿主接口版本是否一致。

## 7.3 换肤无效果

现象：点击“切换主题”无变化。  
排查：

- 是否正确调用 `sendUpdateUIAction()`；
- 资源名是否满足 `cxt_` 规则；
- 皮肤包是否放到了可访问路径。

## 7.4 字体未变化

现象：调用 `changeConfig` 后无变化。  
排查：

- 布局是否使用了 `@dimen/cxf_*`；
- 页面是否实现并注册了更新监听；
- 是否调用 `updateUI()`。

---

## 8. 生产落地建议

1. 增加插件签名校验与白名单；
2. 加入插件版本兼容矩阵（hostMin/hostMax）；
3. 加入加载失败重试与回滚策略；
4. 建立 CI：`assembleDebug + lint + unitTest`；
5. 逐步迁移 AndroidX 与新 AGP。

---

## 9. API 速查

### 插件相关

- `SpeedApkManager.getInstance().loadApk(key, apkPath, dexOutPath, context)`：加载插件
- `SpeedApkManager.getInstance().getHelper(key)`：获取已加载插件 helper
- `SpeedUtils.getNativeApkPath(context, assetName)`：从 assets 拷贝插件到私有目录
- `SpeedUtils.goActivity(activity, key, classTag)`：跳转插件页面

### 主题/字体相关

- `SPThemeManager.getInstance().init(context)`：初始化主题系统
- `SPThemeManager.getInstance().changeTheme(apkName).sendUpdateUIAction()`：切换主题并刷新
- `SPFontManager.getInstance().init(context)`：初始化字体系统
- `SPFontManager.getInstance().changeConfig(size).updateUI()`：变更字号并刷新

---

## 10. 版本与兼容说明

- 当前仓库示例构建基线：Android Support + `compileSdkVersion 28`；
- 文档以当前仓库代码为准；
- 旧文档 `THEME_MANAGER.md` 作为历史参考，存在冲突时以本 README 为准。

---

## 附：相关文档

- `PROJECT_OPTIMIZATION_REPORT.md`：项目早期优化路线
- `THEME_MANAGER.md`：历史主题文档
