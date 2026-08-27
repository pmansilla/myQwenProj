package com.amap.location.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.amap.location.common.util.f;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;

/* compiled from: DeviceInfo.java */
/* loaded from: classes.dex */
public class a {
    public static InterfaceC0027a a = null;
    public static b b = null;
    private static volatile String c = null;
    private static volatile String d = null;
    private static volatile String e = null;
    private static volatile String f = null;
    private static volatile String g = null;
    private static volatile boolean h = true;

    /* compiled from: DeviceInfo.java */
    /* renamed from: com.amap.location.common.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public interface InterfaceC0027a {
        String a();
    }

    /* compiled from: DeviceInfo.java */
    /* loaded from: classes.dex */
    public interface b {
        String a(Context context);
    }

    public static String a() {
        if (TextUtils.isEmpty(e)) {
            try {
                if (a != null) {
                    e = a.a();
                }
            } catch (Exception unused) {
            }
        }
        return e == null ? "" : e;
    }

    public static String a(Context context) {
        if (c == null && h) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getApplicationContext().getSystemService("phone");
                c = Build.VERSION.SDK_INT >= 26 ? telephonyManager.getImei() : telephonyManager.getDeviceId();
            } catch (Throwable unused) {
            }
            if (c == null) {
                c = "";
            }
        }
        return c == null ? "" : c;
    }

    public static void a(Context context, String str) {
        if (TextUtils.isEmpty(str) || str.equals(d)) {
            return;
        }
        d = str;
        try {
            context.getSharedPreferences("sp_common", 0).edit().putString("tid", com.amap.location.common.util.a.a(str)).apply();
        } catch (Exception unused) {
        }
    }

    public static void a(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        e = str;
    }

    public static void a(boolean z) {
        h = z;
    }

    public static String b() {
        return Build.MODEL == null ? "" : Build.MODEL;
    }

    public static String b(Context context) {
        if (d == null) {
            try {
                if (h) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("sp_common", 0);
                    String b2 = com.amap.location.common.util.a.b(sharedPreferences.getString("tid", null));
                    if (!TextUtils.isEmpty(b2) || b == null) {
                        d = b2;
                    } else {
                        String a2 = b.a(context);
                        if (TextUtils.isEmpty(a2)) {
                            a2 = "";
                        } else {
                            sharedPreferences.edit().putString("tid", com.amap.location.common.util.a.a(a2)).apply();
                        }
                        d = a2;
                    }
                }
            } catch (Exception unused) {
            }
        }
        return d == null ? "" : d;
    }

    public static void b(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        e = str;
    }

    public static String c() {
        return Build.MANUFACTURER == null ? "" : Build.MANUFACTURER;
    }

    public static String c(Context context) {
        if (TextUtils.isEmpty(e)) {
            try {
                if (a != null) {
                    e = a.a();
                }
            } catch (Exception unused) {
            }
        }
        return e == null ? "" : e;
    }

    public static int d() {
        return Build.VERSION.SDK_INT;
    }

    public static String d(Context context) {
        if (f == null) {
            try {
                if (h) {
                    f = ((TelephonyManager) context.getApplicationContext().getSystemService("phone")).getSubscriberId();
                }
            } catch (SecurityException | Exception unused) {
            }
            if (f == null) {
                f = "";
            }
        }
        return f == null ? "" : f;
    }

    public static long e(Context context) {
        return f.a(f(context));
    }

    private static String e() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            if (networkInterfaces == null) {
                return "";
            }
            for (NetworkInterface networkInterface : Collections.list(networkInterfaces)) {
                if (networkInterface.getName().equalsIgnoreCase("wlan0")) {
                    byte[] hardwareAddress = Build.VERSION.SDK_INT >= 9 ? networkInterface.getHardwareAddress() : null;
                    if (hardwareAddress == null) {
                        return "";
                    }
                    StringBuilder sb = new StringBuilder();
                    for (byte b2 : hardwareAddress) {
                        sb.append(String.format("%02X:", Byte.valueOf(b2)));
                    }
                    if (sb.length() > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    return sb.toString();
                }
            }
            return "";
        } catch (Exception unused) {
            return "";
        }
    }

    public static String f(Context context) {
        WifiInfo connectionInfo;
        if (!TextUtils.isEmpty(g)) {
            return g;
        }
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            if (wifiManager == null || (connectionInfo = wifiManager.getConnectionInfo()) == null) {
                return "";
            }
            String macAddress = connectionInfo.getMacAddress();
            if (Build.VERSION.SDK_INT >= 23 && macAddress != null && macAddress.equals("02:00:00:00:00:00")) {
                macAddress = e();
            }
            if (macAddress == null || macAddress.length() <= 0) {
                return "";
            }
            String replace = macAddress.replace(":", "");
            if (replace != null && replace.length() > 0) {
                g = replace;
            }
            return replace;
        } catch (SecurityException | Exception unused) {
            return "";
        }
    }
}
