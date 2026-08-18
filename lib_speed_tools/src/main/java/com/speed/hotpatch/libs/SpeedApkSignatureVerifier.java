package com.speed.hotpatch.libs;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.os.Build;
import android.util.Log;

import java.security.MessageDigest;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Verifies that plugin APKs use the host application's signing certificate.
 */
public final class SpeedApkSignatureVerifier {

    private static final String TAG = "SpeedApkSignature";

    private SpeedApkSignatureVerifier() {
    }

    public static boolean isSignedByHost(Context context, String apkPath) {
        if (context == null || apkPath == null) {
            Log.w(TAG, "Refusing APK signature verification with invalid arguments");
            return false;
        }
        try {
            PackageManager packageManager = context.getPackageManager();
            int flags = getSigningInfoFlags();
            PackageInfo hostInfo = packageManager.getPackageInfo(context.getPackageName(), flags);
            PackageInfo apkInfo = packageManager.getPackageArchiveInfo(apkPath, flags);
            Set<String> hostDigests = getCertificateDigests(hostInfo);
            Set<String> apkDigests = getCertificateDigests(apkInfo);
            if (hostDigests.isEmpty() || apkDigests.isEmpty()) {
                Log.w(TAG, "Refusing APK with missing signing certificates: " + apkPath);
                return false;
            }
            if (!hostDigests.equals(apkDigests)) {
                Log.w(TAG, "Refusing APK with a different signing certificate: " + apkPath);
                return false;
            }
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Refusing APK after signature verification failure: " + apkPath, e);
            return false;
        }
    }

    private static int getSigningInfoFlags() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return PackageManager.GET_SIGNING_CERTIFICATES;
        }
        return PackageManager.GET_SIGNATURES;
    }

    private static Set<String> getCertificateDigests(PackageInfo packageInfo) throws Exception {
        if (packageInfo == null) {
            return Collections.emptySet();
        }
        Signature[] signatures;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            SigningInfo signingInfo = packageInfo.signingInfo;
            if (signingInfo == null) {
                return Collections.emptySet();
            }
            signatures = signingInfo.getApkContentsSigners();
        } else {
            signatures = packageInfo.signatures;
        }
        if (signatures == null || signatures.length == 0) {
            return Collections.emptySet();
        }
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        Set<String> certificateDigests = new HashSet<>();
        for (Signature signature : signatures) {
            if (signature == null) {
                return Collections.emptySet();
            }
            certificateDigests.add(toHex(digest.digest(signature.toByteArray())));
            digest.reset();
        }
        return certificateDigests;
    }

    private static String toHex(byte[] bytes) {
        StringBuilder value = new StringBuilder(bytes.length * 2);
        for (byte current : bytes) {
            value.append(Character.forDigit((current >>> 4) & 0x0f, 16));
            value.append(Character.forDigit(current & 0x0f, 16));
        }
        return value.toString();
    }
}
