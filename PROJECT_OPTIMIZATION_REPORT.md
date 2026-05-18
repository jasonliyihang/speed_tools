# 项目早期阶段优化建议（Speed Tools）

## 1) 构建与依赖体系（优先级：P0）

- **升级构建基础设施**：当前仍使用 `com.android.tools.build:gradle:3.1.3`，仓库包含 `jcenter()`，这是历史配置，已不适合作为长期维护方案。建议迁移到较新的 Android Gradle Plugin + Gradle Wrapper 组合，并移除 `jcenter()` 依赖。  
- **统一版本声明方式**：当前在根 `build.gradle` 的 `ext` 中集中定义版本，但模块里仍有硬编码版本（如 `appcompat-v7:28.0.0`）。建议迁移到 `libs.versions.toml`（Version Catalog）统一管理，减少漂移。  
- **开启构建性能参数**：`gradle.properties` 中并行构建、JVM 参数基本都注释掉了。建议在 CI/本地评估后开启缓存、并行、增量设置。

**收益**：降低构建失败风险、提升编译速度、减少依赖不可用问题。

---

## 2) 安全与发布规范（优先级：P0）

- **移除仓库中的签名文件**：根目录存在 `keys.store.jks`，属于高风险资产，不应进入版本库。建议立即轮换密钥并删除历史暴露文件（必要时重写历史）。  
- **插件 APK 打包资产策略**：`module_host_main/src/main/assets/` 直接放置 debug apk（`module_client_one-debug.apk`、`module_client_two-debug.apk`），不适合生产。建议区分 debug/release 资产渠道，避免调试包进入正式发布链路。

**收益**：减少供应链与签名泄露风险，提升发布合规性。

---

## 3) 运行时稳定性（优先级：P1）

- **线程与生命周期安全**：`HostMainActivity` 手动 `new Thread(this).start()` + 非静态 `Handler`，在 Activity 销毁场景可能导致泄露或状态错乱。建议替换为 `Executor` + 主线程 `Handler(Looper.getMainLooper())` 或 Lifecycle 感知方案（如 ViewModel + 协程）。  
- **空指针防护**：`SpeedUtils.getNativeApkPath()` 失败会返回 `null`，但 `HostMainActivity.run()` 直接调用 `getAbsolutePath()`，存在 NPE 风险。建议统一错误返回与 fail-fast 日志。  
- **反射与类加载异常治理**：`SpeedUtils` 中大量 `catch (Exception) + printStackTrace`，建议替换为统一异常模型与结构化日志，并对关键路径（loadApk、readDexFile）增加错误码和上报点。

**收益**：减少线上崩溃，提高问题可定位性。

---

## 4) 插件框架架构层优化（优先级：P1）

- **单例线程安全细节完善**：`SpeedApkManager` 双重检查锁缺少 `volatile`，理论上有指令重排风险。建议 `private static volatile SpeedApkManager instance`。  
- **接口命名与职责收敛**：目前 `*Interface` / `*InterfaceImp` 命名偏旧，且职责切分较碎，建议按“加载、资源、路由、主题”分层并通过 facade 暴露能力。  
- **API 语义清晰化**：如 `getOutDexpaPath` 存在拼写问题，影响可读性；建议统一命名规范并通过 deprecate 兼容旧 API。

**收益**：降低维护成本，便于后续功能扩展（热修复、增量包、多插件并发）。

---

## 5) 测试与质量门禁（优先级：P1）

- **测试样例目前偏模板化**：多个模块仍是默认 `ExampleUnitTest`/`ApplicationTest`。建议先补三类核心测试：  
  1. 插件加载成功/失败路径；  
  2. 资源查找与主题切换一致性；  
  3. 关键反射调用兼容性（不同 API level）。
- **引入静态检查**：新增 `lint`、`detekt`/`checkstyle`（Java 可用 SpotBugs + Checkstyle），并配置 CI 阻断规则。  
- **最小 CI 流水线**：PR 必跑 `assembleDebug + lint + unitTest`。

**收益**：在项目早期建立“可回归能力”，避免后期重构代价。

---

## 6) 工程整洁度（优先级：P2）

- **示例模块命名统一**：`theme_dome`、`clientdome` 等拼写不一致，建议统一（如 `theme_demo`, `client_demo`）。  
- **文档版本信息老化**：README 标注 API 28 和旧发布方式，建议新增“当前维护状态/兼容矩阵/快速开始（2026 版）”。  
- **历史注释清理**：大量注释掉的 Gradle 配置（签名、发布脚本）建议迁移到文档，减少主干噪音。

---

## 建议的两周落地路线图

### Week 1（先稳住）
1. 清理密钥与 debug 资产发布风险。  
2. 建立最小 CI（assemble/lint/unitTest）。  
3. 修复 `HostMainActivity` 线程与 NPE 风险。

### Week 2（再提速）
1. 升级 AGP/Gradle 与仓库配置，去除 `jcenter`。  
2. 建立版本目录（Version Catalog）。  
3. 补齐插件加载/资源/主题三类回归测试。

---

## 可量化验收指标（建议）

- Debug 全量构建耗时下降 **20%+**。  
- 插件加载关键路径崩溃率下降至 **<0.1%**。  
- PR 自动化检查覆盖率达到 **100%**（每次提交都跑）。  
- 关键模块（`lib_speed_tools`）单测数从模板测试提升到具备真实断言的核心用例。
