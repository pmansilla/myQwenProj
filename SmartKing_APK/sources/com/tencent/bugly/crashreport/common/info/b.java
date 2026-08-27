package com.tencent.bugly.crashreport.common.info;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.os.StatFs;
import android.provider.Settings;
import android.support.v4.os.EnvironmentCompat;
import android.telephony.TelephonyManager;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import me.panpf.sketch.uri.FileUriModel;

/* compiled from: BUGLY */
/* loaded from: classes2.dex */
public class b {
    private static final String[] a = {"/su", "/su/bin/su", "/sbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/data/local/su", "/system/xbin/su", "/system/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/system/bin/cufsdosck", "/system/xbin/cufsdosck", "/system/bin/cufsmgr", "/system/xbin/cufsmgr", "/system/bin/cufaevdd", "/system/xbin/cufaevdd", "/system/bin/conbb", "/system/xbin/conbb"};
    private static final String[] b = {"com.ami.duosupdater.ui", "com.ami.launchmetro", "com.ami.syncduosservices", "com.bluestacks.home", "com.bluestacks.windowsfilemanager", "com.bluestacks.settings", "com.bluestacks.bluestackslocationprovider", "com.bluestacks.appsettings", "com.bluestacks.bstfolder", "com.bluestacks.BstCommandProcessor", "com.bluestacks.s2p", "com.bluestacks.setup", "com.kaopu001.tiantianserver", "com.kpzs.helpercenter", "com.kaopu001.tiantianime", "com.android.development_settings", "com.android.development", "com.android.customlocale2", "com.genymotion.superuser", "com.genymotion.clipboardproxy", "com.uc.xxzs.keyboard", "com.uc.xxzs", "com.blue.huang17.agent", "com.blue.huang17.launcher", "com.blue.huang17.ime", "com.microvirt.guide", "com.microvirt.market", "com.microvirt.memuime", "cn.itools.vm.launcher", "cn.itools.vm.proxy", "cn.itools.vm.softkeyboard", "cn.itools.avdmarket", "com.syd.IME", "com.bignox.app.store.hd", "com.bignox.launcher", "com.bignox.app.phone", "com.bignox.app.noxservice", "com.android.noxpush", "com.haimawan.push", "me.haima.helpcenter", "com.windroy.launcher", "com.windroy.superuser", "com.windroy.launcher", "com.windroy.ime", "com.android.flysilkworm", "com.android.emu.inputservice", "com.tiantian.ime", "com.microvirt.launcher", "me.le8.androidassist", "com.vphone.helper", "com.vphone.launcher", "com.duoyi.giftcenter.giftcenter"};
    private static final String[] c = {"/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq", "/system/lib/libc_malloc_debug_qemu.so", "/sys/qemu_trace", "/system/bin/qemu-props", "/dev/socket/qemud", "/dev/qemu_pipe", "/dev/socket/baseband_genyd", "/dev/socket/genyd"};

    public static String a() {
        try {
            return Build.MODEL;
        } catch (Throwable th) {
            if (x.a(th)) {
                return "fail";
            }
            th.printStackTrace();
            return "fail";
        }
    }

    public static String a(Context context) {
        String str = "fail";
        if (context == null) {
            return "fail";
        }
        try {
            String string = Settings.Secure.getString(context.getContentResolver(), "android_id");
            if (string == null) {
                return "null";
            }
            try {
                return string.toLowerCase();
            } catch (Throwable th) {
                str = string;
                th = th;
                if (x.a(th)) {
                    return str;
                }
                x.a("Failed to get Android ID.", new Object[0]);
                return str;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:4:0x004f, code lost:
    
        r0 = java.lang.System.getProperty("os.arch");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String a(android.content.Context r3, boolean r4) {
        /*
            r0 = 0
            if (r4 == 0) goto L4d
            java.lang.String r4 = "ro.product.cpu.abilist"
            java.lang.String r4 = com.tencent.bugly.proguard.z.a(r3, r4)     // Catch: java.lang.Throwable -> L4b
            boolean r1 = com.tencent.bugly.proguard.z.a(r4)     // Catch: java.lang.Throwable -> L4b
            if (r1 != 0) goto L17
            java.lang.String r1 = "fail"
            boolean r1 = r4.equals(r1)     // Catch: java.lang.Throwable -> L4b
            if (r1 == 0) goto L1d
        L17:
            java.lang.String r4 = "ro.product.cpu.abi"
            java.lang.String r4 = com.tencent.bugly.proguard.z.a(r3, r4)     // Catch: java.lang.Throwable -> L4b
        L1d:
            boolean r3 = com.tencent.bugly.proguard.z.a(r4)     // Catch: java.lang.Throwable -> L4b
            if (r3 != 0) goto L4d
            java.lang.String r3 = "fail"
            boolean r3 = r4.equals(r3)     // Catch: java.lang.Throwable -> L4b
            if (r3 == 0) goto L2c
            goto L4d
        L2c:
            java.lang.Class<com.tencent.bugly.crashreport.common.info.b> r3 = com.tencent.bugly.crashreport.common.info.b.class
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L4b
            java.lang.String r1 = "ABI list: "
            r0.<init>(r1)     // Catch: java.lang.Throwable -> L4b
            r0.append(r4)     // Catch: java.lang.Throwable -> L4b
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Throwable -> L4b
            r1 = 0
            java.lang.Object[] r2 = new java.lang.Object[r1]     // Catch: java.lang.Throwable -> L4b
            com.tencent.bugly.proguard.x.b(r3, r0, r2)     // Catch: java.lang.Throwable -> L4b
            java.lang.String r3 = ","
            java.lang.String[] r3 = r4.split(r3)     // Catch: java.lang.Throwable -> L4b
            r0 = r3[r1]     // Catch: java.lang.Throwable -> L4b
            goto L4d
        L4b:
            r3 = move-exception
            goto L62
        L4d:
            if (r0 != 0) goto L55
            java.lang.String r3 = "os.arch"
            java.lang.String r0 = java.lang.System.getProperty(r3)     // Catch: java.lang.Throwable -> L4b
        L55:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L4b
            r3.<init>()     // Catch: java.lang.Throwable -> L4b
            r3.append(r0)     // Catch: java.lang.Throwable -> L4b
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Throwable -> L4b
            return r3
        L62:
            boolean r4 = com.tencent.bugly.proguard.x.a(r3)
            if (r4 != 0) goto L6b
            r3.printStackTrace()
        L6b:
            java.lang.String r3 = "fail"
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.common.info.b.a(android.content.Context, boolean):java.lang.String");
    }

    public static String b() {
        try {
            return Build.VERSION.RELEASE;
        } catch (Throwable th) {
            if (x.a(th)) {
                return "fail";
            }
            th.printStackTrace();
            return "fail";
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:16:0x0031. Please report as an issue. */
    public static String b(Context context) {
        NetworkInfo activeNetworkInfo;
        TelephonyManager telephonyManager;
        String str = EnvironmentCompat.MEDIA_UNKNOWN;
        try {
            activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        } catch (Exception e) {
            if (!x.a(e)) {
                e.printStackTrace();
            }
        }
        if (activeNetworkInfo == null) {
            return null;
        }
        if (activeNetworkInfo.getType() == 1) {
            return "WIFI";
        }
        if (activeNetworkInfo.getType() == 0 && (telephonyManager = (TelephonyManager) context.getSystemService("phone")) != null) {
            int networkType = telephonyManager.getNetworkType();
            switch (networkType) {
                case 1:
                    return "GPRS";
                case 2:
                    return "EDGE";
                case 3:
                    return "UMTS";
                case 4:
                    return "CDMA";
                case 5:
                    return "EVDO_0";
                case 6:
                    return "EVDO_A";
                case 7:
                    return "1xRTT";
                case 8:
                    return "HSDPA";
                case 9:
                    return "HSUPA";
                case 10:
                    return "HSPA";
                case 11:
                    return "iDen";
                case 12:
                    return "EVDO_B";
                case 13:
                    return "LTE";
                case 14:
                    return "eHRPD";
                case 15:
                    return "HSPA+";
                default:
                    str = "MOBILE(" + networkType + SQLBuilder.PARENTHESES_RIGHT;
                    break;
            }
        }
        return str;
    }

    public static int c() {
        try {
            return Build.VERSION.SDK_INT;
        } catch (Throwable th) {
            if (x.a(th)) {
                return -1;
            }
            th.printStackTrace();
            return -1;
        }
    }

    public static String c(Context context) {
        String a2 = z.a(context, "ro.miui.ui.version.name");
        if (!z.a(a2) && !a2.equals("fail")) {
            return "XiaoMi/MIUI/" + a2;
        }
        String a3 = z.a(context, "ro.build.version.emui");
        if (!z.a(a3) && !a3.equals("fail")) {
            return "HuaWei/EMOTION/" + a3;
        }
        String a4 = z.a(context, "ro.lenovo.series");
        if (!z.a(a4) && !a4.equals("fail")) {
            return "Lenovo/VIBE/" + z.a(context, "ro.build.version.incremental");
        }
        String a5 = z.a(context, "ro.build.nubia.rom.name");
        if (!z.a(a5) && !a5.equals("fail")) {
            return "Zte/NUBIA/" + a5 + "_" + z.a(context, "ro.build.nubia.rom.code");
        }
        String a6 = z.a(context, "ro.meizu.product.model");
        if (!z.a(a6) && !a6.equals("fail")) {
            return "Meizu/FLYME/" + z.a(context, "ro.build.display.id");
        }
        String a7 = z.a(context, "ro.build.version.opporom");
        if (!z.a(a7) && !a7.equals("fail")) {
            return "Oppo/COLOROS/" + a7;
        }
        String a8 = z.a(context, "ro.vivo.os.build.display.id");
        if (!z.a(a8) && !a8.equals("fail")) {
            return "vivo/FUNTOUCH/" + a8;
        }
        String a9 = z.a(context, "ro.aa.romver");
        if (!z.a(a9) && !a9.equals("fail")) {
            return "htc/" + a9 + FileUriModel.SCHEME + z.a(context, "ro.build.description");
        }
        String a10 = z.a(context, "ro.lewa.version");
        if (!z.a(a10) && !a10.equals("fail")) {
            return "tcl/" + a10 + FileUriModel.SCHEME + z.a(context, "ro.build.display.id");
        }
        String a11 = z.a(context, "ro.gn.gnromvernumber");
        if (!z.a(a11) && !a11.equals("fail")) {
            return "amigo/" + a11 + FileUriModel.SCHEME + z.a(context, "ro.build.display.id");
        }
        String a12 = z.a(context, "ro.build.tyd.kbstyle_version");
        if (!z.a(a12) && !a12.equals("fail")) {
            return "dido/" + a12;
        }
        return z.a(context, "ro.build.fingerprint") + FileUriModel.SCHEME + z.a(context, "ro.build.rom.id");
    }

    public static long d() {
        try {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            return statFs.getBlockCount() * statFs.getBlockSize();
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            return -1L;
        }
    }

    public static String d(Context context) {
        return z.a(context, "ro.board.platform");
    }

    public static long e() {
        try {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            return statFs.getAvailableBlocks() * statFs.getBlockSize();
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            return -1L;
        }
    }

    public static boolean e(Context context) {
        int i;
        if (g(context) != null) {
            return true;
        }
        ArrayList arrayList = new ArrayList();
        while (i < c.length) {
            if (i == 0) {
                i = new File(c[i]).exists() ? i + 1 : 0;
                arrayList.add(Integer.valueOf(i));
            } else {
                if (!new File(c[i]).exists()) {
                }
                arrayList.add(Integer.valueOf(i));
            }
        }
        return (arrayList.isEmpty() ? null : arrayList.toString()) != null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r0v18 */
    /* JADX WARN: Type inference failed for: r0v2 */
    /* JADX WARN: Type inference failed for: r0v5, types: [java.io.BufferedReader] */
    public static long f() {
        FileReader fileReader;
        Throwable th;
        Throwable th2;
        BufferedReader bufferedReader;
        ?? r0 = "/proc/meminfo";
        try {
            try {
                fileReader = new FileReader("/proc/meminfo");
            } catch (Throwable th3) {
                th = th3;
            }
        } catch (Throwable th4) {
            fileReader = null;
            th = th4;
            r0 = 0;
        }
        try {
            bufferedReader = new BufferedReader(fileReader, 2048);
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    long parseLong = Long.parseLong(readLine.split(":\\s+", 2)[1].toLowerCase().replace("kb", "").trim()) << 10;
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        if (!x.a(e)) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        fileReader.close();
                    } catch (IOException e2) {
                        if (!x.a(e2)) {
                            e2.printStackTrace();
                        }
                    }
                    return parseLong;
                }
                try {
                    bufferedReader.close();
                } catch (IOException e3) {
                    if (!x.a(e3)) {
                        e3.printStackTrace();
                    }
                }
                try {
                    fileReader.close();
                    return -1L;
                } catch (IOException e4) {
                    if (x.a(e4)) {
                        return -1L;
                    }
                    e4.printStackTrace();
                    return -1L;
                }
            } catch (Throwable th5) {
                th2 = th5;
                if (!x.a(th2)) {
                    th2.printStackTrace();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e5) {
                        if (!x.a(e5)) {
                            e5.printStackTrace();
                        }
                    }
                }
                if (fileReader == null) {
                    return -2L;
                }
                try {
                    fileReader.close();
                    return -2L;
                } catch (IOException e6) {
                    if (x.a(e6)) {
                        return -2L;
                    }
                    e6.printStackTrace();
                    return -2L;
                }
            }
        } catch (Throwable th6) {
            th2 = th6;
            bufferedReader = null;
        }
    }

    public static boolean f(Context context) {
        return (((h(context) | p()) | q()) | o()) > 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:107:0x014e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:114:0x013e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static long g() {
        /*
            Method dump skipped, instructions count: 349
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.common.info.b.g():long");
    }

    private static String g(Context context) {
        PackageManager packageManager = context.getPackageManager();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < b.length; i++) {
            try {
                packageManager.getPackageInfo(b[i], 1);
                arrayList.add(Integer.valueOf(i));
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return arrayList.toString();
    }

    private static int h(Context context) {
        int i;
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getInstallerPackageName("de.robv.android.xposed.installer");
            i = 1;
        } catch (Exception unused) {
            i = 0;
        }
        try {
            packageManager.getInstallerPackageName("com.saurik.substrate");
            return i | 2;
        } catch (Exception unused2) {
            return i;
        }
    }

    public static long h() {
        if (!n()) {
            return 0L;
        }
        try {
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            return statFs.getBlockCount() * statFs.getBlockSize();
        } catch (Throwable th) {
            if (x.a(th)) {
                return -2L;
            }
            th.printStackTrace();
            return -2L;
        }
    }

    public static long i() {
        if (!n()) {
            return 0L;
        }
        try {
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            return statFs.getAvailableBlocks() * statFs.getBlockSize();
        } catch (Throwable th) {
            if (x.a(th)) {
                return -2L;
            }
            th.printStackTrace();
            return -2L;
        }
    }

    public static String j() {
        return "";
    }

    public static String k() {
        try {
            return Build.BRAND;
        } catch (Throwable th) {
            if (x.a(th)) {
                return "fail";
            }
            th.printStackTrace();
            return "fail";
        }
    }

    public static boolean l() {
        boolean z;
        String[] strArr = a;
        int length = strArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                z = false;
                break;
            }
            if (new File(strArr[i]).exists()) {
                z = true;
                break;
            }
            i++;
        }
        return (Build.TAGS != null && Build.TAGS.contains("test-keys")) || z;
    }

    public static boolean m() {
        double maxMemory = Runtime.getRuntime().maxMemory();
        Double.isNaN(maxMemory);
        float f = (float) (maxMemory / 1048576.0d);
        double d = Runtime.getRuntime().totalMemory();
        Double.isNaN(d);
        float f2 = (float) (d / 1048576.0d);
        float f3 = f - f2;
        x.c("maxMemory : %f", Float.valueOf(f));
        x.c("totalMemory : %f", Float.valueOf(f2));
        x.c("freeMemory : %f", Float.valueOf(f3));
        return f3 < 10.0f;
    }

    private static boolean n() {
        try {
            return Environment.getExternalStorageState().equals("mounted");
        } catch (Throwable th) {
            if (x.a(th)) {
                return false;
            }
            th.printStackTrace();
            return false;
        }
    }

    private static int o() {
        try {
            Method method = Class.forName("android.app.ActivityManagerNative").getMethod("getDefault", new Class[0]);
            method.setAccessible(true);
            return method.invoke(null, new Object[0]).getClass().getName().startsWith("$Proxy") ? 256 : 0;
        } catch (Exception unused) {
            return 256;
        }
    }

    private static int p() {
        try {
            throw new Exception("detect hook");
        } catch (Exception e) {
            int i = 0;
            int i2 = 0;
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                if (stackTraceElement.getClassName().equals("de.robv.android.xposed.XposedBridge") && stackTraceElement.getMethodName().equals("main")) {
                    i |= 4;
                }
                if (stackTraceElement.getClassName().equals("de.robv.android.xposed.XposedBridge") && stackTraceElement.getMethodName().equals("handleHookedMethod")) {
                    i |= 8;
                }
                if (stackTraceElement.getClassName().equals("com.saurik.substrate.MS$2") && stackTraceElement.getMethodName().equals("invoked")) {
                    i |= 16;
                }
                if (stackTraceElement.getClassName().equals("com.android.internal.os.ZygoteInit") && (i2 = i2 + 1) == 2) {
                    i |= 32;
                }
            }
            return i;
        }
    }

    private static int q() {
        BufferedReader bufferedReader;
        IOException e;
        UnsupportedEncodingException e2;
        FileNotFoundException e3;
        int i = 0;
        BufferedReader bufferedReader2 = null;
        try {
        } catch (Throwable th) {
            th = th;
        }
        try {
            try {
                HashSet hashSet = new HashSet();
                bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/" + Process.myPid() + "/maps"), "utf-8"));
                while (true) {
                    try {
                        String readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            break;
                        }
                        if (readLine.endsWith(".so") || readLine.endsWith(".jar")) {
                            hashSet.add(readLine.substring(readLine.lastIndexOf(SQLBuilder.BLANK) + 1));
                        }
                    } catch (FileNotFoundException e4) {
                        e3 = e4;
                        e3.printStackTrace();
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                        return i;
                    } catch (UnsupportedEncodingException e5) {
                        e2 = e5;
                        e2.printStackTrace();
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                        return i;
                    } catch (IOException e6) {
                        e = e6;
                        e.printStackTrace();
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                        return i;
                    }
                }
                Iterator it = hashSet.iterator();
                while (it.hasNext()) {
                    Object next = it.next();
                    if (((String) next).toLowerCase().contains("xposed")) {
                        i |= 64;
                    }
                    if (((String) next).contains("com.saurik.substrate")) {
                        i |= 128;
                    }
                }
                bufferedReader.close();
            } catch (IOException e7) {
                e7.printStackTrace();
            }
        } catch (FileNotFoundException e8) {
            bufferedReader = null;
            e3 = e8;
        } catch (UnsupportedEncodingException e9) {
            bufferedReader = null;
            e2 = e9;
        } catch (IOException e10) {
            bufferedReader = null;
            e = e10;
        } catch (Throwable th2) {
            th = th2;
            if (0 != 0) {
                try {
                    bufferedReader2.close();
                } catch (IOException e11) {
                    e11.printStackTrace();
                }
            }
            throw th;
        }
        return i;
    }
}
