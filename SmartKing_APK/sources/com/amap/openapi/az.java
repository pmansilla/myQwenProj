package com.amap.openapi;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import com.amap.location.common.model.AmapLoc;
import java.lang.reflect.Method;

/* compiled from: DeviceUtil.java */
/* loaded from: classes.dex */
public class az {
    private static String a;
    private static long b;
    private static boolean c;

    @NonNull
    public static String a(@NonNull Context context) {
        if (a == null) {
            a = context.getPackageName();
        }
        String a2 = bd.a(a);
        a = a2;
        return a2;
    }

    public static boolean a(Location location) {
        if (location == null) {
            return false;
        }
        try {
            Method method = Location.class.getMethod("isFromMockProvider", null);
            if (method != null) {
                return ((Boolean) method.invoke(location, null)).booleanValue();
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean b(@NonNull Context context) {
        boolean z = false;
        if (Build.VERSION.SDK_INT >= 23) {
            return false;
        }
        if (b > 0 && SystemClock.elapsedRealtime() - b < 180000) {
            return c;
        }
        try {
            String string = Settings.Secure.getString(context.getContentResolver(), "mock_location");
            if (string != null) {
                if (!string.equals(AmapLoc.RESULT_TYPE_GPS)) {
                    z = true;
                }
            }
        } catch (SecurityException unused) {
        }
        c = z;
        b = SystemClock.elapsedRealtime();
        return z;
    }

    public static boolean c(Context context) {
        try {
            PowerManager powerManager = (PowerManager) context.getSystemService("power");
            return Build.VERSION.SDK_INT >= 20 ? powerManager.isInteractive() : powerManager.isScreenOn();
        } catch (Throwable unused) {
            return false;
        }
    }
}
