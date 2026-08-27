package com.loc;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.ServiceInfo;
import android.location.Location;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.MotionEventCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.SparseArray;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.DPoint;
import com.amap.location.common.model.AmapLoc;
import com.autonavi.aps.amapapi.model.AMapLocationServer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Random;
import org.json.JSONObject;

/* compiled from: Utils.java */
/* loaded from: classes.dex */
public final class fa {
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

    public static float a(AMapLocation aMapLocation, AMapLocation aMapLocation2) {
        return a(new double[]{aMapLocation.getLatitude(), aMapLocation.getLongitude(), aMapLocation2.getLatitude(), aMapLocation2.getLongitude()});
    }

    public static float a(DPoint dPoint, DPoint dPoint2) {
        return a(new double[]{dPoint.getLatitude(), dPoint.getLongitude(), dPoint2.getLatitude(), dPoint2.getLongitude()});
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

    public static Object a(Context context, String str) {
        if (context == null) {
            return null;
        }
        try {
            return context.getApplicationContext().getSystemService(str);
        } catch (Throwable th) {
            es.a(th, "Utils", "getServ");
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0025  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x002b A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:15:0x002e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String a(long r4, java.lang.String r6) {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r6)
            if (r0 == 0) goto L8
            java.lang.String r6 = "yyyy-MM-dd HH:mm:ss"
        L8:
            r0 = 0
            java.text.SimpleDateFormat r1 = new java.text.SimpleDateFormat     // Catch: java.lang.Throwable -> L16
            java.util.Locale r2 = java.util.Locale.CHINA     // Catch: java.lang.Throwable -> L16
            r1.<init>(r6, r2)     // Catch: java.lang.Throwable -> L16
            r1.applyPattern(r6)     // Catch: java.lang.Throwable -> L14
            goto L1f
        L14:
            r6 = move-exception
            goto L18
        L16:
            r6 = move-exception
            r1 = r0
        L18:
            java.lang.String r0 = "Utils"
            java.lang.String r2 = "formatUTC"
            com.loc.es.a(r6, r0, r2)
        L1f:
            r2 = 0
            int r6 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r6 > 0) goto L29
            long r4 = java.lang.System.currentTimeMillis()
        L29:
            if (r1 != 0) goto L2e
            java.lang.String r4 = "NULL"
            return r4
        L2e:
            java.lang.Long r4 = java.lang.Long.valueOf(r4)
            java.lang.String r4 = r1.format(r4)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.fa.a(long, java.lang.String):java.lang.String");
    }

    public static void a() {
    }

    public static boolean a(long j, long j2) {
        String a2 = a(j, "yyyyMMddHH");
        String a3 = a(j2, "yyyyMMddHH");
        if ("NULL".equals(a2) || "NULL".equals(a3)) {
            return false;
        }
        return a2.equals(a3);
    }

    public static boolean a(Context context) {
        if (context == null) {
            return false;
        }
        try {
            return d() < 17 ? d(context, "android.provider.Settings$System") : d(context, "android.provider.Settings$Global");
        } catch (Throwable unused) {
            return false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x005b  */
    /* JADX WARN: Removed duplicated region for block: B:19:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean a(android.database.sqlite.SQLiteDatabase r6, java.lang.String r7) {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r7)
            r1 = 0
            if (r0 == 0) goto L8
            return r1
        L8:
            java.lang.String r0 = "2.0.201501131131"
            java.lang.String r2 = "."
            java.lang.String r3 = ""
            java.lang.String r0 = r0.replace(r2, r3)
            if (r6 == 0) goto L6e
            r2 = 1
            r3 = 0
            boolean r4 = r6.isOpen()     // Catch: java.lang.Throwable -> L5f java.lang.Throwable -> L66
            if (r4 != 0) goto L1d
            goto L6e
        L1d:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L5f java.lang.Throwable -> L66
            r4.<init>()     // Catch: java.lang.Throwable -> L5f java.lang.Throwable -> L66
            java.lang.String r5 = "SELECT count(*) as c FROM sqlite_master WHERE type = 'table' AND name = '"
            r4.append(r5)     // Catch: java.lang.Throwable -> L5f java.lang.Throwable -> L66
            java.lang.String r7 = r7.trim()     // Catch: java.lang.Throwable -> L5f java.lang.Throwable -> L66
            r4.append(r7)     // Catch: java.lang.Throwable -> L5f java.lang.Throwable -> L66
            r4.append(r0)     // Catch: java.lang.Throwable -> L5f java.lang.Throwable -> L66
            java.lang.String r7 = "' "
            r4.append(r7)     // Catch: java.lang.Throwable -> L5f java.lang.Throwable -> L66
            java.lang.String r7 = r4.toString()     // Catch: java.lang.Throwable -> L5f java.lang.Throwable -> L66
            android.database.Cursor r6 = r6.rawQuery(r7, r3)     // Catch: java.lang.Throwable -> L5f java.lang.Throwable -> L66
            if (r6 == 0) goto L51
            boolean r7 = r6.moveToFirst()     // Catch: java.lang.Throwable -> L4e java.lang.Throwable -> L67
            if (r7 == 0) goto L51
            int r7 = r6.getInt(r1)     // Catch: java.lang.Throwable -> L4e java.lang.Throwable -> L67
            if (r7 <= 0) goto L51
            r7 = 1
            goto L52
        L4e:
            r7 = move-exception
            r3 = r6
            goto L60
        L51:
            r7 = 0
        L52:
            int r0 = r4.length()     // Catch: java.lang.Throwable -> L4e java.lang.Throwable -> L67
            r4.delete(r1, r0)     // Catch: java.lang.Throwable -> L4e java.lang.Throwable -> L67
            if (r6 == 0) goto L6d
            r6.close()
            goto L6d
        L5f:
            r7 = move-exception
        L60:
            if (r3 == 0) goto L65
            r3.close()
        L65:
            throw r7
        L66:
            r6 = r3
        L67:
            if (r6 == 0) goto L6c
            r6.close()
        L6c:
            r7 = 1
        L6d:
            return r7
        L6e:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.fa.a(android.database.sqlite.SQLiteDatabase, java.lang.String):boolean");
    }

    public static boolean a(Location location, int i) {
        Boolean bool = false;
        if (Build.VERSION.SDK_INT >= 18) {
            try {
                bool = (Boolean) ew.a(location, "isFromMockProvider", new Object[0]);
            } catch (Throwable unused) {
            }
        }
        if (bool.booleanValue()) {
            return true;
        }
        Bundle extras = location.getExtras();
        if ((extras != null ? extras.getInt("satellites") : 0) <= 0) {
            return true;
        }
        return i == 0 && location.getAltitude() == 0.0d && location.getBearing() == 0.0f && location.getSpeed() == 0.0f;
    }

    public static boolean a(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            return b(aMapLocation);
        }
        return false;
    }

    public static boolean a(AMapLocationServer aMapLocationServer) {
        if (aMapLocationServer == null || AmapLoc.RESULT_TYPE_FAIL.equals(aMapLocationServer.d()) || AmapLoc.RESULT_TYPE_SELF_LAT_LON.equals(aMapLocationServer.d()) || AmapLoc.RESULT_TYPE_NO_LONGER_USED.equals(aMapLocationServer.d())) {
            return false;
        }
        return b(aMapLocationServer);
    }

    public static boolean a(String str) {
        return (TextUtils.isEmpty(str) || "00:00:00:00:00:00".equals(str) || str.contains(" :")) ? false : true;
    }

    public static boolean a(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            ArrayList<String> d2 = d(str);
            String[] split = str2.toString().split("#");
            int i = 0;
            int i2 = 0;
            for (int i3 = 0; i3 < split.length; i3++) {
                if (split[i3].contains(",nb") || split[i3].contains(",access")) {
                    i++;
                    if (d2.contains(split[i3])) {
                        i2++;
                    }
                }
            }
            int size = d2.size() + i;
            double d3 = i2 * 2;
            double d4 = size;
            Double.isNaN(d4);
            if (d3 >= d4 * 0.618d) {
                return true;
            }
        }
        return false;
    }

    public static boolean a(JSONObject jSONObject, String str) {
        return ad.a(jSONObject, str);
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
            return ad.b(bArr);
        } catch (Throwable th) {
            es.a(th, "Utils", "gz");
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
            es.a(th, "Utils", "getMccMnc");
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
        return System.currentTimeMillis();
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
        if (!TextUtils.isEmpty(es.g)) {
            return es.g;
        }
        if (context == null) {
            return null;
        }
        try {
            packageInfo = context.getPackageManager().getPackageInfo(u.c(context), 64);
        } catch (Throwable th) {
            es.a(th, "Utils", "getAppName part");
            packageInfo = null;
        }
        try {
            if (TextUtils.isEmpty(es.h)) {
                es.h = null;
            }
        } catch (Throwable th2) {
            es.a(th2, "Utils", "getAppName");
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
        String c2 = u.c(context);
        if (!TextUtils.isEmpty(c2)) {
            sb.append(",");
            sb.append(c2);
        }
        if (!TextUtils.isEmpty(es.h)) {
            sb.append(",");
            sb.append(es.h);
        }
        String sb2 = sb.toString();
        es.g = sb2;
        return sb2;
    }

    public static String b(TelephonyManager telephonyManager) {
        int i = 0;
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
        if (telephonyManager != null) {
            try {
                i = telephonyManager.getNetworkType();
            } catch (Throwable unused) {
            }
        }
        return e.get(i, "UNKWN");
    }

    public static boolean b(long j, long j2) {
        String a2 = a(j, "yyyyMMdd");
        String a3 = a(j2, "yyyyMMdd");
        if ("NULL".equals(a2) || "NULL".equals(a3)) {
            return false;
        }
        return a2.equals(a3);
    }

    public static boolean b(Context context, String str) {
        try {
            return context.getPackageManager().getPackageInfo(str, 0) != null;
        } catch (Throwable unused) {
            return false;
        }
    }

    public static boolean b(AMapLocation aMapLocation) {
        double longitude = aMapLocation.getLongitude();
        double latitude = aMapLocation.getLatitude();
        return !(longitude == 0.0d && latitude == 0.0d) && longitude <= 180.0d && latitude <= 90.0d && longitude >= -180.0d && latitude >= -90.0d;
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
        return a(Integer.parseInt(str), (byte[]) null);
    }

    public static double c(double d2) {
        double d3 = (long) (d2 * 1000000.0d);
        Double.isNaN(d3);
        return d3 / 1000000.0d;
    }

    public static long c() {
        return SystemClock.elapsedRealtime();
    }

    public static NetworkInfo c(Context context) {
        try {
            return x.r(context);
        } catch (Throwable th) {
            es.a(th, "Utils", "getNetWorkInfo");
            return null;
        }
    }

    public static boolean c(long j, long j2) {
        if (!b(j, j2)) {
            return false;
        }
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTimeInMillis(j);
        int i = calendar.get(11);
        calendar.setTimeInMillis(j2);
        int i2 = calendar.get(11);
        if (i > 12) {
            if (i2 > 12) {
                return true;
            }
        } else if (i2 <= 12) {
            return true;
        }
        return false;
    }

    public static boolean c(Context context, String str) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getApplicationContext().getPackageManager().getPackageInfo(str, 256);
        } catch (Throwable unused) {
            packageInfo = null;
        }
        return packageInfo != null;
    }

    public static byte[] c(String str) {
        return b(Integer.parseInt(str), (byte[]) null);
    }

    public static int d() {
        if (b > 0) {
            return b;
        }
        try {
            try {
                return ew.b("android.os.Build$VERSION", "SDK_INT");
            } catch (Throwable unused) {
                return 0;
            }
        } catch (Throwable unused2) {
            return Integer.parseInt(ew.a("android.os.Build$VERSION", "SDK").toString());
        }
    }

    public static ArrayList<String> d(String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (!TextUtils.isEmpty(str)) {
            String[] split = str.split("#");
            for (int i = 0; i < split.length; i++) {
                if (split[i].contains(",nb") || split[i].contains(",access")) {
                    arrayList.add(split[i]);
                }
            }
        }
        return arrayList;
    }

    public static boolean d(Context context) {
        try {
            NetworkInfo c2 = c(context);
            if (c2 != null) {
                if (c2.isConnectedOrConnecting()) {
                    return true;
                }
            }
        } catch (Throwable unused) {
        }
        return false;
    }

    private static boolean d(Context context, String str) throws Throwable {
        return ((Integer) ew.a(str, "getInt", new Object[]{context.getContentResolver(), ((String) ew.a(str, "AIRPLANE_MODE_ON")).toString()}, (Class<?>[]) new Class[]{ContentResolver.class, String.class})).intValue() == 1;
    }

    public static double e(String str) throws NumberFormatException {
        return Double.parseDouble(str);
    }

    public static String e() {
        return Build.MODEL;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x002e, code lost:
    
        if (r2.importance == 100) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0030, code lost:
    
        return true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean e(android.content.Context r6) {
        /*
            r0 = 1
            java.lang.String r1 = "activity"
            java.lang.Object r1 = r6.getSystemService(r1)     // Catch: java.lang.Throwable -> L32
            android.app.ActivityManager r1 = (android.app.ActivityManager) r1     // Catch: java.lang.Throwable -> L32
            java.util.List r1 = r1.getRunningAppProcesses()     // Catch: java.lang.Throwable -> L32
            java.util.Iterator r1 = r1.iterator()     // Catch: java.lang.Throwable -> L32
        L11:
            boolean r2 = r1.hasNext()     // Catch: java.lang.Throwable -> L32
            r3 = 0
            if (r2 == 0) goto L31
            java.lang.Object r2 = r1.next()     // Catch: java.lang.Throwable -> L32
            android.app.ActivityManager$RunningAppProcessInfo r2 = (android.app.ActivityManager.RunningAppProcessInfo) r2     // Catch: java.lang.Throwable -> L32
            java.lang.String r4 = r2.processName     // Catch: java.lang.Throwable -> L32
            java.lang.String r5 = com.loc.u.c(r6)     // Catch: java.lang.Throwable -> L32
            boolean r4 = r4.equals(r5)     // Catch: java.lang.Throwable -> L32
            if (r4 == 0) goto L11
            int r6 = r2.importance     // Catch: java.lang.Throwable -> L32
            r1 = 100
            if (r6 == r1) goto L31
            return r0
        L31:
            return r3
        L32:
            r6 = move-exception
            java.lang.String r1 = "Utils"
            java.lang.String r2 = "isApplicationBroughtToBackground"
            com.loc.es.a(r6, r1, r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.fa.e(android.content.Context):boolean");
    }

    public static float f(String str) throws NumberFormatException {
        return Float.parseFloat(str);
    }

    public static String f() {
        return Build.VERSION.RELEASE;
    }

    public static boolean f(Context context) {
        int i;
        if (Build.VERSION.SDK_INT < 23 || context.getApplicationInfo().targetSdkVersion < 23) {
            for (String str : f) {
                if (context.checkCallingOrSelfPermission(str) != 0) {
                    return false;
                }
            }
        } else {
            Application application = (Application) context;
            for (String str2 : f) {
                try {
                    i = ew.b(application.getBaseContext(), "checkSelfPermission", str2);
                } catch (Throwable unused) {
                    i = 0;
                }
                if (i != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int g() {
        return new Random().nextInt(65536) - 32768;
    }

    public static int g(String str) throws NumberFormatException {
        return Integer.parseInt(str);
    }

    public static boolean g(Context context) {
        int i;
        if (context.getApplicationInfo().targetSdkVersion < 29) {
            return true;
        }
        try {
            i = ew.b(((Application) context).getBaseContext(), "checkSelfPermission", g);
        } catch (Throwable unused) {
            i = 0;
        }
        return i == 0;
    }

    public static int h(String str) throws NumberFormatException {
        return Integer.parseInt(str, 16);
    }

    public static void h() {
        d.clear();
    }

    @SuppressLint({"NewApi"})
    public static boolean h(Context context) {
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
        if (!z && d() > 17) {
            try {
                return "true".equals(String.valueOf(ew.a(a, "isScanAlwaysAvailable", new Object[0])));
            } catch (Throwable unused2) {
            }
        }
        return z;
    }

    public static byte i(String str) throws NumberFormatException {
        return Byte.parseByte(str);
    }

    public static String i() {
        try {
            return y.b("S128DF1572465B890OE3F7A13167KLEI".getBytes("UTF-8")).substring(20);
        } catch (Throwable unused) {
            return "";
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:11:0x0023. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0032 A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String i(android.content.Context r3) {
        /*
            android.net.NetworkInfo r3 = c(r3)
            if (r3 == 0) goto L52
            boolean r0 = r3.isConnectedOrConnecting()
            if (r0 != 0) goto Ld
            goto L52
        Ld:
            java.lang.String r0 = "UNKNOWN"
            int r1 = r3.getType()
            r2 = 1
            if (r1 != r2) goto L19
            java.lang.String r0 = "WIFI"
            goto L51
        L19:
            if (r1 != 0) goto L51
            java.lang.String r0 = r3.getSubtypeName()
            int r3 = r3.getSubtype()
            switch(r3) {
                case 1: goto L35;
                case 2: goto L35;
                case 3: goto L32;
                case 4: goto L35;
                case 5: goto L32;
                case 6: goto L32;
                case 7: goto L35;
                case 8: goto L32;
                case 9: goto L32;
                case 10: goto L32;
                case 11: goto L35;
                case 12: goto L32;
                case 13: goto L2f;
                case 14: goto L32;
                case 15: goto L32;
                case 16: goto L35;
                case 17: goto L32;
                default: goto L26;
            }
        L26:
            java.lang.String r3 = "GSM"
            boolean r3 = r3.equalsIgnoreCase(r0)
            if (r3 == 0) goto L38
            goto L35
        L2f:
            java.lang.String r0 = "4G"
            goto L51
        L32:
            java.lang.String r0 = "3G"
            goto L51
        L35:
            java.lang.String r0 = "2G"
            goto L51
        L38:
            java.lang.String r3 = "TD-SCDMA"
            boolean r3 = r3.equalsIgnoreCase(r0)
            if (r3 != 0) goto L32
            java.lang.String r3 = "WCDMA"
            boolean r3 = r3.equalsIgnoreCase(r0)
            if (r3 != 0) goto L32
            java.lang.String r3 = "CDMA2000"
            boolean r3 = r3.equalsIgnoreCase(r0)
            if (r3 == 0) goto L51
            goto L32
        L51:
            return r0
        L52:
            java.lang.String r3 = "DISCONNECTED"
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.fa.i(android.content.Context):java.lang.String");
    }

    public static String j(Context context) {
        String l = x.l(context);
        if (TextUtils.isEmpty(l) || l.equals("00:00:00:00:00:00")) {
            l = "00:00:00:00:00:00";
            if (context != null) {
                l = ez.b(context, "pref", "smac", "00:00:00:00:00:00");
            }
        }
        if (TextUtils.isEmpty(l)) {
            l = "00:00:00:00:00:00";
        }
        if (!h) {
            if (context != null && !TextUtils.isEmpty(l)) {
                ez.a(context, "pref", "smac", l);
            }
            h = true;
        }
        return l;
    }

    public static boolean k(Context context) {
        return Build.VERSION.SDK_INT >= 28 && context.getApplicationInfo().targetSdkVersion >= 28;
    }

    public static boolean l(Context context) {
        ServiceInfo serviceInfo;
        try {
            serviceInfo = context.getPackageManager().getServiceInfo(new ComponentName(context, "com.amap.api.location.APSService"), 128);
        } catch (Throwable unused) {
            serviceInfo = null;
        }
        return serviceInfo != null;
    }
}
