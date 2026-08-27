package com.loc;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Xml;
import android.view.WindowManager;
import com.amap.location.common.model.AmapLoc;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;

/* compiled from: DeviceInfo.java */
/* loaded from: classes.dex */
public final class x {
    static String a = "";
    static String b = "";
    public static boolean c = false;
    static String d = "";
    static boolean e = false;
    static int f = -1;
    static String g = "";
    static String h = "";
    private static String i = null;
    private static boolean j = false;
    private static String k = "";
    private static String l = "";
    private static String m = "";
    private static String n = "";
    private static String o = "";
    private static String p = "";
    private static boolean q = false;
    private static long r = 0;
    private static int s = 0;
    private static String t = null;
    private static String u = "";

    /* JADX WARN: Failed to find 'out' block for switch in B:13:0x004c. Please report as an issue. */
    private static String A(Context context) {
        FileInputStream fileInputStream = null;
        try {
            if (ad.a(context, "android.permission.READ_EXTERNAL_STORAGE") && "mounted".equals(Environment.getExternalStorageState())) {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/.UTSystemConfig/Global/Alvin2.xml");
                XmlPullParser newPullParser = Xml.newPullParser();
                FileInputStream fileInputStream2 = new FileInputStream(file);
                try {
                    newPullParser.setInput(fileInputStream2, "utf-8");
                    boolean z = false;
                    for (int eventType = newPullParser.getEventType(); 1 != eventType; eventType = newPullParser.next()) {
                        if (eventType != 0) {
                            switch (eventType) {
                                case 2:
                                    if (newPullParser.getAttributeCount() > 0) {
                                        int attributeCount = newPullParser.getAttributeCount();
                                        boolean z2 = z;
                                        for (int i2 = 0; i2 < attributeCount; i2++) {
                                            String attributeValue = newPullParser.getAttributeValue(i2);
                                            if ("UTDID2".equals(attributeValue) || "UTDID".equals(attributeValue)) {
                                                z2 = true;
                                            }
                                        }
                                        z = z2;
                                        break;
                                    } else {
                                        break;
                                    }
                                case 3:
                                    z = false;
                                    break;
                                case 4:
                                    if (z) {
                                        String text = newPullParser.getText();
                                        try {
                                            fileInputStream2.close();
                                        } catch (Throwable unused) {
                                        }
                                        return text;
                                    }
                                    break;
                            }
                        }
                    }
                    fileInputStream = fileInputStream2;
                } catch (Throwable th) {
                    th = th;
                    fileInputStream = fileInputStream2;
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (Throwable unused2) {
                        }
                    }
                    throw th;
                }
            }
            if (fileInputStream == null) {
                return "";
            }
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            fileInputStream.close();
        } catch (Throwable unused3) {
            return "";
        }
    }

    private static String B(Context context) throws InvocationTargetException, IllegalAccessException {
        if (Build.VERSION.SDK_INT >= 29) {
            return "";
        }
        if ((u == null || "".equals(u)) && b(context, ad.c("WYW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfUEhPTkVfU1RBVEU="))) {
            TelephonyManager G = G(context);
            if (G == null) {
                return "";
            }
            Method a2 = ad.a(G.getClass(), "UZ2V0U3Vic2NyaWJlcklk", (Class<?>[]) new Class[0]);
            if (a2 != null) {
                u = (String) a2.invoke(G, new Object[0]);
            }
            if (u == null) {
                u = "";
            }
            return u;
        }
        return u;
    }

    private static String C(Context context) {
        if (!b(context, ad.c("WYW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfUEhPTkVfU1RBVEU="))) {
            return null;
        }
        TelephonyManager G = G(context);
        if (G == null) {
            return "";
        }
        String simOperatorName = G.getSimOperatorName();
        return TextUtils.isEmpty(simOperatorName) ? G.getNetworkOperatorName() : simOperatorName;
    }

    private static int D(Context context) {
        ConnectivityManager E;
        NetworkInfo activeNetworkInfo;
        if (context == null || !b(context, ad.c("AYW5kcm9pZC5wZXJtaXNzaW9uLkFDQ0VTU19ORVRXT1JLX1NUQVRF")) || (E = E(context)) == null || (activeNetworkInfo = E.getActiveNetworkInfo()) == null) {
            return -1;
        }
        return activeNetworkInfo.getType();
    }

    private static ConnectivityManager E(Context context) {
        return (ConnectivityManager) context.getSystemService("connectivity");
    }

    private static int F(Context context) {
        TelephonyManager G;
        if (b(context, ad.c("WYW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfUEhPTkVfU1RBVEU=")) && (G = G(context)) != null) {
            return G.getNetworkType();
        }
        return -1;
    }

    private static TelephonyManager G(Context context) {
        return (TelephonyManager) context.getSystemService("phone");
    }

    public static String a() {
        return i;
    }

    public static String a(final Context context) {
        if (!TextUtils.isEmpty(b)) {
            return b;
        }
        String a2 = a.a(context);
        if (!TextUtils.isEmpty(a2)) {
            b = a2;
            return a2;
        }
        if (j) {
            return "";
        }
        j = true;
        aq.d().submit(new Runnable() { // from class: com.loc.x.1
            @Override // java.lang.Runnable
            public final void run() {
                byte[] bArr;
                Map<String, String> a3 = a.a();
                if (a3 == null) {
                    return;
                }
                String a4 = a.a(x.g(context), x.u(context), x.l(context), x.x(context));
                if (TextUtils.isEmpty(a4)) {
                    return;
                }
                byte[] bArr2 = new byte[0];
                try {
                    bg.a();
                    bArr = bg.a(new bf(a4.getBytes(), a3));
                } catch (t e2) {
                    e2.printStackTrace();
                    bArr = bArr2;
                }
                a.a(context, new String(bArr));
            }
        });
        return "";
    }

    public static String a(Context context, String str) {
        if (Build.VERSION.SDK_INT >= 29) {
            return "";
        }
        try {
            if (Build.VERSION.SDK_INT < 21) {
                return "";
            }
            if (!TextUtils.isEmpty(g)) {
                return g;
            }
            TelephonyManager G = G(context);
            if (f == -1) {
                Method a2 = ad.a(TelephonyManager.class, "UZ2V0UGhvbmVDb3VudA=", (Class<?>[]) new Class[0]);
                if (a2 != null) {
                    try {
                        f = ((Integer) a2.invoke(G, new Object[0])).intValue();
                    } catch (Throwable unused) {
                    }
                }
                f = 0;
            }
            Method a3 = ad.a(TelephonyManager.class, "MZ2V0SW1laQ=", (Class<?>[]) new Class[]{Integer.TYPE});
            if (a3 == null) {
                f = 0;
                return "";
            }
            StringBuilder sb = new StringBuilder();
            for (int i2 = 0; i2 < f; i2++) {
                try {
                    sb.append((String) a3.invoke(G, Integer.valueOf(i2)));
                    sb.append(str);
                } catch (Throwable unused2) {
                }
            }
            String sb2 = sb.toString();
            if (sb2.length() == 0) {
                f = 0;
                return "";
            }
            String substring = sb2.substring(0, sb2.length() - 1);
            g = substring;
            return substring;
        } catch (Throwable unused3) {
            return "";
        }
    }

    private static List<ScanResult> a(List<ScanResult> list) {
        int size = list.size();
        for (int i2 = 0; i2 < size - 1; i2++) {
            for (int i3 = 1; i3 < size - i2; i3++) {
                int i4 = i3 - 1;
                if (list.get(i4).level > list.get(i3).level) {
                    ScanResult scanResult = list.get(i4);
                    list.set(i4, list.get(i3));
                    list.set(i3, scanResult);
                }
            }
        }
        return list;
    }

    public static void a(String str) {
        i = str;
    }

    public static String b() {
        if (!TextUtils.isEmpty(d)) {
            return d;
        }
        String b2 = a.b();
        d = b2;
        return b2;
    }

    public static String b(Context context) {
        try {
            return C(context);
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    private static boolean b(Context context, String str) {
        return context != null && context.checkCallingOrSelfPermission(str) == 0;
    }

    public static String c(Context context) {
        if (Build.VERSION.SDK_INT >= 29) {
            return "";
        }
        try {
            String x = x(context);
            if (x != null && x.length() >= 5) {
                return x.substring(3, 5);
            }
            return "";
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    public static void c() {
        try {
            if (Build.VERSION.SDK_INT > 14) {
                TrafficStats.class.getDeclaredMethod("setThreadStatsTag", Integer.TYPE).invoke(null, 40964);
            }
        } catch (Throwable unused) {
        }
    }

    public static int d(Context context) {
        try {
            return F(context);
        } catch (Throwable th) {
            th.printStackTrace();
            return -1;
        }
    }

    public static long d() {
        long blockCount;
        long blockCount2;
        if (r != 0) {
            return r;
        }
        try {
            StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
            StatFs statFs2 = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
            if (Build.VERSION.SDK_INT >= 18) {
                blockCount = (statFs.getBlockCountLong() * statFs.getBlockSizeLong()) / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED;
                blockCount2 = (statFs2.getBlockCountLong() * statFs2.getBlockSizeLong()) / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED;
            } else {
                blockCount = (statFs.getBlockCount() * statFs.getBlockSize()) / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED;
                blockCount2 = (statFs2.getBlockCount() * statFs2.getBlockSize()) / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED;
            }
            r = blockCount + blockCount2;
        } catch (Throwable unused) {
        }
        return r;
    }

    public static int e(Context context) {
        try {
            return D(context);
        } catch (Throwable th) {
            th.printStackTrace();
            return -1;
        }
    }

    public static String e() {
        if (!TextUtils.isEmpty(t)) {
            return t;
        }
        String property = System.getProperty("os.arch");
        t = property;
        return property;
    }

    private static String f() {
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.getName().equalsIgnoreCase("wlan0")) {
                    byte[] hardwareAddress = Build.VERSION.SDK_INT >= 9 ? networkInterface.getHardwareAddress() : null;
                    if (hardwareAddress == null) {
                        return "";
                    }
                    StringBuilder sb = new StringBuilder();
                    for (byte b2 : hardwareAddress) {
                        String upperCase = Integer.toHexString(b2 & 255).toUpperCase();
                        if (upperCase.length() == 1) {
                            sb.append(AmapLoc.RESULT_TYPE_GPS);
                        }
                        sb.append(upperCase);
                        sb.append(":");
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
        try {
            return B(context);
        } catch (Throwable th) {
            th.printStackTrace();
            return "";
        }
    }

    public static String g(Context context) {
        if (a != null && !"".equals(a)) {
            return a;
        }
        if (b(context, ad.c("WYW5kcm9pZC5wZXJtaXNzaW9uLldSSVRFX1NFVFRJTkdT"))) {
            a = Settings.System.getString(context.getContentResolver(), "mqBRboGZkQPcAkyk");
        }
        if (a != null && !"".equals(a)) {
            return a;
        }
        try {
            a = A(context);
        } catch (Throwable unused) {
        }
        return a == null ? "" : a;
    }

    public static String h(Context context) {
        if (Build.VERSION.SDK_INT >= 29) {
            return "";
        }
        if (!TextUtils.isEmpty(l)) {
            return l;
        }
        if (!b(context, ad.c("WYW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfUEhPTkVfU1RBVEU="))) {
            return "";
        }
        if (Build.VERSION.SDK_INT >= 26) {
            return (String) ad.a(Build.class, "MZ2V0U2VyaWFs", (Class<?>[]) new Class[0]).invoke(Build.class, new Object[0]);
        }
        if (Build.VERSION.SDK_INT >= 9) {
            l = Build.SERIAL;
        }
        return l == null ? "" : l;
    }

    public static String i(Context context) {
        if (!TextUtils.isEmpty(k)) {
            return k;
        }
        try {
            String string = Settings.Secure.getString(context.getContentResolver(), ad.c(new String(al.a(13))));
            k = string;
            return string == null ? "" : k;
        } catch (Throwable unused) {
            return k;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String j(Context context) {
        WifiManager wifiManager;
        if (context == null) {
            return "";
        }
        try {
            return (b(context, ad.c("WYW5kcm9pZC5wZXJtaXNzaW9uLkFDQ0VTU19XSUZJX1NUQVRF")) && (wifiManager = (WifiManager) context.getSystemService("wifi")) != null && wifiManager.isWifiEnabled()) ? wifiManager.getConnectionInfo().getBSSID() : "";
        } catch (Throwable unused) {
            return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String k(Context context) {
        StringBuilder sb = new StringBuilder();
        if (context != null) {
            if (b(context, ad.c("WYW5kcm9pZC5wZXJtaXNzaW9uLkFDQ0VTU19XSUZJX1NUQVRF"))) {
                WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
                if (wifiManager == null) {
                    return "";
                }
                if (wifiManager.isWifiEnabled()) {
                    List<ScanResult> scanResults = wifiManager.getScanResults();
                    if (scanResults != null && scanResults.size() != 0) {
                        List<ScanResult> a2 = a(scanResults);
                        boolean z = true;
                        for (int i2 = 0; i2 < a2.size() && i2 < 7; i2++) {
                            ScanResult scanResult = a2.get(i2);
                            if (z) {
                                z = false;
                            } else {
                                sb.append(";");
                            }
                            sb.append(scanResult.BSSID);
                        }
                    }
                    return sb.toString();
                }
                return sb.toString();
            }
        }
        return sb.toString();
    }

    public static String l(Context context) {
        if ((m == null || "".equals(m)) && b(context, ad.c("WYW5kcm9pZC5wZXJtaXNzaW9uLkFDQ0VTU19XSUZJX1NUQVRF"))) {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            if (wifiManager == null) {
                return "";
            }
            m = wifiManager.getConnectionInfo().getMacAddress();
            if (ad.c("YMDI6MDA6MDA6MDA6MDA6MDA").equals(m) || ad.c("YMDA6MDA6MDA6MDA6MDA6MDA").equals(m)) {
                m = f();
            }
            return m;
        }
        return m;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String[] m(Context context) {
        if (b(context, ad.c("WYW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfUEhPTkVfU1RBVEU=")) && b(context, ad.c("EYW5kcm9pZC5wZXJtaXNzaW9uLkFDQ0VTU19DT0FSU0VfTE9DQVRJT04="))) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager == null) {
                return new String[]{"", ""};
            }
            CellLocation cellLocation = telephonyManager.getCellLocation();
            if (cellLocation instanceof GsmCellLocation) {
                GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
                int cid = gsmCellLocation.getCid();
                return new String[]{gsmCellLocation.getLac() + "||" + cid, "gsm"};
            }
            if (cellLocation instanceof CdmaCellLocation) {
                CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cellLocation;
                return new String[]{cdmaCellLocation.getSystemId() + "||" + cdmaCellLocation.getNetworkId() + "||" + cdmaCellLocation.getBaseStationId(), "cdma"};
            }
            return new String[]{"", ""};
        }
        return new String[]{"", ""};
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String n(Context context) {
        try {
            TelephonyManager G = G(context);
            if (G == null) {
                return "";
            }
            String networkOperator = G.getNetworkOperator();
            if (!TextUtils.isEmpty(networkOperator) && networkOperator.length() >= 3) {
                return networkOperator.substring(0, 3);
            }
            return "";
        } catch (Throwable unused) {
            return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String o(Context context) {
        try {
            TelephonyManager G = G(context);
            if (G == null) {
                return "";
            }
            String networkOperator = G.getNetworkOperator();
            if (!TextUtils.isEmpty(networkOperator) && networkOperator.length() >= 3) {
                return networkOperator.substring(3);
            }
            return "";
        } catch (Throwable unused) {
            return "";
        }
    }

    public static int p(Context context) {
        try {
            return F(context);
        } catch (Throwable unused) {
            return -1;
        }
    }

    public static int q(Context context) {
        try {
            return D(context);
        } catch (Throwable unused) {
            return -1;
        }
    }

    public static NetworkInfo r(Context context) {
        ConnectivityManager E;
        if (b(context, ad.c("AYW5kcm9pZC5wZXJtaXNzaW9uLkFDQ0VTU19ORVRXT1JLX1NUQVRF")) && (E = E(context)) != null) {
            return E.getActiveNetworkInfo();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String s(Context context) {
        try {
            NetworkInfo r2 = r(context);
            if (r2 == null) {
                return null;
            }
            return r2.getExtraInfo();
        } catch (Throwable unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String t(Context context) {
        StringBuilder sb;
        if (n != null && !"".equals(n)) {
            return n;
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        if (windowManager == null) {
            return "";
        }
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int i2 = displayMetrics.widthPixels;
        int i3 = displayMetrics.heightPixels;
        if (i3 > i2) {
            sb = new StringBuilder();
            sb.append(i2);
            sb.append("*");
            sb.append(i3);
        } else {
            sb = new StringBuilder();
            sb.append(i3);
            sb.append("*");
            sb.append(i2);
        }
        n = sb.toString();
        return n;
    }

    public static String u(Context context) {
        if (Build.VERSION.SDK_INT >= 29) {
            return "";
        }
        if ((o == null || "".equals(o)) && b(context, ad.c("WYW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfUEhPTkVfU1RBVEU="))) {
            TelephonyManager G = G(context);
            if (G == null) {
                return "";
            }
            Method a2 = ad.a(G.getClass(), "QZ2V0RGV2aWNlSWQ", (Class<?>[]) new Class[0]);
            if (Build.VERSION.SDK_INT >= 26) {
                a2 = ad.a(G.getClass(), "QZ2V0SW1laQ==", (Class<?>[]) new Class[0]);
            }
            if (a2 != null) {
                o = (String) a2.invoke(G, new Object[0]);
            }
            if (o == null) {
                o = "";
            }
            return o;
        }
        return o;
    }

    public static String v(Context context) {
        return u(context) + "#" + a(context);
    }

    public static String w(Context context) {
        if (Build.VERSION.SDK_INT >= 29) {
            return "";
        }
        if ((p == null || "".equals(p)) && b(context, ad.c("WYW5kcm9pZC5wZXJtaXNzaW9uLlJFQURfUEhPTkVfU1RBVEU="))) {
            TelephonyManager G = G(context);
            if (G == null) {
                return "";
            }
            if (Build.VERSION.SDK_INT >= 26) {
                Method a2 = ad.a(G.getClass(), "QZ2V0TWVpZA==", (Class<?>[]) new Class[0]);
                if (a2 != null) {
                    p = (String) a2.invoke(G, new Object[0]);
                }
                if (p == null) {
                    p = "";
                }
            }
            return p;
        }
        return p;
    }

    public static String x(Context context) {
        try {
            return B(context);
        } catch (Throwable unused) {
            return "";
        }
    }

    public static int y(Context context) {
        if (s != 0) {
            return s;
        }
        int i2 = 0;
        if (Build.VERSION.SDK_INT >= 16) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            if (activityManager == null) {
                return 0;
            }
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            activityManager.getMemoryInfo(memoryInfo);
            i2 = (int) (memoryInfo.totalMem / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
        } else {
            BufferedReader bufferedReader = null;
            try {
                BufferedReader bufferedReader2 = new BufferedReader(new FileReader(new File("/proc/meminfo")));
                try {
                    int intValue = Integer.valueOf(bufferedReader2.readLine().split("\\s+")[1]).intValue();
                    try {
                        bufferedReader2.close();
                    } catch (IOException unused) {
                    }
                    i2 = intValue;
                } catch (Throwable unused2) {
                    bufferedReader = bufferedReader2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException unused3) {
                        }
                    }
                    int i3 = i2 / 1024;
                    s = i3;
                    return i3;
                }
            } catch (Throwable th) {
                th = th;
            }
        }
        int i32 = i2 / 1024;
        s = i32;
        return i32;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String z(Context context) {
        try {
            return C(context);
        } catch (Throwable unused) {
            return "";
        }
    }
}
