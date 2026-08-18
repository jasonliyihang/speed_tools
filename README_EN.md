# Speed Tools

<p align="center">
  <b>An all-in-one Android framework for local pluginization, dynamic skinning and font scaling</b>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Android-5.0%2B-brightgreen" alt="minSdk 21">
  <img src="https://img.shields.io/badge/compileSdk-35-blue" alt="compileSdk 35">
  <img src="https://img.shields.io/badge/AGP-8.8.2-orange" alt="AGP 8.8.2">
  <img src="https://img.shields.io/badge/AndroidX-Yes-success" alt="AndroidX">
</p>

<p align="center">
  <a href="README.md">中文文档</a> · <b>English</b>
</p>

> **Design goals**: low-intrusion integration, decoupling of multiple business modules, no dependency on Google Play dynamic delivery.
>
> **Integration**: source dependency is the recommended way now; the legacy Maven coordinate `com.liyihangjson:speed_tools:1.1.1` is no longer recommended.

---

## 📋 Table of Contents

- [Features](#-features)
- [Architecture](#-architecture)
- [Project Structure](#-project-structure)
- [Requirements](#-requirements)
- [Quick Start: Run the Demo in 10 Minutes](#-quick-start-run-the-demo-in-10-minutes)
- [Pluginization Guide](#-pluginization-guide)
- [Skinning & Font Scaling Guide](#-skinning--font-scaling-guide)
- [API Cheat Sheet](#-api-cheat-sheet)
- [FAQ & Troubleshooting](#-faq--troubleshooting)
- [Production Recommendations](#-production-recommendations)
- [Versions & Compatibility](#-versions--compatibility)
- [Related Documents](#-related-documents)

---

## ✨ Features

| Feature | Description | Typical use case |
|---|---|---|
| **Pluginization** | The host dynamically loads uninstalled APKs and launches plugin pages through a proxy | Independent evolution of business modules, plugin-level decoupling |
| **Dynamic skinning** | Load a skin-package APK at runtime to replace colors / images / backgrounds | Night mode, seasonal themes, brand customization |
| **Font scaling** | Adjust global font size at runtime with persisted user preference | Accessibility, large-font mode |

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                          Host APK                            │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐  │
│  │   Assets     │    │ SpeedUtils   │    │ Proxy Activity│  │
│  │ (plugin APK) │───▶│ (copy/load)  │───▶│ (lifecycle)   │  │
│  └──────────────┘    └──────────────┘    └──────────────┘  │
│         │                   │                       │       │
│         ▼                   ▼                       ▼       │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              SpeedApkManager (singleton)             │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  │   │
│  │  │ ClassLoader │  │  Resources  │  │ PackageInfo │  │   │
│  │  │(memory/file)│  │  (bridging) │  │ (metadata)  │  │   │
│  │  └─────────────┘  └─────────────┘  └─────────────┘  │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                  Plugin APK (not installed)                  │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐  │
│  │ Plugin page  │    │    res/      │    │AndroidManifest│ │
│  │(impl class)  │◀───│ (resources)  │    │(entry declare)│ │
│  └──────────────┘    └──────────────┘    └──────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

**Core flow**:
1. The plugin APK is placed in the host `assets/` directory (or an external download directory);
2. At runtime it is copied into the host's private directory (`assets` for development, download + verification for production);
3. **Class loading**: Android 8.0+ uses `InMemoryDexClassLoader` (in-memory loading, avoiding the Android 14+ file permission restriction); older versions fall back to `DexClassLoader`;
4. **Resource bridging**: an `AssetManager` is created via reflection to build the plugin `Resources` context;
5. **Proxy forwarding**: the host navigates through a unified API and a proxy `Activity` forwards lifecycle callbacks to the plugin implementation class.

---

## 📁 Project Structure

```
speed_tools/
├── lib_speed_tools/          # Core library (plugin loading, proxy, skinning, fonts)
├── module_host_main/         # Sample host app (loads and launches plugins)
├── module_client_one/        # Sample plugin 1
├── module_client_two/        # Sample plugin 2
├── theme_demo/               # Skinning and font-scaling demo app
├── black_theme/              # Sample skin package (resources only, no logic)
└── lib_img_utils/            # Third-party image library test module
```

| Module | Type | Purpose |
|---|---|---|
| `lib_speed_tools` | Library | Core capabilities (plugin loading, proxy, skinning, fonts) |
| `module_host_main` | App | Sample host (loads and launches plugins) |
| `module_client_one` | App | Sample plugin 1 |
| `module_client_two` | App | Sample plugin 2 |
| `theme_demo` | App | Skinning and font-scaling sample |
| `black_theme` | App | Sample skin package |
| `lib_img_utils` | Library | Third-party library test module |

---

## 🛠️ Requirements

- **JDK**: 17+
- **Android Studio**: latest stable (Koala or newer recommended)
- **compileSdk**: 35
- **minSdk**: 21 (Android 5.0)
- **targetSdk**: 35
- **AGP**: 8.8.2
- **Gradle**: 8.10+
- **Required**: `android.useAndroidX=true`

---

## 🚀 Quick Start: Run the Demo in 10 Minutes

> All steps below can be verified directly in this repository; no extra project is needed.

### Step 1: Build the plugin APKs

```bash
# Build the two sample plugins
./gradlew :module_client_one:assembleDebug
./gradlew :module_client_two:assembleDebug

# Build the skin package
./gradlew :black_theme:assembleDebug
```

Output paths:
- `module_client_one/build/outputs/apk/debug/module_client_one-debug.apk`
- `module_client_two/build/outputs/apk/debug/module_client_two-debug.apk`
- `black_theme/build/outputs/apk/debug/black_theme-debug.apk`

### Step 2: Place the plugins into the host

Copy the two plugin APKs built above into the host `assets` directory:

```
module_host_main/src/main/assets/
    ├── module_client_one-debug.apk
    └── module_client_two-debug.apk
```

Copy the skin-package APK into the `theme_demo` assets directory:

```
theme_demo/src/main/assets/
    └── black_theme-debug.apk
```

### Step 3: Run the host

Select the **`module_host_main`** run configuration in Android Studio and run it.

After launch the host loads the plugins in the background; once loading succeeds two buttons appear, each opening one plugin page.

### Step 4: Run the skinning demo

Select the **`theme_demo`** run configuration and run it.

Follow the on-screen steps: switch to the black theme → restore the default → enlarge the font → restore the font.

---

## 🔌 Pluginization Guide

### Overall flow

```
Host prepares the APK (assets / download)
        │
        ▼
Copy into the private directory (resolvePluginApk)
        │
        ▼
Load the plugin (SpeedApkManager.loadApk)
        │
        ▼
Create the plugin ClassLoader + Resources
        │
        ▼
Navigate to the plugin page (SpeedUtils.goActivity)
        │
        ▼
Proxy Activity forwards the lifecycle
```

### 1. Host dependency

**`settings.gradle`**
```gradle
include ':lib_speed_tools'
```

**Host `build.gradle`**
```gradle
dependencies {
    implementation project(':lib_speed_tools')
}
```

### 2. Loading a plugin from the host (recommended template)

```java
private static final String EXTERNAL_APK_DIR = "/sdcard/Download";
private static final String FIRST_PLUGIN_APK = "module_client_one-debug.apk";
private static final String FIRST_APK_KEY = "first_apk";

private boolean loadPlugin(String key, String assetFileName, String dexOutKey) {
    // Look into the external directory first, then fall back to copying from assets
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

**Parameters**:

| Parameter | Meaning | Recommendation |
|---|---|---|
| `key` | Unique plugin identifier | Name it per business domain, e.g. `biz_order_v1` |
| `apkPath` | Absolute APK path | Make sure it is readable |
| `dexOutKey` | Name of the dex optimization directory | Use a separate directory per plugin to avoid conflicts |

### 3. Navigating to a plugin page

```java
SpeedUtils.goActivity(this, "first_apk", null);
```

| Parameter | Description |
|---|---|
| `activity` | Host Activity |
| `"first_apk"` | The plugin key used when loading |
| `null` | Class tag; when empty the default entry is used (must be declared in the plugin manifest meta-data) |

### 4. Plugin project conventions

**Suggested structure**:
```
plugin module/
├── src/main/java/
│   └── PluginMainActivity.java      # Page implementation
│   └── PluginEntry.java             # Entry logic (called by the proxy)
├── src/main/res/                    # Plugin resources
└── src/main/AndroidManifest.xml     # Declares the entry meta-data
```

**Entry declaration example** (`AndroidManifest.xml`):
```xml
<application>
    <meta-data
        android:name="root_class"
        android:value="com.example.clientdome.ClientMainActivity" />
</application>
```

When the host navigates, pass `"root_class"` as `classTag`; the proxy layer resolves the target class through the `meta-data` entry.

### 5. Security recommendations (mandatory for production)

- [ ] Verify the plugin APK signature or SHA-256
- [ ] Verify plugin/host version compatibility
- [ ] Never load APKs from unknown sources
- [ ] Report loading failures through monitoring
- [ ] Load plugins on a background thread and update the UI on the main thread

---

## 🎨 Skinning & Font Scaling Guide

> This chapter can be used directly for integration into a business project without reading the demo.

### Core objects

| Class | Responsibility |
|---|---|
| `SPThemeManager` | Theme management (load skin package, switch, refresh) |
| `SPFontManager` | Font size management (scale value, persistence) |
| `SPUpdateUIListener` | Refresh callback interface |

### 1. Application initialization

```java
@Override
public void onCreate() {
    super.onCreate();
    SPFontManager.getInstance().init(this);
    SPThemeManager.getInstance().init(this);
}
```

⚠️ **Note**: without initialization the skinning/font APIs have no effect.

### 2. Activity base class integration

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
        // Refresh custom view state (e.g. update view colors manually)
    }
}
```

### 3. Resource naming rules (mandatory)

| Type | Prefix | Example |
|---|---|---|
| Color / background / image | `cxt_` | `@color/cxt_primary`, `@mipmap/cxt_logo` |
| Font dimension | `cxf_` | `@dimen/cxf_normal`, `@dimen/cxf_large` |

**Key rules**:
- Resource names in the main project and the skin package must be **exactly identical** (prefix and case included);
- The values may differ — that is exactly what skinning is about.

### 4. Referencing them in layouts

```xml
<TextView
    android:background="@color/cxt_primary"
    android:textSize="@dimen/cxf_normal"
    android:src="@mipmap/cxt_logo" />
```

### 5. The skin-package project

A skin package is a regular Android app module that **contains resources only, no business code**:

1. Create a new Android app module;
2. Put skinning resources with the **same names** as the main project into `res/`;
3. Build the APK, e.g. `black_theme-debug.apk`.

### 6. Applying a skin and restoring the default

```java
// Switch to the skin package
SPThemeManager.getInstance()
        .changeTheme("black_theme-debug.apk")
        .sendUpdateUIAction();

// Restore the default theme
SPThemeManager.getInstance()
        .changeTheme(SPThemeManager.DEFAULT_THEMES)
        .sendUpdateUIAction();
```

### 7. Changing the font size

```java
// Enlarge (enumerating the levels small/medium/large is recommended)
SPFontManager.getInstance().changeConfig(40).updateUI();

// Restore the default
SPFontManager.getInstance().changeConfig(0).updateUI();
```

### 8. Skinning troubleshooting checklist

| # | Check | Common mistake |
|---|---|---|
| 1 | Is the Application initialized? | Forgot to call `init()` |
| 2 | Does the Activity register/unregister the listener? | Missing in `onCreate` / `onDestroy` |
| 3 | Does the skin APK file name match? | Typo or wrong path |
| 4 | Are resource names exactly identical? | Wrong case or prefix |
| 5 | Was the refresh triggered? | Missing `sendUpdateUIAction()` |

---

## 📚 API Cheat Sheet

### Pluginization API

| API | Description |
|---|---|
| `SpeedApkManager.getInstance().loadApk(key, apkPath, dexOutPath, context)` | Load a plugin |
| `SpeedApkManager.getInstance().getHelper(key)` | Get the helper of a loaded plugin |
| `SpeedApkManager.getInstance().isLoaded(key)` | Check whether a plugin is loaded |
| `SpeedUtils.resolvePluginApk(ctx, externalDir, assetName)` | Resolve the plugin APK (external directory first, assets as fallback) |
| `SpeedUtils.copyAssetToCache(ctx, assetName, cacheDir)` | Copy an APK from assets into the private cache |
| `SpeedUtils.goActivity(activity, key, classTag)` | Navigate to a plugin page |
| `SpeedUtils.createResourcesFromApk(ctx, apkPath)` | Read the resources of an APK |

### Theme / font API

| API | Description |
|---|---|
| `SPThemeManager.getInstance().init(context)` | Initialize the theme system |
| `SPThemeManager.getInstance().changeTheme(apkName)` | Switch the skin package |
| `SPThemeManager.getInstance().sendUpdateUIAction()` | Trigger a global UI refresh |
| `SPThemeManager.getInstance().registerUpdateUI(activity)` | Register an Activity listener |
| `SPThemeManager.getInstance().unRegisterUpdateUI(activity)` | Unregister an Activity listener |
| `SPThemeManager.DEFAULT_THEMES` | Default theme constant |
| `SPFontManager.getInstance().init(context)` | Initialize the font system |
| `SPFontManager.getInstance().changeConfig(size)` | Change the font size offset |
| `SPFontManager.getInstance().updateUI()` | Trigger a global UI refresh |

---

## ❓ FAQ & Troubleshooting

### Plugin fails to load

**Symptom**: the button is visible but tapping does nothing / a toast reports a loading failure.

| Check | How |
|---|---|
| Does the APK exist? | Check whether the target file is in `assets/` or the external directory |
| Do the plugin keys match? | Compare the keys used in `loadApk()` and `goActivity()` |
| Is `dexOutPath` writable? | Make sure the private directory is writable |
| Class loading exception | Look for `ClassNotFoundException` in the logs |

### Blank page or crash

| Check | How |
|---|---|
| Entry class declaration | Check the `meta-data` in the plugin `AndroidManifest.xml` |
| Plugin resources | Make sure the plugin `res/` resources are complete |
| API version | Make sure plugin and host use the same `lib_speed_tools` version |

### Skinning has no effect

| Check | How |
|---|---|
| Refresh trigger | Make sure `sendUpdateUIAction()` was called |
| Resource prefix | Make sure the `cxt_` / `cxf_` prefixes are used |
| Skin package path | Make sure the APK is in an accessible path (assets or download directory) |

### Font does not change

| Check | How |
|---|---|
| Layout references | Make sure the layout uses `@dimen/cxf_*` |
| Listener registration | Make sure the Activity implements `SPUpdateUIListener` and registers it |
| Refresh call | Make sure `updateUI()` was called |

---

## 🏭 Production Recommendations

1. **Signature verification**: add plugin APK signature verification and an allowlist;
2. **Version matrix**: maintain a plugin/host compatibility matrix (`hostMin` / `hostMax`);
3. **Failure rollback**: add retry and rollback strategies for load failures;
4. **CI pipeline**: `assembleDebug + lint + unitTest`;
5. **Resource conventions**: agree on the `cxt_` / `cxf_` prefixes across the team;
6. **Thread safety**: plugin loading must run on a background thread and must not block the main thread.

---

## ⚠️ Versions & Compatibility

| Item | Version |
|---|---|
| compileSdk | 35 |
| minSdk | 21 (Android 5.0) |
| targetSdk | 35 |
| AGP | 8.8.2 |
| Kotlin (enforced) | 1.8.22 |
| Java | 17 |

**Android 14+ support**:
- Android 14 (API 34) forbids loading writable dex files;
- On Android 8.0+ this framework already switches to `InMemoryDexClassLoader` (in-memory loading), so no extra file permission handling is needed;
- Older versions (API 21–25) keep using `DexClassLoader`.

---

## 📎 Related Documents

| Document | Description |
|---|---|
| `PROJECT_OPTIMIZATION_REPORT.md` | Early optimization roadmap and code-hygiene suggestions |
| `THEME_MANAGER.md` | Legacy theme document (this README wins in case of conflict) |

---

> Issues and pull requests are welcome.
