package com.amap.api.mapcore.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.location.Location;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.view.MotionEventCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.SparseArray;
import com.amap.location.common.model.AmapLoc;
import java.util.Hashtable;
import java.util.Random;
import org.json.JSONObject;

/* compiled from: Utils.java */
/* loaded from: classes.dex */
public final class la {
    private static int b;
    private static String[] c;
    private static Hashtable<String, Long> d = new Hashtable<>();
    private static SparseArray<String> e = null;
    private static String[] f = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};
    private static String g = "android.permission.ACCESS_BACKGROUND_LOCATION";
    static WifiManager a = null;
    private static boolean h = false;

    public static double a(double d2) {
        double d3 = (long) (d2 * 1000000.0d);
        Double.isNaN(d3);
        return d3 / 1000000.0d;
    }

    public static float a(float f2) {
        double d2 = f2;
        Double.isNaN(d2);
        double d3 = (long) (d2 * 100.0d);
        Double.isNaN(d3);
        return (float) (d3 / 100.0d);
    }

    public static float a(double[] dArr) {
        float[] fArr = new float[1];
        Location.distanceBetween(dArr[0], dArr[1], dArr[2], dArr[3], fArr);
        return fArr[0];
    }

    public static int a(int i) {
        return (i * 2) - 113;
    }

    public static int a(NetworkInfo networkInfo) {
        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
            return networkInfo.getType();
        }
        return -1;
    }

    public static long a() {
        return System.currentTimeMillis();
    }

    public static Object a(Context context, String str) {
        if (context == null) {
            return null;
        }
        try {
            return context.getApplicationContext().getSystemService(str);
        } catch (Throwable th) {
            kw.a(th, "Utils", "getServ");
            return null;
        }
    }

    public static boolean a(Context context) {
        if (context == null) {
            return false;
        }
        try {
            return c() < 17 ? b(context, "android.provider.Settings$System") : b(context, "android.provider.Settings$Global");
        } catch (Throwable unused) {
            return false;
        }
    }

    public static boolean a(String str) {
        return (TextUtils.isEmpty(str) || "00:00:00:00:00:00".equals(str) || str.contains(" :")) ? false : true;
    }

    public static boolean a(JSONObject jSONObject, String str) {
        return hp.a(jSONObject, str);
    }

    public static byte[] a(int i, byte[] bArr) {
        if (bArr == null || bArr.length < 2) {
            bArr = new byte[2];
        }
        bArr[0] = (byte) (i & 255);
        bArr[1] = (byte) ((i & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8);
        return bArr;
    }

    public static byte[] a(long j) {
        byte[] bArr = new byte[8];
        for (int i = 0; i < 8; i++) {
            bArr[i] = (byte) ((j >> (i * 8)) & 255);
        }
        return bArr;
    }

    public static byte[] a(byte[] bArr) {
        try {
            return hp.b(bArr);
        } catch (Throwable th) {
            kw.a(th, "Utils", "gz");
            return null;
        }
    }

    public static String[] a(TelephonyManager telephonyManager) {
        int i;
        String networkOperator = telephonyManager != null ? telephonyManager.getNetworkOperator() : null;
        String[] strArr = {AmapLoc.RESULT_TYPE_GPS, AmapLoc.RESULT_TYPE_GPS};
        if (!TextUtils.isEmpty(networkOperator) && TextUtils.isDigitsOnly(networkOperator) && networkOperator.length() > 4) {
            strArr[0] = networkOperator.substring(0, 3);
            char[] charArray = networkOperator.substring(3).toCharArray();
            int i2 = 0;
            while (i2 < charArray.length && Character.isDigit(charArray[i2])) {
                i2++;
            }
            strArr[1] = networkOperator.substring(3, i2 + 3);
        }
        try {
            i = Integer.parseInt(strArr[0]);
        } catch (Throwable th) {
            kw.a(th, "Utils", "getMccMnc");
            i = 0;
        }
        if (i == 0) {
            strArr[0] = AmapLoc.RESULT_TYPE_GPS;
        }
        if (AmapLoc.RESULT_TYPE_GPS.equals(strArr[0]) || AmapLoc.RESULT_TYPE_GPS.equals(strArr[1])) {
            return (AmapLoc.RESULT_TYPE_GPS.equals(strArr[0]) && AmapLoc.RESULT_TYPE_GPS.equals(strArr[1]) && c != null) ? c : strArr;
        }
        c = strArr;
        return strArr;
    }

    public static double b(double d2) {
        double d3 = (long) (d2 * 100.0d);
        Double.isNaN(d3);
        return d3 / 100.0d;
    }

    public static long b() {
        return SystemClock.elapsedRealtime();
    }

    public static String b(int i) {
        switch (i) {
            case 0:
                return "success";
            case 1:
                return "重要参数为空";
            case 2:
                return "WIFI信息不足";
            case 3:
                return "请求参数获取出现异常";
            case 4:
                return "网络连接异常";
            case 5:
                return "解析数据异常";
            case 6:
                return "定位结果错误";
            case 7:
                return "KEY错误";
            case 8:
                return "其他错误";
            case 9:
                return "初始化异常";
            case 10:
                return "定位服务启动失败";
            case 11:
                return "错误的基站信息，请检查是否插入SIM卡";
            case 12:
                return "缺少定位权限";
            case 13:
                return "网络定位失败，请检查设备是否插入sim卡，是否开启移动网络或开启了wifi模块";
            case 14:
                return "GPS 定位失败，由于设备当前 GPS 状态差,建议持设备到相对开阔的露天场所再次尝试";
            case 15:
                return "当前返回位置为模拟软件返回，请关闭模拟软件，或者在option中设置允许模拟";
            case 16:
            case 17:
            default:
                return "其他错误";
            case 18:
                return "定位失败，飞行模式下关闭了WIFI开关，请关闭飞行模式或者打开WIFI开关";
            case 19:
                return "定位失败，没有检查到SIM卡，并且关闭了WIFI开关，请打开WIFI开关或者插入SIM卡";
        }
    }

    public static String b(Context context) {
        PackageInfo packageInfo;
        if (!TextUtils.isEmpty(kw.g)) {
            return kw.g;
        }
        if (context == null) {
            return null;
        }
        try {
            packageInfo = context.getPackageManager().getPackageInfo(hd.c(context), 64);
        } catch (Throwable th) {
            kw.a(th, "Utils", "getAppName part");
            packageInfo = null;
        }
        try {
            if (TextUtils.isEmpty(kw.h)) {
                kw.h = null;
            }
        } catch (Throwable th2) {
            kw.a(th2, "Utils", "getAppName");
        }
        StringBuilder sb = new StringBuilder();
        if (packageInfo != null) {
            CharSequence loadLabel = packageInfo.applicationInfo != null ? packageInfo.applicationInfo.loadLabel(context.getPackageManager()) : null;
            if (loadLabel != null) {
                sb.append(loadLabel.toString());
            }
            if (!TextUtils.isEmpty(packageInfo.versionName)) {
                sb.append(packageInfo.versionName);
            }
        }
        String c2 = hd.c(context);
        if (!TextUtils.isEmpty(c2)) {
            sb.append(",");
            sb.append(c2);
        }
        if (!TextUtils.isEmpty(kw.h)) {
            sb.append(",");
            sb.append(kw.h);
        }
        String sb2 = sb.toString();
        kw.g = sb2;
        return sb2;
    }

    public static String b(TelephonyManager telephonyManager) {
        if (e == null) {
            SparseArray<String> sparseArray = new SparseArray<>();
            e = sparseArray;
            sparseArray.append(0, "UNKWN");
            e.append(1, "GPRS");
            e.append(2, "EDGE");
            e.append(3, "UMTS");
            e.append(4, "CDMA");
            e.append(5, "EVDO_0");
            e.append(6, "EVDO_A");
            e.append(7, "1xRTT");
            e.append(8, "HSDPA");
            e.append(9, "HSUPA");
            e.append(10, "HSPA");
            e.append(11, "IDEN");
            e.append(12, "EVDO_B");
            e.append(13, "LTE");
            e.append(14, "EHRPD");
            e.append(15, "HSPAP");
        }
        return e.get(telephonyManager != null ? telephonyManager.getNetworkType() : 0, "UNKWN");
    }

    private static boolean b(Context context, String str) throws Throwable {
        return ((Integer) ky.a(str, "getInt", new Object[]{context.getContentResolver(), ((String) ky.a(str, "AIRPLANE_MODE_ON")).toString()}, (Class<?>[]) new Class[]{ContentResolver.class, String.class})).intValue() == 1;
    }

    public static byte[] b(int i, byte[] bArr) {
        if (bArr == null || bArr.length < 4) {
            bArr = new byte[4];
        }
        for (int i2 = 0; i2 < bArr.length; i2++) {
            bArr[i2] = (byte) ((i >> (i2 * 8)) & 255);
        }
        return bArr;
    }

    public static byte[] b(String str) {
        return a(d(str), (byte[]) null);
    }

    public static int c() {
        if (b > 0) {
            return b;
        }
        try {
            try {
                return ky.b("android.os.Build$VERSION", "SDK_INT");
            } catch (Throwable unused) {
                return 0;
            }
        } catch (Throwable unused2) {
            return Integer.parseInt(ky.a("android.os.Build$VERSION", "SDK").toString());
        }
    }

    @SuppressLint({"NewApi"})
    public static boolean c(Context context) {
        boolean z;
        if (context == null) {
            return true;
        }
        if (a == null) {
            a = (WifiManager) a(context, "wifi");
        }
        try {
            z = a.isWifiEnabled();
        } catch (Throwable unused) {
            z = false;
        }
        if (!z && c() > 17) {
            try {
                return "true".equals(String.valueOf(ky.a(a, "isScanAlwaysAvailable", new Object[0])));
            } catch (Throwable unused2) {
            }
        }
        return z;
    }

    public static byte[] c(String str) {
        return b(d(str), (byte[]) null);
    }

    public static int d(String str) throws NumberFormatException {
        return Integer.parseInt(str);
    }

    public static String d() {
        return Build.MODEL;
    }

    public static String d(Context context) {
        String m = hi.m(context);
        if (TextUtils.isEmpty(m) || m.equals("00:00:00:00:00:00")) {
            m = kz.a(context);
        }
        if (TextUtils.isEmpty(m)) {
            m = "00:00:00:00:00:00";
        }
        if (!h) {
            kz.a(context, m);
            h = true;
        }
        return m;
    }

    public static byte e(String str) throws NumberFormatException {
        return Byte.parseByte(str);
    }

    public static String e() {
        return Build.VERSION.RELEASE;
    }

    public static boolean e(Context context) {
        return Build.VERSION.SDK_INT >= 28 && context.getApplicationInfo().targetSdkVersion >= 28;
    }

    public static int f() {
        return new Random().nextInt(65536) - 32768;
    }
}
