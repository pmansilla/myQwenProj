package ycble.runchinaup.util;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import java.util.Locale;

/* loaded from: classes2.dex */
public class PhoneDeviceUtil {
    private PhoneDeviceUtil() {
    }

    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager != null) {
            return telephonyManager.getDeviceId();
        }
        return null;
    }

    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    public static String getSystemModel() {
        return Build.MODEL;
    }

    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    public static boolean hasPermissions(Context context, String... strArr) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        for (String str : strArr) {
            if (!(ContextCompat.checkSelfPermission(context, str) == 0)) {
                return false;
            }
        }
        return true;
    }
}
