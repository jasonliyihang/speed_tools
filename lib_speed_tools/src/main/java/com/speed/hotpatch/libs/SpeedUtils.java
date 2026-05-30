package com.speed.hotpatch.libs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import android.os.Build;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import dalvik.system.DexClassLoader;
import dalvik.system.InMemoryDexClassLoader;

/**
 * Utilities for plugin APK I/O, package parsing, and class loading.
 */
public final class SpeedUtils {

    public static final String TAG = "SpeedUtils";
    private static final int COPY_BUFFER_SIZE = 8192;

    private SpeedUtils() {
    }

    public static void msg(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("cx_config", Context.MODE_PRIVATE);
    }

    public static String getNameByRid(Context context, int rid) {
        try {
            return context.getResources().getResourceEntryName(rid);
        } catch (Exception e) {
            Log.w(TAG, "getNameByRid failed for rid=" + rid, e);
            return null;
        }
    }

    public static void goActivity(Activity activity, String apkName, String className) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(SpeedConfig.ACTIVITY_URL));
        intent.setPackage(activity.getPackageName());
        intent.putExtra(SpeedConfig.APK_NAME, apkName);
        intent.putExtra(SpeedConfig.CLASS_TAG, className);
        activity.startActivity(intent);
    }

    /**
     * Copies an asset APK into app-private storage and returns the cached file.
     */
    public static File copyAssetToCache(Context context, String assetName, String cacheDirName) {
        if (context == null || assetName == null) {
            return null;
        }
        File cacheDir = context.getDir(cacheDirName, Context.MODE_PRIVATE);
        File outFile = new File(cacheDir, assetName);
        try (InputStream in = context.getAssets().open(assetName);
             FileOutputStream out = new FileOutputStream(outFile)) {
            byte[] buffer = new byte[COPY_BUFFER_SIZE];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            out.getFD().sync();
            return outFile;
        } catch (Exception e) {
            Log.e(TAG, "copyAssetToCache failed asset=" + assetName, e);
            return null;
        }
    }

    /** @deprecated Use {@link #copyAssetToCache(Context, String, String)}. */
    @Deprecated
    public static File getNativeApkPath(Context context, String name) {
        return copyAssetToCache(context, name, "my_cache");
    }

    public static File getNativeApkPathByDir(String dirPath, String apkFileName) {
        if (dirPath == null || apkFileName == null) {
            return null;
        }
        File apkFile = new File(dirPath, apkFileName);
        if (!apkFile.isFile()) {
            Log.w(TAG, "APK not found: " + apkFile.getAbsolutePath());
            return null;
        }
        return apkFile;
    }

    public static File getNativeApkPathByAbsolutePath(String apkFilePath) {
        if (apkFilePath == null) {
            return null;
        }
        File apkFile = new File(apkFilePath);
        if (!apkFile.isFile()) {
            Log.w(TAG, "APK not found: " + apkFilePath);
            return null;
        }
        return apkFile;
    }

    /**
     * Resolves a plugin APK: external dir first, then assets cache.
     */
    public static File resolvePluginApk(Context context, String externalDir, String assetFileName) {
        File external = getNativeApkPathByDir(externalDir, assetFileName);
        if (external != null) {
            return external;
        }
        return copyAssetToCache(context, assetFileName, "my_cache");
    }

    public static Resources readApkRes(Context context, String apkPath) {
        return createResourcesFromApk(context, apkPath);
    }

    public static Resources createResourcesFromApk(Context context, String apkPath) {
        if (context == null || apkPath == null) {
            return null;
        }
        try {
            AssetManager assetManager = createAssetManager(apkPath);
            if (assetManager == null) {
                return null;
            }
            Resources hostRes = context.getResources();
            return new Resources(assetManager, hostRes.getDisplayMetrics(), hostRes.getConfiguration());
        } catch (Exception e) {
            Log.e(TAG, "createResourcesFromApk failed: " + apkPath, e);
            return null;
        }
    }

    public static AssetManager createAssetManager(String apkPath) {
        try {
            AssetManager assetManager = AssetManager.class.getDeclaredConstructor().newInstance();
            Method addAssetPath = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, apkPath);
            return assetManager;
        } catch (Exception e) {
            Log.e(TAG, "createAssetManager failed: " + apkPath, e);
            return null;
        }
    }

    public static String getOutDexPath(Context context, String dexDirKey) {
        return context.getDir(dexDirKey, Context.MODE_PRIVATE).getAbsolutePath();
    }

    /** @deprecated Typo alias; use {@link #getOutDexPath(Context, String)}. */
    @Deprecated
    public static String getOutDexpaPath(Context context, String apkPath) {
        return getOutDexPath(context, apkPath);
    }

    public static PackageInfo getPackageInfo(Context context, String apkFilepath) {
        if (context == null || apkFilepath == null) {
            return null;
        }
        try {
            PackageInfo pkgInfo = context.getPackageManager().getPackageArchiveInfo(
                    apkFilepath,
                    PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES | PackageManager.GET_META_DATA);
            applyArchiveSourceDirs(pkgInfo, apkFilepath);
            return pkgInfo;
        } catch (Exception e) {
            Log.e(TAG, "getPackageInfo failed: " + apkFilepath, e);
            return null;
        }
    }

    static void applyArchiveSourceDirs(PackageInfo pkgInfo, String apkFilepath) {
        if (pkgInfo != null && pkgInfo.applicationInfo != null) {
            pkgInfo.applicationInfo.sourceDir = apkFilepath;
            pkgInfo.applicationInfo.publicSourceDir = apkFilepath;
        }
    }

    public static PackageInfo getPackageInfo2(Context context, String packageName) {
        try {
            return context.getPackageManager().getPackageInfo(
                    packageName,
                    PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES | PackageManager.GET_META_DATA);
        } catch (Exception e) {
            Log.e(TAG, "getPackageInfo2 failed: " + packageName, e);
            return null;
        }
    }

    public static Drawable getAppIcon(Context context, String apkFilepath) {
        PackageInfo pkgInfo = getPackageInfo(context, apkFilepath);
        if (pkgInfo == null) {
            return null;
        }
        return context.getPackageManager().getApplicationIcon(pkgInfo.applicationInfo);
    }

    public static CharSequence getAppLabel(Context context, String apkFilepath) {
        PackageInfo pkgInfo = getPackageInfo(context, apkFilepath);
        if (pkgInfo == null) {
            return null;
        }
        return context.getPackageManager().getApplicationLabel(pkgInfo.applicationInfo);
    }

    /**
     * Creates a ClassLoader for the given APK.
     * <p>On Android 8.0+ (API 26) uses {@link InMemoryDexClassLoader} to avoid
     * Android 14+ restrictions on loading writable dex files. On older devices
     * falls back to {@link DexClassLoader}.
     */
    public static ClassLoader readDexFile(Context context, String apkPath, String dexOutKey) {
        if (context == null || apkPath == null || dexOutKey == null) {
            return null;
        }
        Context appContext = context.getApplicationContext();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ByteBuffer[] dexBuffers = extractDexBuffersFromApk(apkPath);
                if (dexBuffers.length == 0) {
                    Log.e(TAG, "No dex files found in apk: " + apkPath);
                    return null;
                }
                return new InMemoryDexClassLoader(dexBuffers, appContext.getClassLoader());
            }
            String optimizedDir = getOutDexPath(appContext, dexOutKey);
            return new DexClassLoader(
                    apkPath,
                    optimizedDir,
                    appContext.getApplicationInfo().nativeLibraryDir,
                    appContext.getClassLoader());
        } catch (Exception e) {
            Log.e(TAG, "readDexFile failed apk=" + apkPath, e);
            return null;
        }
    }

    private static ByteBuffer[] extractDexBuffersFromApk(String apkPath) {
        List<ByteBuffer> buffers = new ArrayList<>();
        try (ZipFile zipFile = new ZipFile(apkPath)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                if ("classes.dex".equals(name) || name.matches("classes\\d+\\.dex")) {
                    try (InputStream is = zipFile.getInputStream(entry)) {
                        byte[] data = readAllBytes(is);
                        buffers.add(ByteBuffer.wrap(data));
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "extractDexBuffersFromApk failed: " + apkPath, e);
        }
        return buffers.toArray(new ByteBuffer[0]);
    }

    private static byte[] readAllBytes(InputStream is) throws java.io.IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[COPY_BUFFER_SIZE];
        int len;
        while ((len = is.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        return bos.toByteArray();
    }

    /**
     * @deprecated Dynamic-proxy wiring removed; instantiate *InterfaceImp classes directly.
     */
    @Deprecated
    public static Object getObj(String interfaceClassName, ClassLoader classLoader) {
        try {
            Class<?> implClass = classLoader.loadClass(interfaceClassName + "Imp");
            return implClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            Log.e(TAG, "getObj failed for " + interfaceClassName, e);
            return null;
        }
    }
}
