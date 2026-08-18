package com.speed.hotpatch.libs;

import java.util.UUID;

/**
 *  by liyihang
 */
public class SpeedConfig {

    public static final String APK_NAME="apkName";
    public static final String CLASS_TAG="classTag";
    public static final String IN_PROCESS_TOKEN_EXTRA="speed_tools_in_process_token";
    public static final String ROOT_CLASS_NAME ="root_class";
    public static final String ACTIVITY_URL="speed_tools://jason.com/find_class";

    private static final String IN_PROCESS_TOKEN = UUID.randomUUID().toString();

    static String getInProcessToken() {
        return IN_PROCESS_TOKEN;
    }

    static boolean matchesInProcessToken(String token) {
        return IN_PROCESS_TOKEN.equals(token);
    }

}
