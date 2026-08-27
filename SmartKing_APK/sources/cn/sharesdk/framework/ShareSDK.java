package cn.sharesdk.framework;

import android.graphics.Bitmap;
import java.util.HashMap;

/* loaded from: classes.dex */
public class ShareSDK {
    public static final String SDK_TAG = "SHARESDK";
    public static final int SDK_VERSION_CODE;
    public static final String SDK_VERSION_NAME = "3.4.1";
    private static f a = null;
    private static boolean b = true;

    static {
        int i = 0;
        for (String str : SDK_VERSION_NAME.split("\\.")) {
            i = (i * 100) + Integer.parseInt(str);
        }
        SDK_VERSION_CODE = i;
        c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String a(int i, String str) {
        c();
        return a.a(i, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String a(Bitmap bitmap) {
        c();
        return a.a(bitmap);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String a(String str) {
        c();
        return a.c(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String a(String str, boolean z, int i, String str2) {
        c();
        return a.a(str, z, i, str2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(int i, int i2) {
        c();
        a.a(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(String str, String str2) {
        c();
        a.a(str, str2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a() {
        c();
        return a.e();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String b(String str, String str2) {
        c();
        return a.b(str, str2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean b() {
        c();
        return a.f();
    }

    private static synchronized void c() {
        synchronized (ShareSDK.class) {
            if (a == null) {
                f fVar = new f();
                fVar.b();
                a = fVar;
            }
        }
    }

    public static void closeDebug() {
        b = false;
    }

    public static void deleteCache() {
        c();
        a.g();
    }

    public static boolean getEnableAuthTag() {
        c();
        return a.a();
    }

    public static Platform getPlatform(String str) {
        c();
        return a.a(str);
    }

    public static Platform[] getPlatformList() {
        c();
        return a.c();
    }

    public static <T extends Service> T getService(Class<T> cls) {
        c();
        return (T) a.c(cls);
    }

    public static boolean isDebug() {
        return b;
    }

    public static boolean isRemoveCookieOnAuthorize() {
        c();
        return a.d();
    }

    public static void logApiEvent(String str, int i) {
        c();
        a.a(str, i);
    }

    public static void logDemoEvent(int i, Platform platform) {
        c();
        a.a(i, platform);
    }

    public static String platformIdToName(int i) {
        c();
        return a.c(i);
    }

    public static int platformNameToId(String str) {
        c();
        return a.b(str);
    }

    public static void registerPlatform(Class<? extends CustomPlatform> cls) {
        c();
        a.d(cls);
    }

    public static void registerService(Class<? extends Service> cls) {
        c();
        a.a(cls);
    }

    public static void removeCookieOnAuthorize(boolean z) {
        c();
        a.b(z);
    }

    public static void setConnTimeout(int i) {
        c();
        a.a(i);
    }

    public static void setEnableAuthTag(boolean z) {
        c();
        a.a(z);
    }

    public static void setPlatformDevInfo(String str, HashMap<String, Object> hashMap) {
        c();
        a.a(str, hashMap);
    }

    public static void setReadTimeout(int i) {
        c();
        a.b(i);
    }

    public static void unregisterPlatform(Class<? extends CustomPlatform> cls) {
        c();
        a.e(cls);
    }

    public static void unregisterService(Class<? extends Service> cls) {
        c();
        a.b(cls);
    }
}
