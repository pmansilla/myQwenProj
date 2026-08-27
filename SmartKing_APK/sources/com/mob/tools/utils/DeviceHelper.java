package com.mob.tools.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.UiModeManager;
import android.app.WallpaperManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.location.Location;
import android.net.Proxy;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.PowerManager;
import android.os.Process;
import android.os.StatFs;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.os.EnvironmentCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Size;
import android.view.View;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import basecamera.module.lib.CameraInterface;
import com.alibaba.fastjson.asm.Opcodes;
import com.amap.location.common.model.AmapLoc;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import com.autonavi.amap.mapcore.AeUtil;
import com.litesuits.orm.db.assit.SQLBuilder;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.mob.tools.MobLog;
import com.mob.tools.log.NLog;
import com.mob.tools.utils.BHelper;
import com.mob.tools.utils.ReflectHelper;
import com.sun.mail.imap.IMAPStore;
import io.reactivex.annotations.SchedulerSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.panpf.sketch.uri.FileUriModel;
import org.apache.commons.lang.time.DateUtils;

/* loaded from: classes2.dex */
public class DeviceHelper {
    private static DeviceHelper deviceHelper;
    private String advertiseID;
    private String bufModel;
    private String bufUiVersion;
    private Context context;
    private String ime;
    private String[] invalidMacList;
    private Boolean isSmlt;
    private List<Object> sActList;
    private String srno;
    private String wfMc;
    private String cacheDeviceKey = null;
    private volatile boolean hasSdcardWritePermission = false;
    private String ln = "-1";
    private String swVer = "-1";
    private int sActCnt = -1;
    private ArrayList<String> bufIm = new ArrayList<>();
    private ArrayList<String> bufImp = new ArrayList<>();
    private HashMap<String, String> bufMcs = new HashMap<>();
    private BVS bvs = new BVS();

    /* loaded from: classes2.dex */
    public interface BtScanCallback {
        void onScan(ArrayList<HashMap<String, Object>> arrayList);
    }

    /* loaded from: classes2.dex */
    public static class BtWatcher {
        protected void onBtConnectionChanged(boolean z, HashMap<String, Object> hashMap) {
        }

        protected void onBtDisabled() {
        }

        protected void onBtEnabled() {
        }

        protected void onDeviceConnected(HashMap<String, Object> hashMap) {
        }

        protected void onDeviceDisconnected(HashMap<String, Object> hashMap) {
        }
    }

    /* loaded from: classes2.dex */
    private class GSConnection implements ServiceConnection {
        boolean got;
        private final BlockingQueue<IBinder> iBinders;

        private GSConnection() {
            this.got = false;
            this.iBinders = new LinkedBlockingQueue();
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                this.iBinders.put(iBinder);
            } catch (Throwable th) {
                MobLog.getInstance().w(th);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
        }

        public IBinder takeBinder() throws InterruptedException {
            if (this.got) {
                throw new IllegalStateException();
            }
            this.got = true;
            return this.iBinders.poll(1500L, TimeUnit.MILLISECONDS);
        }
    }

    private DeviceHelper(Context context) {
        this.context = context.getApplicationContext();
    }

    private String byteToHex(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            sb.append(String.format("%02x:", Byte.valueOf(b)));
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private boolean checkMacIsValid(String str) {
        if (str != null) {
            try {
            } catch (Throwable th) {
                MobLog.getInstance().d(th);
            }
            if (!TextUtils.isEmpty(str.trim())) {
                if (this.invalidMacList == null) {
                    this.invalidMacList = getInvalidMacList();
                }
                String[] strArr = this.invalidMacList;
                if (strArr == null) {
                    strArr = new String[]{Strings.getString(70), Strings.getString(71)};
                }
                for (String str2 : strArr) {
                    if (str2 != null && str.trim().startsWith(str2.trim())) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private boolean checkRootApp() {
        try {
            for (String str : new String[]{"com.noshufou.android.su", "com.noshufou.android.su.elite", "eu.chainfire.supersu", "com.koushikdutta.superuser", "com.thirdparty.superuser", "com.yellowes.su", "com.topjohnwu.magisk", "com.koushikdutta.rommanager", "com.koushikdutta.rommanager.license", "com.dimonvideo.luckypatcher", "com.chelpus.lackypatch", "com.ramdroid.appquarantine", "com.ramdroid.appquarantinepro", "com.android.vending.billing.InAppBillingService.COIN", "com.chelpus.luckypatcher"}) {
                if (deviceHelper.isPackageInstalled(str)) {
                    return true;
                }
            }
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
        return false;
    }

    private boolean checkRootFile() {
        try {
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
        if (new File("/system/app/Superuser.apk").exists()) {
            return true;
        }
        String[] strArr = {"/data/local/", "/data/local/bin/", "/data/local/xbin/", "/sbin/", "/su/bin/", "/system/bin/", "/system/bin/.ext/", "/system/bin/failsafe/", "/system/sd/xbin/", "/system/usr/we-need-root/", "/system/xbin/", "/system/sbin/", "/vendor/bin/", "/cache", "/data", "/dev"};
        for (String str : strArr) {
            if (new File(str, "su").exists()) {
                return true;
            }
        }
        for (String str2 : strArr) {
            if (new File(str2, "busybox").exists()) {
                return true;
            }
        }
        for (String str3 : strArr) {
            if (new File(str3, "magisk").exists()) {
                return true;
            }
        }
        return false;
    }

    private boolean checkRootProp() {
        String[] split;
        try {
            InputStream invokeRuntimeExec = invokeRuntimeExec(new String[]{"getprop"});
            if (invokeRuntimeExec != null && (split = new Scanner(invokeRuntimeExec).useDelimiter("\\A").next().split("\n")) != null) {
                HashMap hashMap = new HashMap();
                hashMap.put("ro.debuggable", AmapLoc.RESULT_TYPE_WIFI_ONLY);
                hashMap.put("ro.secure", AmapLoc.RESULT_TYPE_GPS);
                for (String str : split) {
                    for (String str2 : hashMap.keySet()) {
                        if (str != null && str.contains(str2)) {
                            if (str.contains("[" + hashMap.get(str2) + "]")) {
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
        return false;
    }

    private boolean checkRootRw() {
        String[] split;
        try {
            InputStream invokeRuntimeExec = invokeRuntimeExec(new String[]{"mount"});
            if (invokeRuntimeExec != null && (split = new Scanner(invokeRuntimeExec).useDelimiter("\\A").next().split("\n")) != null) {
                String[] strArr = {"/system", "/system/bin", "/system/sbin", "/system/xbin", "/vendor/bin", "/sbin", "/etc"};
                for (String str : split) {
                    String[] split2 = str.split(SQLBuilder.BLANK);
                    if (split2.length >= 4) {
                        String str2 = split2[1];
                        String str3 = split2[3];
                        for (String str4 : strArr) {
                            if (str2 != null && str2.equalsIgnoreCase(str4)) {
                                for (String str5 : str3.split(",")) {
                                    if (str5 != null && str5.equalsIgnoreCase("rw")) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
        return false;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(9:1|(2:2|3)|(2:5|(6:7|8|9|(2:11|(2:13|(1:18)(1:16)))|21|(1:18)(1:19)))|24|8|9|(0)|21|(0)(0)) */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0031 A[Catch: Throwable -> 0x0043, TRY_LEAVE, TryCatch #0 {Throwable -> 0x0043, blocks: (B:9:0x0023, B:11:0x0031), top: B:8:0x0023 }] */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0046 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:19:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean checkRootSu() {
        /*
            r6 = this;
            r0 = 1
            r1 = 0
            java.lang.String r2 = "which"
            java.lang.String r3 = "su"
            java.lang.String[] r2 = new java.lang.String[]{r2, r3}     // Catch: java.lang.Throwable -> L22
            java.io.InputStream r2 = r6.invokeRuntimeExec(r2)     // Catch: java.lang.Throwable -> L22
            if (r2 == 0) goto L22
            java.io.BufferedReader r3 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L22
            java.io.InputStreamReader r4 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L22
            r4.<init>(r2)     // Catch: java.lang.Throwable -> L22
            r3.<init>(r4)     // Catch: java.lang.Throwable -> L22
            java.lang.String r2 = r3.readLine()     // Catch: java.lang.Throwable -> L22
            if (r2 == 0) goto L22
            r2 = 1
            goto L23
        L22:
            r2 = 0
        L23:
            java.lang.String r3 = "/system/xbin/which"
            java.lang.String r4 = "su"
            java.lang.String[] r3 = new java.lang.String[]{r3, r4}     // Catch: java.lang.Throwable -> L43
            java.io.InputStream r3 = r6.invokeRuntimeExec(r3)     // Catch: java.lang.Throwable -> L43
            if (r3 == 0) goto L43
            java.io.BufferedReader r4 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L43
            java.io.InputStreamReader r5 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L43
            r5.<init>(r3)     // Catch: java.lang.Throwable -> L43
            r4.<init>(r5)     // Catch: java.lang.Throwable -> L43
            java.lang.String r3 = r4.readLine()     // Catch: java.lang.Throwable -> L43
            if (r3 == 0) goto L43
            r3 = 1
            goto L44
        L43:
            r3 = 0
        L44:
            if (r2 != 0) goto L4a
            if (r3 == 0) goto L49
            goto L4a
        L49:
            r0 = 0
        L4a:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.tools.utils.DeviceHelper.checkRootSu():boolean");
    }

    public static Object currentActivityThread() {
        Object run;
        final ReflectHelper.ReflectRunnable<Void, Object> reflectRunnable = new ReflectHelper.ReflectRunnable<Void, Object>() { // from class: com.mob.tools.utils.DeviceHelper.2
            @Override // com.mob.tools.utils.ReflectHelper.ReflectRunnable
            public Object run(Void r3) {
                try {
                    return ReflectHelper.invokeStaticMethod(ReflectHelper.importClass(Strings.getString(31)), Strings.getString(32), new Object[0]);
                } catch (Throwable th) {
                    MobLog.getInstance().w(th);
                    return null;
                }
            }
        };
        if ((Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId() || Build.VERSION.SDK_INT >= 18) && (run = reflectRunnable.run(null)) != null) {
            return run;
        }
        final Object obj = new Object();
        final Object[] objArr = new Object[1];
        synchronized (obj) {
            UIHandler.sendEmptyMessage(0, new Handler.Callback() { // from class: com.mob.tools.utils.DeviceHelper.3
                @Override // android.os.Handler.Callback
                public boolean handleMessage(Message message) {
                    NLog mobLog;
                    synchronized (obj) {
                        try {
                            try {
                                objArr[0] = reflectRunnable.run(null);
                            } catch (Throwable th) {
                                MobLog.getInstance().w(th);
                                try {
                                    obj.notify();
                                } catch (Throwable th2) {
                                    th = th2;
                                    mobLog = MobLog.getInstance();
                                    mobLog.w(th);
                                    return false;
                                }
                            }
                            try {
                                obj.notify();
                            } catch (Throwable th3) {
                                th = th3;
                                mobLog = MobLog.getInstance();
                                mobLog.w(th);
                                return false;
                            }
                        } catch (Throwable th4) {
                            try {
                                obj.notify();
                            } catch (Throwable th5) {
                                MobLog.getInstance().w(th5);
                            }
                            throw th4;
                        }
                    }
                    return false;
                }
            });
            try {
                obj.wait();
            } catch (Throwable th) {
                MobLog.getInstance().w(th);
            }
        }
        return objArr[0];
    }

    private HashMap<String, Object> decryptData(String str, byte[] bArr) {
        try {
            return new Hashon().fromJson(Data.AES128Decode(str, bArr));
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return new HashMap<>();
        }
    }

    private byte[] encryptData(String str, HashMap<String, Object> hashMap) {
        String fromHashMap = new Hashon().fromHashMap(hashMap);
        try {
            return Data.AES128Encode(str, fromHashMap);
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return fromHashMap.getBytes();
        }
    }

    private String genDeviceKey() {
        try {
            return Data.byteToHex(Data.SHA1(getMacAddress() + ":" + getDeviceId() + ":" + getModel()));
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return null;
        }
    }

    private HashMap<String, String> getANS() {
        try {
            return (HashMap) ResHelper.readObjectFromFile(ResHelper.getDataCacheFile(this.context, ".ans").getAbsolutePath());
        } catch (Throwable th) {
            try {
                MobLog.getInstance().w(th);
                ResHelper.getDataCacheFile(this.context, ".ans").delete();
            } catch (Throwable th2) {
                MobLog.getInstance().w(th2);
            }
            return null;
        }
    }

    public static Context getApplication() {
        try {
            Object currentActivityThread = currentActivityThread();
            if (currentActivityThread != null) {
                return (Context) ReflectHelper.invokeInstanceMethod(currentActivityThread, Strings.getString(33), new Object[0]);
            }
            return null;
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return null;
        }
    }

    private String getCurrentNetworkHardwareAddress() throws Throwable {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        if (networkInterfaces == null) {
            return null;
        }
        for (NetworkInterface networkInterface : Collections.list(networkInterfaces)) {
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            if (inetAddresses != null) {
                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        byte[] hardwareAddress = Build.VERSION.SDK_INT >= 9 ? networkInterface.getHardwareAddress() : null;
                        if (hardwareAddress != null) {
                            return byteToHex(hardwareAddress);
                        }
                    }
                }
            }
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0095 A[ADDED_TO_REGION, RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0096  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x007b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0081 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0069 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x003f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String getDeviceKeyWithDuid(java.lang.String r7) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 252
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.tools.utils.DeviceHelper.getDeviceKeyWithDuid(java.lang.String):java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x007c  */
    /* JADX WARN: Removed duplicated region for block: B:14:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0082 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String getHardwareAddressFromShell(java.lang.String r6) {
        /*
            r5 = this;
            r0 = 42
            r1 = 0
            java.lang.String r0 = com.mob.tools.utils.Strings.getString(r0)     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L67
            java.lang.String r0 = com.mob.tools.utils.ReflectHelper.importClass(r0)     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L67
            r2 = 43
            java.lang.String r2 = com.mob.tools.utils.Strings.getString(r2)     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L67
            r3 = 0
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L67
            java.lang.Object r0 = com.mob.tools.utils.ReflectHelper.invokeStaticMethod(r0, r2, r4)     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L67
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L67
            r2.<init>()     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L67
            r4 = 6
            java.lang.String r4 = com.mob.tools.utils.Strings.getString(r4)     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L67
            r2.append(r4)     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L67
            r2.append(r6)     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L67
            r6 = 7
            java.lang.String r6 = com.mob.tools.utils.Strings.getString(r6)     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L67
            r2.append(r6)     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L67
            java.lang.String r6 = r2.toString()     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L67
            r2 = 44
            java.lang.String r2 = com.mob.tools.utils.Strings.getString(r2)     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L67
            r4 = 1
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L67
            r4[r3] = r6     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L67
            java.lang.Object r6 = com.mob.tools.utils.ReflectHelper.invokeInstanceMethod(r0, r2, r4)     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L67
            r0 = 45
            java.lang.String r0 = com.mob.tools.utils.Strings.getString(r0)     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L67
            java.lang.Object[] r2 = new java.lang.Object[r3]     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L67
            java.lang.Object r6 = com.mob.tools.utils.ReflectHelper.invokeInstanceMethod(r6, r0, r2)     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L67
            java.io.InputStream r6 = (java.io.InputStream) r6     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L67
            java.io.InputStreamReader r0 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L67
            r0.<init>(r6)     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L67
            java.io.BufferedReader r6 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L67
            r6.<init>(r0)     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L67
            java.lang.String r0 = r6.readLine()     // Catch: java.lang.Throwable -> L63 java.lang.Throwable -> L7e
            r6.close()     // Catch: java.lang.Throwable -> L76
            goto L76
        L63:
            r0 = move-exception
            goto L69
        L65:
            r0 = move-exception
            goto L80
        L67:
            r0 = move-exception
            r6 = r1
        L69:
            com.mob.tools.log.NLog r2 = com.mob.tools.MobLog.getInstance()     // Catch: java.lang.Throwable -> L7e
            r2.d(r0)     // Catch: java.lang.Throwable -> L7e
            if (r6 == 0) goto L75
            r6.close()     // Catch: java.lang.Throwable -> L75
        L75:
            r0 = r1
        L76:
            boolean r6 = android.text.TextUtils.isEmpty(r0)
            if (r6 == 0) goto L7d
            r0 = r1
        L7d:
            return r0
        L7e:
            r0 = move-exception
            r1 = r6
        L80:
            if (r1 == 0) goto L85
            r1.close()     // Catch: java.lang.Throwable -> L85
        L85:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.tools.utils.DeviceHelper.getHardwareAddressFromShell(java.lang.String):java.lang.String");
    }

    public static synchronized DeviceHelper getInstance(Context context) {
        DeviceHelper deviceHelper2;
        synchronized (DeviceHelper.class) {
            if (deviceHelper == null && context != null) {
                deviceHelper = new DeviceHelper(context);
            }
            deviceHelper2 = deviceHelper;
        }
        return deviceHelper2;
    }

    private String[] getInvalidMacList() {
        ArrayList arrayList;
        try {
            String AES128Decode = Data.AES128Decode(Strings.getString(76), (byte[]) ResHelper.readObjectFromFile(ResHelper.getDataCacheFile(this.context, ".mcli").getPath()));
            if (TextUtils.isEmpty(AES128Decode) || (arrayList = (ArrayList) new Hashon().fromJson(AES128Decode).get("list")) == null || arrayList.size() <= 0) {
                return null;
            }
            return (String[]) arrayList.toArray(new String[0]);
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return null;
        }
    }

    private static List<String> getLauncherPackageNames(Context context) throws Throwable {
        ArrayList arrayList = new ArrayList();
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        Iterator<ResolveInfo> it = packageManager.queryIntentActivities(intent, 65536).iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().activityInfo.packageName);
        }
        return arrayList;
    }

    private String getLocalDeviceKey() throws Throwable {
        File cacheRootFile;
        String str = null;
        if (!getSdcardState()) {
            return null;
        }
        File file = new File(getSdcardPath(), "ShareSDK");
        if (file.exists()) {
            File file2 = new File(file, ".dk");
            if (file2.exists() && (cacheRootFile = ResHelper.getCacheRootFile(this.context, ".dk")) != null && file2.renameTo(cacheRootFile)) {
                file2.delete();
            }
        }
        File cacheRootFile2 = ResHelper.getCacheRootFile(this.context, ".dk");
        if (cacheRootFile2 != null && !cacheRootFile2.exists()) {
            return null;
        }
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(cacheRootFile2));
        Object readObject = objectInputStream.readObject();
        if (readObject != null && (readObject instanceof char[])) {
            str = String.valueOf((char[]) readObject);
        }
        objectInputStream.close();
        return str;
    }

    private String getLocalSerial() {
        try {
            String AES128Decode = Data.AES128Decode(Strings.getString(76), (byte[]) ResHelper.readObjectFromFile(ResHelper.getCacheRootFile(this.context, ".slw").getPath()));
            return !TextUtils.isEmpty(AES128Decode) ? AES128Decode.trim() : getWAbcd(3);
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return null;
        }
    }

    private String getLocalWifiMac() {
        try {
            File cacheRootFile = ResHelper.getCacheRootFile(this.context, ".mcw");
            String AES128Decode = cacheRootFile.exists() ? Data.AES128Decode(Strings.getString(138), (byte[]) ResHelper.readObjectFromFile(cacheRootFile.getPath())) : null;
            if (TextUtils.isEmpty(AES128Decode)) {
                AES128Decode = getWAbcd(2);
            }
            if (!TextUtils.isEmpty(AES128Decode) && AES128Decode.trim().matches("^[a-fA-F0-9]{2}(:[a-fA-F0-9]{2}){5}$")) {
                return AES128Decode.trim();
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        return null;
    }

    private HashMap<String, Object> getMapFromOtherPlace(String str) {
        try {
            String AES128Decode = Data.AES128Decode(Strings.getString(76), (byte[]) ResHelper.readObjectFromFile(str));
            if (TextUtils.isEmpty(AES128Decode)) {
                return null;
            }
            HashMap<String, Object> fromJson = new Hashon().fromJson(AES128Decode);
            String str2 = (String) fromJson.remove(Strings.getString(78));
            String MD5 = Data.MD5(getSortWabcd(fromJson) + Strings.getString(77));
            if (str2 != null) {
                if (!str2.equals(MD5)) {
                    return null;
                }
            }
            return fromJson;
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return null;
        }
    }

    private ArrayList<String> getPL() {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            Object invokeInstanceMethod = ReflectHelper.invokeInstanceMethod(ReflectHelper.invokeStaticMethod(ReflectHelper.importClass(Strings.getString(42)), Strings.getString(43), new Object[0]), Strings.getString(44), Strings.getString(20));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((InputStream) ReflectHelper.invokeInstanceMethod(invokeInstanceMethod, Strings.getString(45), new Object[0]), "utf-8"));
            for (String readLine = bufferedReader.readLine(); readLine != null; readLine = bufferedReader.readLine()) {
                String trim = readLine.trim();
                if (trim.length() > 8 && trim.substring(0, 8).equalsIgnoreCase("package:")) {
                    String trim2 = trim.substring(8).trim();
                    if (!TextUtils.isEmpty(trim2)) {
                        arrayList.add(trim2);
                    }
                }
            }
            bufferedReader.close();
            ReflectHelper.invokeInstanceMethod(invokeInstanceMethod, Strings.getString(51), new Object[0]);
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
        if (arrayList.isEmpty()) {
            try {
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.addCategory(Strings.getString(74));
                for (ResolveInfo resolveInfo : this.context.getPackageManager().queryIntentActivities(intent, 0)) {
                    if (resolveInfo != null && resolveInfo.activityInfo != null && !TextUtils.isEmpty(resolveInfo.activityInfo.packageName)) {
                        arrayList.add(resolveInfo.activityInfo.packageName);
                    }
                }
            } catch (Throwable th2) {
                MobLog.getInstance().w(th2);
            }
        }
        return arrayList;
    }

    private String getSortWabcd(HashMap<String, Object> hashMap) {
        if (hashMap == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        String str = (String) hashMap.get(AmapLoc.RESULT_TYPE_GPS);
        if (str != null) {
            sb.append(str);
        }
        String str2 = (String) hashMap.get(AmapLoc.RESULT_TYPE_WIFI_ONLY);
        if (str2 != null) {
            sb.append(str2);
        }
        String str3 = (String) hashMap.get(AmapLoc.RESULT_TYPE_FUSED);
        if (str3 != null) {
            sb.append(str3);
        }
        String str4 = (String) hashMap.get(AmapLoc.RESULT_TYPE_CELL_ONLY);
        if (str4 != null) {
            sb.append(str4);
        }
        return sb.toString();
    }

    private String getSystemProperties(String str) {
        try {
            Object invokeStaticMethod = ReflectHelper.invokeStaticMethod(ReflectHelper.importClass(Strings.getString(9)), Strings.getString(10), str);
            return invokeStaticMethod != null ? String.valueOf(invokeStaticMethod) : "";
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return "";
        }
    }

    public static String getTopApp(Context context) throws Throwable {
        if (Build.VERSION.SDK_INT < 21) {
            return ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1).get(0).topActivity.getPackageName();
        }
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService("usagestats");
        if (usageStatsManager == null) {
            return null;
        }
        long currentTimeMillis = System.currentTimeMillis();
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(4, currentTimeMillis - DateUtils.MILLIS_PER_HOUR, currentTimeMillis);
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
            return "";
        }
        int i = 0;
        for (int i2 = 0; i2 < queryUsageStats.size(); i2++) {
            if (queryUsageStats.get(i2).getLastTimeUsed() > queryUsageStats.get(i).getLastTimeUsed()) {
                i = i2;
            }
        }
        return queryUsageStats.get(i).getPackageName();
    }

    private void getTrafficBytes(String str, String str2, long[] jArr) {
        try {
            int indexOf = str.indexOf(str2);
            if (indexOf != -1) {
                Matcher matcher = Pattern.compile(" \\d+ ").matcher(str.substring(indexOf));
                int i = 0;
                while (matcher.find()) {
                    if (i == 0) {
                        jArr[0] = Long.parseLong(matcher.group().trim());
                    } else if (i == 8) {
                        jArr[1] = Long.parseLong(matcher.group().trim());
                        return;
                    }
                    i++;
                }
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
    }

    private String getValidNetworkHardwareAddress() throws Throwable {
        HashMap<String, String> listNetworkHardware = listNetworkHardware();
        if (listNetworkHardware == null || listNetworkHardware.isEmpty()) {
            return null;
        }
        ArrayList arrayList = new ArrayList(listNetworkHardware.keySet());
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        ArrayList arrayList5 = new ArrayList();
        ArrayList arrayList6 = new ArrayList();
        ArrayList arrayList7 = new ArrayList();
        ArrayList arrayList8 = new ArrayList();
        String str = null;
        while (arrayList.size() > 0) {
            String trim = ((String) arrayList.remove(0)).trim();
            if (trim.equals("wlan0")) {
                str = "wlan0";
            } else if (trim.startsWith("wlan")) {
                arrayList2.add(trim);
            } else if (trim.startsWith("eth")) {
                arrayList3.add(trim);
            } else if (trim.startsWith("rev_rmnet")) {
                arrayList4.add(trim);
            } else if (trim.startsWith("dummy")) {
                arrayList5.add(trim);
            } else if (trim.startsWith("usbnet")) {
                arrayList6.add(trim);
            } else if (trim.startsWith("rmnet_usb")) {
                arrayList7.add(trim);
            } else {
                arrayList8.add(trim);
            }
        }
        Collections.sort(arrayList2);
        Collections.sort(arrayList3);
        Collections.sort(arrayList4);
        Collections.sort(arrayList5);
        Collections.sort(arrayList6);
        Collections.sort(arrayList7);
        Collections.sort(arrayList8);
        if (!TextUtils.isEmpty(str)) {
            arrayList.add(str);
        }
        arrayList.addAll(arrayList2);
        if ("wifi".equals(getNetworkType())) {
            try {
                String currentNetworkHardwareAddress = getCurrentNetworkHardwareAddress();
                if (!TextUtils.isEmpty(currentNetworkHardwareAddress)) {
                    arrayList.add(currentNetworkHardwareAddress);
                }
            } catch (Throwable unused) {
            }
        }
        if (arrayList.size() > 0) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                String str2 = listNetworkHardware.get((String) it.next());
                if (str2 != null && checkMacIsValid(str2)) {
                    this.wfMc = str2.trim();
                    saveLocalWifiMac(this.wfMc);
                    return this.wfMc;
                }
            }
        }
        arrayList.addAll(arrayList3);
        arrayList.addAll(arrayList4);
        arrayList.addAll(arrayList5);
        arrayList.addAll(arrayList6);
        arrayList.addAll(arrayList7);
        arrayList.addAll(arrayList8);
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            String str3 = listNetworkHardware.get((String) it2.next());
            if (str3 != null && checkMacIsValid(str3)) {
                return str3.trim();
            }
        }
        return null;
    }

    private String getWifiMac() {
        Object invokeInstanceMethod;
        String str;
        try {
            Object systemServiceSafe = getSystemServiceSafe("wifi");
            if (systemServiceSafe != null && (invokeInstanceMethod = ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(2), new Object[0])) != null && (str = (String) ReflectHelper.invokeInstanceMethod(invokeInstanceMethod, Strings.getString(5), new Object[0])) != null) {
                return str.trim();
            }
            return null;
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
        return null;
    }

    private int getWifiSecurity(String str) {
        if (str == null) {
            return 0;
        }
        if (str.contains("WEP")) {
            return 1;
        }
        if (str.contains("PSK")) {
            return 2;
        }
        return str.contains("EAP") ? 3 : 0;
    }

    private String getWlanMac() {
        try {
            String hardwareAddressFromShell = getHardwareAddressFromShell("wlan0");
            if (hardwareAddressFromShell != null && checkMacIsValid(hardwareAddressFromShell)) {
                this.wfMc = hardwareAddressFromShell.trim();
                saveLocalWifiMac(this.wfMc);
                return this.wfMc;
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        try {
            return getValidNetworkHardwareAddress();
        } catch (Throwable th2) {
            MobLog.getInstance().d(th2);
            return null;
        }
    }

    private InputStream invokeRuntimeExec(String[] strArr) {
        try {
            return (InputStream) ReflectHelper.invokeInstanceMethod(ReflectHelper.invokeInstanceMethod(ReflectHelper.invokeStaticMethod(ReflectHelper.importClass(Strings.getString(42)), Strings.getString(43), new Object[0]), Strings.getString(44), strArr), Strings.getString(45), new Object[0]);
        } catch (Throwable unused) {
            return null;
        }
    }

    private static boolean isBackground(Context context) {
        try {
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getApplicationContext().getSystemService("activity")).getRunningAppProcesses();
            if (runningAppProcesses != null && !runningAppProcesses.isEmpty()) {
                String packageName = context.getPackageName();
                for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                    if (runningAppProcessInfo.processName.equals(packageName)) {
                        return runningAppProcessInfo.importance == 400;
                    }
                }
            }
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
        return false;
    }

    private boolean isSystemApp(PackageInfo packageInfo) {
        return ((packageInfo.applicationInfo.flags & 1) == 1) || ((packageInfo.applicationInfo.flags & 128) == 1);
    }

    private HashMap<String, Object> readCache(File file) {
        if (file.exists()) {
            try {
                FileChannel channel = new FileInputStream(file).getChannel();
                ByteBuffer allocate = ByteBuffer.allocate((int) channel.size());
                do {
                } while (channel.read(allocate) > 0);
                return decryptData(getInstance(this.context).getModel(), allocate.array());
            } catch (Throwable th) {
                MobLog.getInstance().d(th);
            }
        }
        return new HashMap<>();
    }

    private void saveANS(HashMap<String, String> hashMap) {
        if (hashMap != null) {
            try {
                ResHelper.saveObjectToFile(ResHelper.getDataCacheFile(this.context, ".ans").getAbsolutePath(), hashMap);
            } catch (Throwable th) {
                MobLog.getInstance().w(th);
            }
        }
    }

    private void saveLocalDeviceKey(String str) throws Throwable {
        if (getSdcardState()) {
            File cacheRootFile = ResHelper.getCacheRootFile(this.context, ".dk");
            if (cacheRootFile != null && cacheRootFile.exists()) {
                cacheRootFile.delete();
            }
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(cacheRootFile));
            objectOutputStream.writeObject(str.toCharArray());
            objectOutputStream.flush();
            objectOutputStream.close();
        }
    }

    private void saveLocalSerial(String str) {
        try {
            if (TextUtils.isEmpty(str) || str.trim().equals("")) {
                return;
            }
            File cacheRootFile = ResHelper.getCacheRootFile(this.context, ".slw");
            ResHelper.saveObjectToFile(cacheRootFile.getPath(), Data.AES128Encode(Strings.getString(76), str.trim()));
            saveWabcd(str.trim(), 3);
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
    }

    private void saveLocalWifiMac(String str) {
        try {
            if (TextUtils.isEmpty(str) || !str.trim().matches("^[a-fA-F0-9]{2}(:[a-fA-F0-9]{2}){5}$")) {
                return;
            }
            File cacheRootFile = ResHelper.getCacheRootFile(this.context, ".mcw");
            ResHelper.saveObjectToFile(cacheRootFile.getPath(), Data.AES128Encode(Strings.getString(138), str.trim()));
            saveWabcd(str.trim(), 2);
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
    }

    private void writeCache(File file, HashMap<String, Object> hashMap) {
        try {
            byte[] encryptData = encryptData(getInstance(this.context).getModel(), hashMap);
            FileChannel channel = new FileOutputStream(file).getChannel();
            channel.write(ByteBuffer.wrap(encryptData));
            channel.force(true);
            channel.close();
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
    }

    public String Base64AES(String str, String str2) {
        String str3;
        try {
            str3 = Base64.encodeToString(Data.AES128Encode(str2, str), 0);
            try {
                return str3.contains("\n") ? str3.replace("\n", "") : str3;
            } catch (Throwable th) {
                th = th;
                MobLog.getInstance().w(th);
                return str3;
            }
        } catch (Throwable th2) {
            th = th2;
            str3 = null;
        }
    }

    public boolean amIOnForeground() {
        try {
            if (Build.VERSION.SDK_INT > 27) {
                return !isBackground(this.context);
            }
            Iterator it = ((Map) ReflectHelper.getInstanceField(currentActivityThread(), Strings.getString(23))).values().iterator();
            while (it.hasNext()) {
                if (!((Boolean) ReflectHelper.getInstanceField(it.next(), Strings.getString(24))).booleanValue()) {
                    return true;
                }
            }
            return false;
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return false;
        }
    }

    public int bs(final Context context, Intent intent) throws Throwable {
        final boolean[] zArr = {false};
        try {
            if (!context.bindService(intent, new ServiceConnection() { // from class: com.mob.tools.utils.DeviceHelper.4
                @Override // android.content.ServiceConnection
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    try {
                        synchronized (zArr) {
                            zArr[0] = true;
                            zArr.notifyAll();
                            context.unbindService(this);
                        }
                    } catch (Throwable th) {
                        MobLog.getInstance().d(th);
                    }
                }

                @Override // android.content.ServiceConnection
                public void onServiceDisconnected(ComponentName componentName) {
                    try {
                        synchronized (zArr) {
                            zArr.notifyAll();
                        }
                    } catch (Throwable th) {
                        MobLog.getInstance().d(th);
                    }
                }
            }, 1)) {
                return 0;
            }
            long j = 200;
            while (!zArr[0] && j > 0) {
                synchronized (zArr) {
                    j -= 20;
                    zArr.wait(20L);
                }
            }
            return zArr[0] ? 1 : 2;
        } catch (SecurityException e) {
            MobLog.getInstance().d(e);
            return 3;
        }
    }

    public int ca(Context context, String str) {
        try {
            Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(str);
            if (launchIntentForPackage != null) {
                return cs(context, launchIntentForPackage);
            }
            return 0;
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return 3;
        }
    }

    public int cb(Context context, String str) {
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                ((ClipboardManager) context.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("", str));
                return 1;
            }
            ((ClipboardManager) context.getSystemService("clipboard")).setText(str);
            return 1;
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return 2;
        }
    }

    public boolean checkADBModel(int i) {
        switch (i) {
            case 0:
                return false;
            case 1:
                return usbEnable();
            case 16:
                return devEnable();
            case 17:
                return usbEnable() || devEnable();
            case 273:
                return usbEnable() && devEnable();
            default:
                return false;
        }
    }

    public boolean checkPad() {
        if (this.bvs.ckpd != null) {
            return this.bvs.ckpd.booleanValue();
        }
        this.bvs.ckpd = Boolean.valueOf((this.context.getResources().getConfiguration().screenLayout & 15) >= 3);
        return this.bvs.ckpd.booleanValue();
    }

    public boolean checkPermission(String str) throws Throwable {
        int i = -1;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                ReflectHelper.importClass("android.content.Context");
                Integer num = (Integer) ReflectHelper.invokeInstanceMethod(this.context, Strings.getString(22), str);
                if (num != null) {
                    i = num.intValue();
                }
            } catch (Throwable th) {
                MobLog.getInstance().d(th);
            }
        } else {
            i = this.context.getPackageManager().checkPermission(str, getPackageName());
        }
        return i == 0;
    }

    public boolean checkUA() {
        try {
            return ((Intent) ReflectHelper.invokeInstanceMethod(this.context, "registerReceiver", new Object[]{null, new IntentFilter("android.intent.action.BATTERY_CHANGED")}, new Class[]{BroadcastReceiver.class, IntentFilter.class})).getIntExtra("plugged", -1) == 2;
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return false;
        }
    }

    public int cs(Context context, Intent intent) {
        boolean z;
        boolean z2;
        if (Build.VERSION.SDK_INT >= 26) {
            return 4;
        }
        try {
            ComponentName component = intent.getComponent();
            String packageName = component.getPackageName();
            String className = component.getClassName();
            List<ActivityManager.RunningServiceInfo> runningServices = ((ActivityManager) context.getSystemService("activity")).getRunningServices(1000);
            if (runningServices != null && !runningServices.isEmpty()) {
                Iterator<ActivityManager.RunningServiceInfo> it = runningServices.iterator();
                z2 = false;
                while (true) {
                    if (!it.hasNext()) {
                        z = false;
                        break;
                    }
                    ActivityManager.RunningServiceInfo next = it.next();
                    String packageName2 = next.service.getPackageName();
                    String className2 = next.service.getClassName();
                    if (packageName2.equals(packageName)) {
                        if (className2.equals(className)) {
                            z = true;
                            z2 = true;
                            break;
                        }
                        z2 = true;
                    }
                }
            } else {
                z = false;
                z2 = false;
            }
            if (z2) {
                return z ? 1 : 2;
            }
            return 0;
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return 3;
        }
    }

    public int cscreen() {
        if (((PowerManager) this.context.getSystemService("power")).isScreenOn()) {
            return ((KeyguardManager) this.context.getSystemService("keyguard")).inKeyguardRestrictedInputMode() ? 2 : 1;
        }
        return 0;
    }

    public boolean cx() {
        try {
            if (this.context.getPackageManager().getPackageInfo("de.robv.android.xposed.installer", 0) != null) {
                return true;
            }
        } catch (Throwable unused) {
        }
        try {
            throw new Exception("test");
        } catch (Throwable th) {
            for (StackTraceElement stackTraceElement : th.getStackTrace()) {
                if (stackTraceElement.getClassName().contains("de.robv.android.xposed.XposedBridge")) {
                    return true;
                }
            }
            try {
                try {
                    ClassLoader.getSystemClassLoader().loadClass("de.robv.android.xposed.XposedHelpers").newInstance();
                    try {
                        ClassLoader.getSystemClassLoader().loadClass("de.robv.android.xposed.XposedBridge").newInstance();
                        return true;
                    } catch (IllegalAccessException unused2) {
                        return true;
                    } catch (InstantiationException unused3) {
                        return true;
                    }
                } catch (Throwable th2) {
                    MobLog.getInstance().d(th2);
                    BufferedReader bufferedReader = null;
                    try {
                        try {
                            BufferedReader bufferedReader2 = new BufferedReader(new FileReader("/proc/" + Process.myPid() + "/maps"));
                            boolean z = false;
                            while (true) {
                                try {
                                    String readLine = bufferedReader2.readLine();
                                    if (readLine == null || z) {
                                        try {
                                            bufferedReader2.close();
                                        } catch (IOException e) {
                                            MobLog.getInstance().d(e);
                                        }
                                        return z;
                                    }
                                    z = readLine.toLowerCase().contains("xposed");
                                } catch (Throwable th3) {
                                    th = th3;
                                    bufferedReader = bufferedReader2;
                                    if (bufferedReader != null) {
                                        try {
                                            bufferedReader.close();
                                        } catch (IOException e2) {
                                            MobLog.getInstance().d(e2);
                                        }
                                    }
                                    throw th;
                                }
                            }
                        } catch (Throwable th4) {
                            th = th4;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                    }
                }
            } catch (IllegalAccessException unused4) {
                return true;
            } catch (InstantiationException unused5) {
                return true;
            }
        }
    }

    public boolean debugable() {
        if (this.bvs.dbg != null) {
            return this.bvs.dbg.booleanValue();
        }
        try {
            boolean z = true;
            PackageInfo packageInfo = this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 1);
            BVS bvs = this.bvs;
            if ((packageInfo.applicationInfo.flags & 2) == 0) {
                z = false;
            }
            bvs.dbg = Boolean.valueOf(z);
            return this.bvs.dbg.booleanValue();
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            this.bvs.dbg = false;
            return this.bvs.dbg.booleanValue();
        }
    }

    public boolean devEnable() {
        try {
            if (Build.VERSION.SDK_INT >= 17) {
                if (Settings.Secure.getInt(this.context.getContentResolver(), "development_settings_enabled", 0) <= 0) {
                    return false;
                }
            } else if (Settings.Secure.getInt(this.context.getContentResolver(), "development_settings_enabled", 0) <= 0) {
                return false;
            }
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    public String gb(Context context) {
        try {
            if (Build.VERSION.SDK_INT < 11) {
                return (String) ((ClipboardManager) context.getSystemService("clipboard")).getText();
            }
            ClipData primaryClip = ((ClipboardManager) context.getSystemService("clipboard")).getPrimaryClip();
            if (primaryClip == null || primaryClip.getItemCount() <= 0) {
                return null;
            }
            return primaryClip.getItemAt(0).getText().toString();
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return null;
        }
    }

    public ArrayList<HashMap<String, String>> getAA() {
        return getAL(false, true);
    }

    public synchronized ArrayList<HashMap<String, String>> getAL(boolean z, boolean z2) {
        return getAL(z, z2, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x005b  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00b5  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00c5  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00c8  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x00b8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized java.util.ArrayList<java.util.HashMap<java.lang.String, java.lang.String>> getAL(boolean r13, boolean r14, boolean r15) {
        /*
            Method dump skipped, instructions count: 260
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.tools.utils.DeviceHelper.getAL(boolean, boolean, boolean):java.util.ArrayList");
    }

    public String getAdvertisingID() throws Throwable {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new Throwable("Do not call this function from the main thread !");
        }
        if (!TextUtils.isEmpty(this.advertiseID)) {
            return this.advertiseID;
        }
        Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
        intent.setPackage("com.google.android.gms");
        GSConnection gSConnection = new GSConnection();
        try {
            this.context.bindService(intent, gSConnection, 1);
            IBinder takeBinder = gSConnection.takeBinder();
            if (takeBinder == null) {
                return this.advertiseID;
            }
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            obtain.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
            takeBinder.transact(1, obtain, obtain2, 0);
            obtain2.readException();
            this.advertiseID = obtain2.readString();
            obtain2.recycle();
            obtain.recycle();
            return this.advertiseID;
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return this.advertiseID;
        } finally {
            this.context.unbindService(gSConnection);
        }
    }

    public int getAlbumCount() {
        Cursor query;
        try {
            if (!checkPermission("android.permission.READ_EXTERNAL_STORAGE") || (query = this.context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, "bucket_display_name like ? or bucket_display_name like ? or bucket_display_name like ?", new String[]{"%Camera%", "%相机%", "%DCIM%"}, "datetaken")) == null) {
                return 0;
            }
            int count = query.getCount();
            try {
                query.close();
            } catch (Throwable unused) {
            }
            return count;
        } catch (Throwable unused2) {
            return 0;
        }
    }

    public String getAndroidID() {
        try {
            if (!TextUtils.isEmpty(this.bvs.aid)) {
                return this.bvs.aid;
            }
            this.bvs.aid = Settings.Secure.getString(this.context.getContentResolver(), "android_id");
            MobLog.getInstance().i("getAndroidID === " + this.bvs.aid, new Object[0]);
            return this.bvs.aid;
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return "";
        }
    }

    public String getAppLanguage() {
        if (!TextUtils.isEmpty(this.bvs.applang)) {
            return this.bvs.applang;
        }
        this.bvs.applang = this.context.getResources().getConfiguration().locale.getLanguage();
        return this.bvs.applang;
    }

    public String getAppName() {
        if (!TextUtils.isEmpty(this.bvs.apnm)) {
            return this.bvs.apnm;
        }
        try {
            ApplicationInfo applicationInfo = this.context.getApplicationInfo();
            String str = applicationInfo.name;
            if (str != null) {
                if (Build.VERSION.SDK_INT < 25 || str.endsWith(".*")) {
                    this.bvs.apnm = str;
                    return this.bvs.apnm;
                }
                try {
                    ReflectHelper.importClass(str);
                    str = null;
                } catch (Throwable unused) {
                }
            }
            int i = applicationInfo.labelRes;
            if (i > 0) {
                try {
                    str = this.context.getString(i);
                } catch (Throwable th) {
                    MobLog.getInstance().w(th);
                }
            } else {
                str = String.valueOf(applicationInfo.nonLocalizedLabel);
            }
            this.bvs.apnm = str;
            return this.bvs.apnm;
        } catch (Throwable th2) {
            MobLog.getInstance().w(th2);
            return "";
        }
    }

    public String getAppName(String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            PackageManager packageManager = this.context.getPackageManager();
            return packageManager.getPackageInfo(str, 1).applicationInfo.loadLabel(packageManager).toString();
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return null;
        }
    }

    public int getAppVersion() {
        PackageInfo packageInfo;
        if (this.bvs.apver != -1) {
            return this.bvs.apver;
        }
        try {
            packageInfo = this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 0);
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            this.bvs.apver = 0;
        }
        if (Build.VERSION.SDK_INT >= 28) {
            this.bvs.apver = (int) packageInfo.getLongVersionCode();
            return this.bvs.apver;
        }
        this.bvs.apver = packageInfo.versionCode;
        return this.bvs.apver;
    }

    public String getAppVersionName() {
        if (!"-1".equals(this.bvs.apvernm)) {
            return this.bvs.apvernm;
        }
        try {
            PackageInfo packageInfo = this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 0);
            this.bvs.apvernm = packageInfo.versionName;
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            this.bvs.apvernm = "1.0";
        }
        return this.bvs.apvernm;
    }

    public ArrayList<HashMap<String, Object>> getArpList() {
        BufferedReader bufferedReader;
        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new FileReader("/proc/net/arp"));
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                break;
            }
            try {
                String trim = readLine.trim();
                if (!trim.toUpperCase(Locale.US).contains("IP") && trim.length() >= 63) {
                    String trim2 = trim.substring(41, 63).trim();
                    if (!trim2.startsWith("00:00:00:00:00:00")) {
                        String trim3 = trim.substring(0, 17).trim();
                        String trim4 = trim.substring(29, 32).trim();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("ip", trim3);
                        hashMap.put("flag", trim4);
                        hashMap.put(Dic.MAC, trim2);
                        arrayList.add(hashMap);
                    }
                }
            } catch (Throwable th2) {
                MobLog.getInstance().d(th2);
            }
            MobLog.getInstance().d(th);
            return arrayList;
        }
        bufferedReader.close();
        return arrayList;
    }

    public ArrayList<HashMap<String, Object>> getAvailableWifiList() {
        Object systemServiceSafe;
        List list;
        String[] split;
        String[] split2;
        String trim;
        try {
            if (!checkPermission("android.permission.ACCESS_WIFI_STATE") || (systemServiceSafe = getSystemServiceSafe("wifi")) == null || (list = (List) ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(34), new Object[0])) == null) {
                return null;
            }
            if (Build.VERSION.SDK_INT > 27) {
                split = TextUtils.split(Strings.getString(72), ",");
                split2 = TextUtils.split(Strings.getString(73), ",");
            } else {
                split = TextUtils.split(Strings.getString(35), ",");
                split2 = TextUtils.split(Strings.getString(36), ",");
            }
            ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();
            for (Object obj : list) {
                HashMap<String, Object> hashMap = new HashMap<>();
                int length = split.length;
                String str = null;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    try {
                        trim = split[i].trim();
                    } catch (Throwable unused) {
                    }
                    if ("SSID".equals(trim)) {
                        String str2 = (String) ReflectHelper.getInstanceField(obj, trim);
                        if (TextUtils.isEmpty(str2)) {
                            str = str2;
                            break;
                        }
                        hashMap.put(trim, str2);
                        str = str2;
                        i++;
                    } else {
                        if ("capabilities".equals(trim)) {
                            String str3 = (String) ReflectHelper.getInstanceField(obj, trim);
                            if (str3 != null && str3.contains("[IBSS]")) {
                                str = null;
                                break;
                            }
                            hashMap.put(trim, str3);
                        } else {
                            hashMap.put(trim, ReflectHelper.getInstanceField(obj, trim));
                        }
                        i++;
                    }
                }
                if (!TextUtils.isEmpty(str)) {
                    for (String str4 : split2) {
                        try {
                            String trim2 = str4.trim();
                            Object instanceField = ReflectHelper.getInstanceField(obj, trim2);
                            hashMap.put(trim2, instanceField == null ? null : instanceField.toString());
                        } catch (Throwable unused2) {
                        }
                    }
                    try {
                        hashMap.put(Strings.getString(39), ReflectHelper.invokeInstanceMethod(obj, Strings.getString(37), new Object[0]));
                    } catch (Throwable unused3) {
                    }
                    try {
                        if (Build.VERSION.SDK_INT < 28) {
                            List list2 = (List) ReflectHelper.getInstanceField(obj, Strings.getString(38));
                            hashMap.put(Strings.getString(38), list2 == null ? null : new ArrayList(list2));
                        }
                    } catch (Throwable unused4) {
                    }
                    arrayList.add(hashMap);
                }
            }
            return arrayList;
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
        return null;
    }

    @SuppressLint({"MissingPermission"})
    public String getBTMac() {
        String str;
        String str2 = null;
        if (Build.VERSION.SDK_INT >= 27) {
            return null;
        }
        try {
            if (checkPermission("android.permission.BLUETOOTH")) {
                if (!"-1".equals(this.bvs.btmc)) {
                    return this.bvs.btmc;
                }
                BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
                if (Build.VERSION.SDK_INT < 23) {
                    str = defaultAdapter.getAddress();
                } else {
                    try {
                        Object instanceField = ReflectHelper.getInstanceField(defaultAdapter, "mService");
                        str = instanceField != null ? (String) ReflectHelper.invokeInstanceMethod(instanceField, "getAddress", new Object[0]) : null;
                    } catch (Throwable th) {
                        MobLog.getInstance().d(th);
                    }
                }
                str2 = str;
                this.bvs.btmc = str2;
            }
        } catch (Throwable unused) {
            this.bvs.btmc = null;
        }
        return str2;
    }

    public String getBTMacFromProvider() {
        if (!"-1".equals(this.bvs.btmcp)) {
            return this.bvs.btmcp;
        }
        this.bvs.btmcp = Settings.Secure.getString(this.context.getContentResolver(), "bluetooth_address");
        return this.bvs.btmcp;
    }

    public String getBaseband() {
        if (!TextUtils.isEmpty(this.bvs.bsbd)) {
            return this.bvs.bsbd;
        }
        String str = null;
        try {
            str = getSystemProperties(Strings.getString(116));
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        this.bvs.bsbd = str;
        return str;
    }

    public void getBatteryState(final ReflectHelper.ReflectRunnable<HashMap<String, Object>, Void> reflectRunnable) {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
            ReflectHelper.invokeInstanceMethod(this.context, "registerReceiver", new Object[]{new BroadcastReceiver() { // from class: com.mob.tools.utils.DeviceHelper.5
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    HashMap hashMap = new HashMap();
                    for (String str : intent.getExtras().keySet()) {
                        hashMap.put(str, intent.getExtras().get(str));
                    }
                    if (reflectRunnable != null) {
                        reflectRunnable.run(hashMap);
                    }
                    try {
                        ReflectHelper.invokeInstanceMethod(context, "unregisterReceiver", new Object[]{this}, new Class[]{BroadcastReceiver.class});
                    } catch (Throwable unused) {
                    }
                }
            }, intentFilter}, new Class[]{BroadcastReceiver.class, IntentFilter.class});
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            if (reflectRunnable != null) {
                reflectRunnable.run(null);
            }
        }
    }

    public String getBluetoothName() {
        Object invokeStaticMethod;
        try {
            if (!"-1".equals(this.bvs.btnm)) {
                return this.bvs.btnm;
            }
            if (!checkPermission("android.permission.BLUETOOTH") || (invokeStaticMethod = ReflectHelper.invokeStaticMethod(ReflectHelper.importClass(Strings.getString(16)), Strings.getString(17), new Object[0])) == null) {
                return null;
            }
            this.bvs.btnm = (String) ReflectHelper.invokeInstanceMethod(invokeStaticMethod, Strings.getString(18), new Object[0]);
            return this.bvs.btnm;
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return null;
        }
    }

    public String getBoardFromSysProperty() {
        if (!TextUtils.isEmpty(this.bvs.bdfp)) {
            return this.bvs.bdfp;
        }
        String str = null;
        try {
            str = getSystemProperties(Strings.getString(117));
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        this.bvs.bdfp = str;
        return str;
    }

    public String getBoardPlatform() {
        if (!TextUtils.isEmpty(this.bvs.bdptfm)) {
            return this.bvs.bdptfm;
        }
        String str = null;
        try {
            str = getSystemProperties(Strings.getString(118));
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        this.bvs.bdptfm = str;
        return str;
    }

    public ArrayList<HashMap<String, Object>> getBondedBluetooth() {
        try {
            return BHelper.getInstance(this.context).getBondedDevice();
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return new ArrayList<>();
        }
    }

    public String getBrand() {
        if (!TextUtils.isEmpty(this.bvs.brd)) {
            return this.bvs.brd;
        }
        this.bvs.brd = Build.BRAND;
        return this.bvs.brd;
    }

    public String getBssid() {
        try {
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        if (!"-1".equals(this.bvs.bsi)) {
            return this.bvs.bsi;
        }
        if (checkPermission("android.permission.ACCESS_WIFI_STATE")) {
            Object systemServiceSafe = getSystemServiceSafe("wifi");
            if (systemServiceSafe == null) {
                this.bvs.bsi = null;
                return null;
            }
            Object invokeInstanceMethod = ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(2), new Object[0]);
            if (invokeInstanceMethod != null) {
                String str = (String) ReflectHelper.invokeInstanceMethod(invokeInstanceMethod, Strings.getString(4), new Object[0]);
                BVS bvs = this.bvs;
                if (str == null) {
                    str = null;
                }
                bvs.bsi = str;
                return this.bvs.bsi;
            }
        }
        return null;
    }

    public HashMap<String, String> getCPUFreq() {
        HashMap<String, String> hashMap = new HashMap<>();
        String readFile = readFile("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
        if (!TextUtils.isEmpty(readFile)) {
            hashMap.put("currentCpuHz", readFile);
        }
        String readFile2 = readFile("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq");
        if (!TextUtils.isEmpty(readFile2)) {
            hashMap.put("minCpuHz", readFile2);
        }
        String readFile3 = readFile("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
        if (!TextUtils.isEmpty(readFile3)) {
            hashMap.put("maxCpuHz", readFile3);
        }
        return hashMap;
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x002d, code lost:
    
        if (r5 == null) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x002f, code lost:
    
        r3.add(r5);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.util.HashMap<java.lang.String, java.lang.Object> getCPUInfo() {
        /*
            r9 = this;
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            java.io.FileReader r1 = new java.io.FileReader     // Catch: java.lang.Throwable -> L7f
            r2 = 41
            java.lang.String r2 = com.mob.tools.utils.Strings.getString(r2)     // Catch: java.lang.Throwable -> L7f
            r1.<init>(r2)     // Catch: java.lang.Throwable -> L7f
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L7f
            r2.<init>(r1)     // Catch: java.lang.Throwable -> L7f
            java.util.ArrayList r3 = new java.util.ArrayList     // Catch: java.lang.Throwable -> L7f
            r3.<init>()     // Catch: java.lang.Throwable -> L7f
            java.lang.String r4 = "processors"
            r0.put(r4, r3)     // Catch: java.lang.Throwable -> L7f
            r4 = 0
        L20:
            r5 = r4
        L21:
            java.lang.String r6 = r2.readLine()     // Catch: java.lang.Throwable -> L7f
            if (r6 == 0) goto L78
            boolean r7 = android.text.TextUtils.isEmpty(r6)     // Catch: java.lang.Throwable -> L7f
            if (r7 == 0) goto L33
            if (r5 == 0) goto L20
            r3.add(r5)     // Catch: java.lang.Throwable -> L7f
            goto L20
        L33:
            java.lang.String r6 = r6.trim()     // Catch: java.lang.Throwable -> L7f
            java.lang.String r7 = "processor"
            boolean r7 = r6.startsWith(r7)     // Catch: java.lang.Throwable -> L7f
            if (r7 == 0) goto L49
            if (r5 == 0) goto L44
            r3.add(r5)     // Catch: java.lang.Throwable -> L7f
        L44:
            java.util.HashMap r5 = new java.util.HashMap     // Catch: java.lang.Throwable -> L7f
            r5.<init>()     // Catch: java.lang.Throwable -> L7f
        L49:
            java.lang.String r7 = ":"
            java.lang.String[] r6 = r6.split(r7)     // Catch: java.lang.Throwable -> L7f
            if (r6 == 0) goto L21
            int r7 = r6.length     // Catch: java.lang.Throwable -> L7f
            r8 = 1
            if (r7 <= r8) goto L21
            r7 = 0
            if (r5 != 0) goto L68
            r7 = r6[r7]     // Catch: java.lang.Throwable -> L7f
            java.lang.String r7 = r7.trim()     // Catch: java.lang.Throwable -> L7f
            r6 = r6[r8]     // Catch: java.lang.Throwable -> L7f
            java.lang.String r6 = r6.trim()     // Catch: java.lang.Throwable -> L7f
            r0.put(r7, r6)     // Catch: java.lang.Throwable -> L7f
            goto L21
        L68:
            r7 = r6[r7]     // Catch: java.lang.Throwable -> L7f
            java.lang.String r7 = r7.trim()     // Catch: java.lang.Throwable -> L7f
            r6 = r6[r8]     // Catch: java.lang.Throwable -> L7f
            java.lang.String r6 = r6.trim()     // Catch: java.lang.Throwable -> L7f
            r5.put(r7, r6)     // Catch: java.lang.Throwable -> L7f
            goto L21
        L78:
            r2.close()     // Catch: java.lang.Throwable -> L7f
            r1.close()     // Catch: java.lang.Throwable -> L7f
            goto L87
        L7f:
            r1 = move-exception
            com.mob.tools.log.NLog r2 = com.mob.tools.MobLog.getInstance()
            r2.d(r1)
        L87:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.tools.utils.DeviceHelper.getCPUInfo():java.util.HashMap");
    }

    public String getCPUType() {
        if (!TextUtils.isEmpty(this.bvs.cptp)) {
            return this.bvs.cptp;
        }
        try {
            this.bvs.cptp = Build.VERSION.SDK_INT < 21 ? Build.CPU_ABI : Build.SUPPORTED_ABIS[0];
            return this.bvs.cptp;
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return null;
        }
    }

    public ArrayList<HashMap<String, String>> getCamResolution() {
        CameraManager cameraManager;
        String[] strArr;
        CameraCharacteristics cameraCharacteristics;
        Size size;
        if (this.bvs.cmrsl != null) {
            return this.bvs.cmrsl;
        }
        try {
            if (Build.VERSION.SDK_INT < 21 || !checkPermission("android.permission.CAMERA") || (cameraManager = (CameraManager) this.context.getSystemService("camera")) == null || (strArr = (String[]) ReflectHelper.invokeInstanceMethod(cameraManager, Strings.getString(Opcodes.IAND), new Object[0])) == null || strArr.length <= 0) {
                return null;
            }
            ArrayList<HashMap<String, String>> arrayList = new ArrayList<>(strArr.length);
            for (String str : strArr) {
                if (str != null && (cameraCharacteristics = (CameraCharacteristics) ReflectHelper.invokeInstanceMethod(cameraManager, Strings.getString(127), str)) != null && (size = (Size) cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE)) != null) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("width", String.valueOf(size.getWidth()));
                    hashMap.put("height", String.valueOf(size.getHeight()));
                    arrayList.add(hashMap);
                }
            }
            this.bvs.cmrsl = arrayList;
            return arrayList;
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return null;
        }
    }

    public String getCarrier() {
        try {
            if (!"-2".equals(this.bvs.crir)) {
                return this.bvs.crir;
            }
            Object systemServiceSafe = getSystemServiceSafe("phone");
            if (systemServiceSafe == null) {
                this.bvs.crir = "-1";
                return this.bvs.crir;
            }
            String str = (String) ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(12), new Object[0]);
            if (TextUtils.isEmpty(str)) {
                str = "-1";
            }
            this.bvs.crir = str;
            return str;
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            this.bvs.crir = "-1";
            return this.bvs.crir;
        }
    }

    public String getCarrierName() {
        if (!"-1".equals(this.bvs.crirnm)) {
            return this.bvs.crirnm;
        }
        try {
            if (checkPermission("android.permission.READ_PHONE_STATE")) {
                Object systemServiceSafe = getSystemServiceSafe("phone");
                if (systemServiceSafe == null) {
                    this.bvs.crirnm = null;
                    return this.bvs.crirnm;
                }
                String str = (String) ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(13), new Object[0]);
                if (TextUtils.isEmpty(str)) {
                    str = null;
                }
                this.bvs.crirnm = str;
                return str;
            }
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
        return null;
    }

    public int getCdmaBid() {
        Object systemServiceSafe;
        Object invokeInstanceMethod;
        try {
            if (checkPermission("android.permission.ACCESS_COARSE_LOCATION") && (systemServiceSafe = getSystemServiceSafe("phone")) != null && (invokeInstanceMethod = ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(26), new Object[0])) != null && "CdmaCellLocation".equals(invokeInstanceMethod.getClass().getSimpleName())) {
                return ((Integer) ResHelper.forceCast(ReflectHelper.invokeInstanceMethod(invokeInstanceMethod, Strings.getString(58), new Object[0]), -1)).intValue();
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        return -1;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x005a  */
    /* JADX WARN: Removed duplicated region for block: B:17:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int getCdmaLat() {
        /*
            r5 = this;
            r0 = -1
            java.lang.String r1 = "android.permission.ACCESS_COARSE_LOCATION"
            boolean r1 = r5.checkPermission(r1)     // Catch: java.lang.Throwable -> L4b
            if (r1 == 0) goto L53
            java.lang.String r1 = "phone"
            java.lang.Object r1 = r5.getSystemServiceSafe(r1)     // Catch: java.lang.Throwable -> L4b
            if (r1 == 0) goto L53
            r2 = 26
            java.lang.String r2 = com.mob.tools.utils.Strings.getString(r2)     // Catch: java.lang.Throwable -> L4b
            r3 = 0
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch: java.lang.Throwable -> L4b
            java.lang.Object r1 = com.mob.tools.utils.ReflectHelper.invokeInstanceMethod(r1, r2, r4)     // Catch: java.lang.Throwable -> L4b
            if (r1 == 0) goto L53
            java.lang.String r2 = "CdmaCellLocation"
            java.lang.Class r4 = r1.getClass()     // Catch: java.lang.Throwable -> L4b
            java.lang.String r4 = r4.getSimpleName()     // Catch: java.lang.Throwable -> L4b
            boolean r2 = r2.equals(r4)     // Catch: java.lang.Throwable -> L4b
            if (r2 == 0) goto L53
            r2 = 56
            java.lang.String r2 = com.mob.tools.utils.Strings.getString(r2)     // Catch: java.lang.Throwable -> L4b
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch: java.lang.Throwable -> L4b
            java.lang.Object r1 = com.mob.tools.utils.ReflectHelper.invokeInstanceMethod(r1, r2, r3)     // Catch: java.lang.Throwable -> L4b
            java.lang.Integer r2 = java.lang.Integer.valueOf(r0)     // Catch: java.lang.Throwable -> L4b
            java.lang.Object r1 = com.mob.tools.utils.ResHelper.forceCast(r1, r2)     // Catch: java.lang.Throwable -> L4b
            java.lang.Integer r1 = (java.lang.Integer) r1     // Catch: java.lang.Throwable -> L4b
            int r1 = r1.intValue()     // Catch: java.lang.Throwable -> L4b
            goto L54
        L4b:
            r1 = move-exception
            com.mob.tools.log.NLog r2 = com.mob.tools.MobLog.getInstance()
            r2.d(r1)
        L53:
            r1 = -1
        L54:
            r2 = 2147483647(0x7fffffff, float:NaN)
            if (r1 != r2) goto L5a
            goto L5b
        L5a:
            r0 = r1
        L5b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.tools.utils.DeviceHelper.getCdmaLat():int");
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x005a  */
    /* JADX WARN: Removed duplicated region for block: B:17:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int getCdmaLon() {
        /*
            r5 = this;
            r0 = -1
            java.lang.String r1 = "android.permission.ACCESS_COARSE_LOCATION"
            boolean r1 = r5.checkPermission(r1)     // Catch: java.lang.Throwable -> L4b
            if (r1 == 0) goto L53
            java.lang.String r1 = "phone"
            java.lang.Object r1 = r5.getSystemServiceSafe(r1)     // Catch: java.lang.Throwable -> L4b
            if (r1 == 0) goto L53
            r2 = 26
            java.lang.String r2 = com.mob.tools.utils.Strings.getString(r2)     // Catch: java.lang.Throwable -> L4b
            r3 = 0
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch: java.lang.Throwable -> L4b
            java.lang.Object r1 = com.mob.tools.utils.ReflectHelper.invokeInstanceMethod(r1, r2, r4)     // Catch: java.lang.Throwable -> L4b
            if (r1 == 0) goto L53
            java.lang.String r2 = "CdmaCellLocation"
            java.lang.Class r4 = r1.getClass()     // Catch: java.lang.Throwable -> L4b
            java.lang.String r4 = r4.getSimpleName()     // Catch: java.lang.Throwable -> L4b
            boolean r2 = r2.equals(r4)     // Catch: java.lang.Throwable -> L4b
            if (r2 == 0) goto L53
            r2 = 57
            java.lang.String r2 = com.mob.tools.utils.Strings.getString(r2)     // Catch: java.lang.Throwable -> L4b
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch: java.lang.Throwable -> L4b
            java.lang.Object r1 = com.mob.tools.utils.ReflectHelper.invokeInstanceMethod(r1, r2, r3)     // Catch: java.lang.Throwable -> L4b
            java.lang.Integer r2 = java.lang.Integer.valueOf(r0)     // Catch: java.lang.Throwable -> L4b
            java.lang.Object r1 = com.mob.tools.utils.ResHelper.forceCast(r1, r2)     // Catch: java.lang.Throwable -> L4b
            java.lang.Integer r1 = (java.lang.Integer) r1     // Catch: java.lang.Throwable -> L4b
            int r1 = r1.intValue()     // Catch: java.lang.Throwable -> L4b
            goto L54
        L4b:
            r1 = move-exception
            com.mob.tools.log.NLog r2 = com.mob.tools.MobLog.getInstance()
            r2.d(r1)
        L53:
            r1 = -1
        L54:
            r2 = 2147483647(0x7fffffff, float:NaN)
            if (r1 != r2) goto L5a
            goto L5b
        L5a:
            r0 = r1
        L5b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.tools.utils.DeviceHelper.getCdmaLon():int");
    }

    public int getCdmaNid() {
        Object systemServiceSafe;
        Object invokeInstanceMethod;
        try {
            if (checkPermission("android.permission.ACCESS_COARSE_LOCATION") && (systemServiceSafe = getSystemServiceSafe("phone")) != null && (invokeInstanceMethod = ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(26), new Object[0])) != null && "CdmaCellLocation".equals(invokeInstanceMethod.getClass().getSimpleName())) {
                return ((Integer) ResHelper.forceCast(ReflectHelper.invokeInstanceMethod(invokeInstanceMethod, Strings.getString(60), new Object[0]), -1)).intValue();
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        return -1;
    }

    public int getCdmaSid() {
        Object systemServiceSafe;
        Object invokeInstanceMethod;
        try {
            if (checkPermission("android.permission.ACCESS_COARSE_LOCATION") && (systemServiceSafe = getSystemServiceSafe("phone")) != null && (invokeInstanceMethod = ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(26), new Object[0])) != null && "CdmaCellLocation".equals(invokeInstanceMethod.getClass().getSimpleName())) {
                return ((Integer) ResHelper.forceCast(ReflectHelper.invokeInstanceMethod(invokeInstanceMethod, Strings.getString(59), new Object[0]), -1)).intValue();
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        return -1;
    }

    public int getCellId() {
        Object systemServiceSafe;
        Object invokeInstanceMethod;
        try {
            if (checkPermission("android.permission.ACCESS_COARSE_LOCATION") && (systemServiceSafe = getSystemServiceSafe("phone")) != null && (invokeInstanceMethod = ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(26), new Object[0])) != null && !"CdmaCellLocation".equals(invokeInstanceMethod.getClass().getSimpleName())) {
                return ((Integer) ResHelper.forceCast(ReflectHelper.invokeInstanceMethod(invokeInstanceMethod, Strings.getString(27), new Object[0]), -1)).intValue();
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        return -1;
    }

    public int getCellLac() {
        Object systemServiceSafe;
        Object invokeInstanceMethod;
        try {
            if (checkPermission("android.permission.ACCESS_COARSE_LOCATION") && (systemServiceSafe = getSystemServiceSafe("phone")) != null && (invokeInstanceMethod = ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(26), new Object[0])) != null && !"CdmaCellLocation".equals(invokeInstanceMethod.getClass().getSimpleName())) {
                return ((Integer) ResHelper.forceCast(ReflectHelper.invokeInstanceMethod(invokeInstanceMethod, Strings.getString(28), new Object[0]), -1)).intValue();
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        return -1;
    }

    public String getCharAndNumr(int i) {
        long currentTimeMillis = System.currentTimeMillis() ^ SystemClock.elapsedRealtime();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(currentTimeMillis);
        Random random = new Random();
        for (int i2 = 0; i2 < i; i2++) {
            if ("char".equalsIgnoreCase(random.nextInt(2) % 2 == 0 ? "char" : "num")) {
                stringBuffer.insert(i2 + 1, (char) (random.nextInt(26) + 97));
            } else {
                stringBuffer.insert(stringBuffer.length(), random.nextInt(10));
            }
        }
        return stringBuffer.toString().substring(0, 40);
    }

    public HashMap<String, Object> getCurrentWifiInfo() {
        Object systemServiceSafe;
        Object invokeInstanceMethod;
        String str = null;
        try {
            if (checkPermission("android.permission.ACCESS_WIFI_STATE") && (systemServiceSafe = getSystemServiceSafe("wifi")) != null && (invokeInstanceMethod = ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(2), new Object[0])) != null) {
                HashMap<String, Object> hashMap = new HashMap<>();
                try {
                    hashMap.put(Dic.BSSID, (String) ReflectHelper.invokeInstanceMethod(invokeInstanceMethod, Strings.getString(4), new Object[0]));
                } catch (Throwable unused) {
                }
                try {
                    String str2 = (String) ReflectHelper.invokeInstanceMethod(invokeInstanceMethod, Strings.getString(3), new Object[0]);
                    if (str2 != null) {
                        str = str2.replace("\"", "");
                    }
                    hashMap.put(Dic.SSID, str);
                } catch (Throwable unused2) {
                }
                try {
                    hashMap.put("ip", Integer.valueOf(((Integer) ReflectHelper.invokeInstanceMethod(invokeInstanceMethod, Strings.getString(79), new Object[0])).intValue()));
                } catch (Throwable unused3) {
                }
                try {
                    hashMap.put(Dic.WLAN_MAC, getMacAddress());
                } catch (Throwable unused4) {
                }
                try {
                    hashMap.put("hidden", Boolean.valueOf(((Boolean) ReflectHelper.invokeInstanceMethod(invokeInstanceMethod, Strings.getString(80), new Object[0])).booleanValue()));
                } catch (Throwable unused5) {
                }
                try {
                    hashMap.put(Dic.SPEED, Integer.valueOf(((Integer) ReflectHelper.invokeInstanceMethod(invokeInstanceMethod, Strings.getString(81), new Object[0])).intValue()));
                } catch (Throwable unused6) {
                }
                try {
                    hashMap.put("networkId", Integer.valueOf(((Integer) ReflectHelper.invokeInstanceMethod(invokeInstanceMethod, Strings.getString(60), new Object[0])).intValue()));
                } catch (Throwable unused7) {
                }
                try {
                    hashMap.put("level", Integer.valueOf(((Integer) ReflectHelper.invokeInstanceMethod(invokeInstanceMethod, Strings.getString(62), new Object[0])).intValue()));
                } catch (Throwable unused8) {
                }
                try {
                    hashMap.put("frequency", Integer.valueOf(((Integer) ReflectHelper.invokeInstanceMethod(invokeInstanceMethod, Strings.getString(82), new Object[0])).intValue()));
                } catch (Throwable unused9) {
                }
                return hashMap;
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        return null;
    }

    public int getDataNtType() {
        return NtFetcher.getInstance(this.context).getDtNtType();
    }

    public HashMap<String, Object> getDefaultIM() {
        if (this.bvs.dfim != null) {
            return this.bvs.dfim;
        }
        this.bvs.dfim = new HashMap<>();
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            String defaultIMPkg = getDefaultIMPkg();
            hashMap.put(IMAPStore.ID_NAME, getAppName(defaultIMPkg));
            hashMap.put("pkg", defaultIMPkg);
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        this.bvs.dfim.putAll(hashMap);
        return hashMap;
    }

    public String getDefaultIMPkg() {
        if (!TextUtils.isEmpty(this.bvs.dfimpkg)) {
            return this.bvs.dfimpkg;
        }
        String str = null;
        try {
            String string = Settings.Secure.getString(this.context.getContentResolver(), "default_input_method");
            if (!TextUtils.isEmpty(string)) {
                str = string.split(FileUriModel.SCHEME)[0];
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        this.bvs.dfimpkg = str;
        return str;
    }

    public String getDefaultResolvePkg(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Intent intent = new Intent();
        intent.setData(Uri.parse(str));
        ResolveInfo resolveActivity = this.context.getPackageManager().resolveActivity(intent, 65536);
        if (resolveActivity == null || resolveActivity.activityInfo == null) {
            return null;
        }
        return resolveActivity.activityInfo.packageName;
    }

    public String getDetailNetworkTypeForStatic() {
        try {
            String lowerCase = getNetworkType().toLowerCase();
            if (!TextUtils.isEmpty(lowerCase) && !SchedulerSupport.NONE.equals(lowerCase)) {
                return lowerCase.startsWith("wifi") ? "wifi" : lowerCase.startsWith("5g") ? "5g" : lowerCase.startsWith("4g") ? "4g" : lowerCase.startsWith("3g") ? "3g" : lowerCase.startsWith("2g") ? "2g" : lowerCase.startsWith("bluetooth") ? "bluetooth" : lowerCase;
            }
            return SchedulerSupport.NONE;
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return SchedulerSupport.NONE;
        }
    }

    public String getDeviceData() {
        try {
            String str = getModel() + "|" + getOSVersionInt() + "|" + getManufacturer() + "|" + getCarrier() + "|" + getScreenSize();
            String deviceKey = getDeviceKey();
            if (deviceKey == null) {
                deviceKey = "";
            } else if (deviceKey.length() > 16) {
                deviceKey = deviceKey.substring(0, 16);
            }
            return Base64AES(str, deviceKey);
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return "";
        }
    }

    public String getDeviceDataNotAES() {
        return getModel() + "|" + getOSVersionInt() + "|" + getManufacturer() + "|" + getCarrier() + "|" + getScreenSize();
    }

    public String getDeviceId() {
        String imei = getIMEI();
        return (!TextUtils.isEmpty(imei) || Build.VERSION.SDK_INT < 9) ? imei : getSerialno();
    }

    public String getDeviceKey() {
        String str;
        String str2;
        Throwable th;
        if (!TextUtils.isEmpty(this.cacheDeviceKey)) {
            return this.cacheDeviceKey;
        }
        String str3 = null;
        try {
            str = getDeviceKeyWithDuid("comm/dbs/.duid");
        } catch (Throwable th2) {
            MobLog.getInstance().w(th2);
            str = null;
        }
        if (TextUtils.isEmpty(str) || str.length() < 40) {
            str = genDeviceKey();
        }
        if (!TextUtils.isEmpty(str) && str.length() >= 40) {
            this.cacheDeviceKey = str.trim();
            return this.cacheDeviceKey;
        }
        try {
            str3 = getLocalDeviceKey();
        } catch (Throwable th3) {
            MobLog.getInstance().w(th3);
        }
        if (!TextUtils.isEmpty(str3) && str3.length() >= 40) {
            this.cacheDeviceKey = str3.trim();
            return this.cacheDeviceKey;
        }
        if (TextUtils.isEmpty(str3) || str3.length() < 40) {
            str3 = getCharAndNumr(40);
        }
        if (str3 == null) {
            return str3;
        }
        try {
            str2 = str3.trim();
            try {
                saveLocalDeviceKey(str2);
                return str2;
            } catch (Throwable th4) {
                th = th4;
                MobLog.getInstance().w(th);
                return str2;
            }
        } catch (Throwable th5) {
            str2 = str3;
            th = th5;
        }
    }

    public HashMap<String, Object> getDeviceMemUsage() {
        BufferedReader bufferedReader;
        HashMap<String, Object> hashMap = new HashMap<>();
        BufferedReader bufferedReader2 = null;
        try {
            try {
                try {
                    bufferedReader = new BufferedReader(new FileReader(Strings.getString(125)));
                    while (true) {
                        try {
                            String readLine = bufferedReader.readLine();
                            if (readLine == null) {
                                break;
                            }
                            String[] split = readLine.split("\\s+");
                            if (split != null && split.length > 1) {
                                String str = split[0];
                                long parseLong = Long.parseLong(split[1]) * PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
                                if ("MemTotal:".equals(str)) {
                                    hashMap.put("totalMemorySize", Long.valueOf(parseLong));
                                } else if ("MemFree:".equals(str)) {
                                    hashMap.put("freeMemorySize", Long.valueOf(parseLong));
                                } else if ("MemAvailable:".equals(str)) {
                                    hashMap.put("availableMemorySize", Long.valueOf(parseLong));
                                } else if ("Active:".equals(str)) {
                                    hashMap.put("activeMemorySize", Long.valueOf(parseLong));
                                } else if ("Inactive:".equals(str)) {
                                    hashMap.put("inactiveMemorySize", Long.valueOf(parseLong));
                                }
                            }
                        } catch (Throwable th) {
                            th = th;
                            bufferedReader2 = bufferedReader;
                            MobLog.getInstance().d(th);
                            if (bufferedReader2 != null) {
                                bufferedReader2.close();
                            }
                            return hashMap;
                        }
                    }
                    bufferedReader.close();
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Throwable th3) {
                th = th3;
                bufferedReader = bufferedReader2;
            }
        } catch (Throwable th4) {
            MobLog.getInstance().d(th4);
        }
        return hashMap;
    }

    public String getDeviceType() {
        if (!TextUtils.isEmpty(this.bvs.dvtp)) {
            return this.bvs.dvtp;
        }
        try {
            UiModeManager uiModeManager = (UiModeManager) getSystemServiceSafe("uimode");
            if (uiModeManager != null) {
                switch (uiModeManager.getCurrentModeType()) {
                    case 1:
                        this.bvs.dvtp = "NO_UI";
                        break;
                    case 2:
                        this.bvs.dvtp = "DESK";
                        break;
                    case 3:
                        this.bvs.dvtp = "CAR";
                        break;
                    case 4:
                        this.bvs.dvtp = "TELEVISION";
                        break;
                    case 5:
                        this.bvs.dvtp = "APPLIANCE";
                        break;
                    case 6:
                        this.bvs.dvtp = "WATCH";
                        break;
                    case 7:
                        this.bvs.dvtp = "VRHEADSET";
                        break;
                    default:
                        this.bvs.dvtp = "UNDEFINED";
                        break;
                }
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            this.bvs.dvtp = "UNDEFINED";
        }
        return this.bvs.dvtp;
    }

    public long getElapsedTime() {
        try {
            return SystemClock.elapsedRealtime();
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return 0L;
        }
    }

    public String getFlavor() {
        if (!TextUtils.isEmpty(this.bvs.flv)) {
            return this.bvs.flv;
        }
        String str = null;
        try {
            str = getSystemProperties(Strings.getString(119));
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        this.bvs.flv = str;
        return str;
    }

    public ArrayList<HashMap<String, String>> getIA(boolean z) {
        return getIA(z, true);
    }

    public ArrayList<HashMap<String, String>> getIA(boolean z, boolean z2) {
        return getAL(false, z, z2);
    }

    public HashMap<String, Object> getIInfo() {
        return getIInfo(false);
    }

    public HashMap<String, Object> getIInfo(boolean z) {
        try {
            Object systemServiceSafe = getSystemServiceSafe("phone");
            if (systemServiceSafe == null) {
                return null;
            }
            HashMap<String, Object> hashMap = new HashMap<>();
            int i = 83;
            int i2 = 84;
            hashMap.put(Strings.getString(83), invokeInstanceMethod(systemServiceSafe, Strings.getString(84), new Object[0]));
            hashMap.put(Strings.getString(85), invokeInstanceMethod(systemServiceSafe, Strings.getString(86), new Object[0]));
            hashMap.put(Strings.getString(87), invokeInstanceMethod(systemServiceSafe, Strings.getString(88), new Object[0]));
            if (Build.VERSION.SDK_INT >= 23) {
                hashMap.put(Strings.getString(89), invokeInstanceMethod(systemServiceSafe, Strings.getString(90), new Object[0]));
            }
            if (!checkPermission("android.permission.READ_PHONE_STATE")) {
                return hashMap;
            }
            if (Build.VERSION.SDK_INT < 29) {
                if (Build.VERSION.SDK_INT >= 26) {
                    hashMap.put(Dic.IMEI, invokeInstanceMethod(systemServiceSafe, Strings.getString(92), new Object[0]));
                    hashMap.put(Dic.MEID, invokeInstanceMethod(systemServiceSafe, Strings.getString(94), new Object[0]));
                } else {
                    hashMap.put(Dic.IMEI, invokeInstanceMethod(systemServiceSafe, Strings.getString(95), new Object[0]));
                }
                hashMap.put(Dic.IMSI, invokeInstanceMethod(systemServiceSafe, Strings.getString(25), new Object[0]));
                hashMap.put(Dic.SIM_SERIAL_NUMBER, invokeInstanceMethod(systemServiceSafe, Strings.getString(14), new Object[0]));
            }
            if (Build.VERSION.SDK_INT >= 24) {
                hashMap.put(Strings.getString(100), Integer.valueOf(getInstance(this.context).getDataNtType()));
            }
            if ("-1".equals(this.swVer) || !isSensitiveDevice()) {
                this.swVer = (String) invokeInstanceMethod(systemServiceSafe, Strings.getString(98), new Object[0]);
            }
            if (!"-1".equals(this.swVer) && !TextUtils.isEmpty(this.swVer)) {
                hashMap.put(Strings.getString(97), this.swVer);
            }
            if (Build.VERSION.SDK_INT >= 22) {
                Object systemServiceSafe2 = getSystemServiceSafe(Strings.getString(114));
                if (this.sActCnt == -1 || !isSensitiveDevice()) {
                    this.sActCnt = ((Integer) invokeInstanceMethod(systemServiceSafe2, Strings.getString(103), new Object[0])).intValue();
                }
                if (this.sActCnt != -1) {
                    hashMap.put(Strings.getString(102), Integer.valueOf(this.sActCnt));
                }
                if (this.sActList == null || !isSensitiveDevice()) {
                    this.sActList = (List) invokeInstanceMethod(systemServiceSafe2, Strings.getString(104), new Object[0]);
                }
                if (this.sActList != null) {
                    int size = this.sActList.size();
                    ArrayList arrayList = new ArrayList();
                    int i3 = 0;
                    while (i3 < size) {
                        Object obj = this.sActList.get(i3);
                        HashMap hashMap2 = new HashMap();
                        int intValue = ((Integer) invokeInstanceMethod(obj, Strings.getString(105), new Object[0])).intValue();
                        hashMap2.put(Strings.getString(106), invokeInstanceMethod(obj, Strings.getString(107), new Object[0]));
                        if (z) {
                            hashMap2.put(Strings.getString(108), invokeInstanceMethod(obj, Strings.getString(109), new Object[0]));
                        }
                        hashMap2.put(Strings.getString(110), invokeInstanceMethod(obj, Strings.getString(111), new Object[0]));
                        int intValue2 = ((Integer) invokeInstanceMethod(obj, Strings.getString(112), new Object[0])).intValue();
                        hashMap2.put(Strings.getString(i), invokeInstanceMethod(systemServiceSafe, Strings.getString(i2), new Object[]{Integer.valueOf(intValue2)}, new Class[]{Integer.TYPE}));
                        if (Build.VERSION.SDK_INT < 29) {
                            if (Build.VERSION.SDK_INT >= 26) {
                                hashMap2.put(Dic.IMEI, invokeInstanceMethod(systemServiceSafe, Strings.getString(92), new Object[]{Integer.valueOf(intValue2)}, new Class[]{Integer.TYPE}));
                                hashMap2.put(Dic.MEID, invokeInstanceMethod(systemServiceSafe, Strings.getString(94), new Object[]{Integer.valueOf(intValue2)}, new Class[]{Integer.TYPE}));
                            } else if (Build.VERSION.SDK_INT >= 23) {
                                hashMap2.put(Dic.IMEI, invokeInstanceMethod(systemServiceSafe, Strings.getString(95), new Object[]{Integer.valueOf(intValue2)}, new Class[]{Integer.TYPE}));
                            } else {
                                hashMap2.put(Dic.IMEI, invokeInstanceMethod(systemServiceSafe, Strings.getString(95), new Object[0]));
                            }
                            hashMap2.put(Dic.IMSI, invokeInstanceMethod(systemServiceSafe, Strings.getString(25), new Object[]{Integer.valueOf(intValue)}, new Class[]{Integer.TYPE}));
                            hashMap2.put(Dic.SIM_SERIAL_NUMBER, invokeInstanceMethod(systemServiceSafe, Strings.getString(14), new Object[]{Integer.valueOf(intValue)}, new Class[]{Integer.TYPE}));
                        }
                        arrayList.add(hashMap2);
                        i3++;
                        i = 83;
                        i2 = 84;
                    }
                    hashMap.put(Dic.SIM_LIST, arrayList);
                }
            }
            return hashMap;
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return null;
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(7:6|(2:7|8)|(3:13|14|(1:16)(4:17|18|19|(4:21|(4:26|(1:30)|31|32)|33|34)(4:35|(1:39)|40|41)))|47|18|19|(0)(0)) */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00ad, code lost:
    
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00b3, code lost:
    
        com.mob.tools.MobLog.getInstance().w(r0);
     */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0047 A[Catch: Throwable -> 0x00ad, TryCatch #0 {Throwable -> 0x00ad, blocks: (B:19:0x0040, B:21:0x0047, B:23:0x0051, B:26:0x0058, B:28:0x0068, B:30:0x006c, B:31:0x0074, B:33:0x0078, B:35:0x007d, B:37:0x0089, B:39:0x008f, B:40:0x0092), top: B:18:0x0040 }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x007d A[Catch: Throwable -> 0x00ad, TryCatch #0 {Throwable -> 0x00ad, blocks: (B:19:0x0040, B:21:0x0047, B:23:0x0051, B:26:0x0058, B:28:0x0068, B:30:0x006c, B:31:0x0074, B:33:0x0078, B:35:0x007d, B:37:0x0089, B:39:0x008f, B:40:0x0092), top: B:18:0x0040 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.String getIMEI() {
        /*
            r6 = this;
            java.lang.String r0 = r6.ime
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto Lb
            java.lang.String r0 = r6.ime
            return r0
        Lb:
            r0 = 0
            java.lang.String r1 = "android.permission.READ_PHONE_STATE"
            boolean r1 = r6.checkPermission(r1)     // Catch: java.lang.Throwable -> Laf
            if (r1 == 0) goto L3f
            int r1 = android.os.Build.VERSION.SDK_INT     // Catch: java.lang.Throwable -> Laf
            r2 = 29
            if (r1 >= r2) goto L3f
            java.lang.String r1 = "phone"
            java.lang.Object r1 = r6.getSystemServiceSafe(r1)     // Catch: java.lang.Throwable -> L33
            if (r1 != 0) goto L23
            return r0
        L23:
            r2 = 8
            java.lang.String r2 = com.mob.tools.utils.Strings.getString(r2)     // Catch: java.lang.Throwable -> L33
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch: java.lang.Throwable -> L33
            java.lang.Object r1 = com.mob.tools.utils.ReflectHelper.invokeInstanceMethod(r1, r2, r3)     // Catch: java.lang.Throwable -> L33
            java.lang.String r1 = (java.lang.String) r1     // Catch: java.lang.Throwable -> L33
            goto L40
        L33:
            r1 = move-exception
            com.mob.tools.log.NLog r2 = com.mob.tools.MobLog.getInstance()     // Catch: java.lang.Throwable -> Laf
            java.lang.String r1 = r1.getMessage()     // Catch: java.lang.Throwable -> Laf
            r2.w(r1)     // Catch: java.lang.Throwable -> Laf
        L3f:
            r1 = r0
        L40:
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch: java.lang.Throwable -> Lad
            r3 = 1
            if (r2 == 0) goto L7d
            android.content.Context r2 = r6.context     // Catch: java.lang.Throwable -> Lad
            java.lang.String r4 = "comm/.di"
            java.io.File r2 = com.mob.tools.utils.ResHelper.getCacheRootFile(r2, r4)     // Catch: java.lang.Throwable -> Lad
            if (r2 == 0) goto L78
            boolean r4 = r2.exists()     // Catch: java.lang.Throwable -> Lad
            if (r4 != 0) goto L58
            goto L78
        L58:
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> Lad
            r3.<init>(r2)     // Catch: java.lang.Throwable -> Lad
            java.io.ObjectInputStream r2 = new java.io.ObjectInputStream     // Catch: java.lang.Throwable -> Lad
            r2.<init>(r3)     // Catch: java.lang.Throwable -> Lad
            java.lang.Object r3 = r2.readObject()     // Catch: java.lang.Throwable -> Lad
            if (r3 == 0) goto L74
            boolean r4 = r3 instanceof char[]     // Catch: java.lang.Throwable -> Lad
            if (r4 == 0) goto L74
            char[] r3 = (char[]) r3     // Catch: java.lang.Throwable -> Lad
            char[] r3 = (char[]) r3     // Catch: java.lang.Throwable -> Lad
            java.lang.String r0 = java.lang.String.valueOf(r3)     // Catch: java.lang.Throwable -> Lad
        L74:
            r2.close()     // Catch: java.lang.Throwable -> Lad
            return r0
        L78:
            java.lang.String r0 = r6.getWAbcd(r3)     // Catch: java.lang.Throwable -> Lad
            return r0
        L7d:
            r6.ime = r1     // Catch: java.lang.Throwable -> Lad
            android.content.Context r0 = r6.context     // Catch: java.lang.Throwable -> Lad
            java.lang.String r2 = "comm/.di"
            java.io.File r0 = com.mob.tools.utils.ResHelper.getCacheRootFile(r0, r2)     // Catch: java.lang.Throwable -> Lad
            if (r0 == 0) goto L92
            boolean r2 = r0.exists()     // Catch: java.lang.Throwable -> Lad
            if (r2 == 0) goto L92
            r0.delete()     // Catch: java.lang.Throwable -> Lad
        L92:
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> Lad
            r2.<init>(r0)     // Catch: java.lang.Throwable -> Lad
            java.io.ObjectOutputStream r0 = new java.io.ObjectOutputStream     // Catch: java.lang.Throwable -> Lad
            r0.<init>(r2)     // Catch: java.lang.Throwable -> Lad
            char[] r2 = r1.toCharArray()     // Catch: java.lang.Throwable -> Lad
            r0.writeObject(r2)     // Catch: java.lang.Throwable -> Lad
            r0.flush()     // Catch: java.lang.Throwable -> Lad
            r0.close()     // Catch: java.lang.Throwable -> Lad
            r6.saveWabcd(r1, r3)     // Catch: java.lang.Throwable -> Lad
            goto Lba
        Lad:
            r0 = move-exception
            goto Lb3
        Laf:
            r1 = move-exception
            r5 = r1
            r1 = r0
            r0 = r5
        Lb3:
            com.mob.tools.log.NLog r2 = com.mob.tools.MobLog.getInstance()
            r2.w(r0)
        Lba:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.tools.utils.DeviceHelper.getIMEI():java.lang.String");
    }

    public ArrayList<HashMap<String, Object>> getIMList() {
        if (this.bvs.imlst != null) {
            return this.bvs.imlst;
        }
        this.bvs.imlst = new ArrayList<>();
        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();
        try {
            for (InputMethodInfo inputMethodInfo : ((InputMethodManager) getSystemServiceSafe("input_method")).getInputMethodList()) {
                if (inputMethodInfo != null) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put(IMAPStore.ID_NAME, inputMethodInfo.loadLabel(this.context.getPackageManager()));
                    hashMap.put("pkg", inputMethodInfo.getPackageName());
                    arrayList.add(hashMap);
                }
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        this.bvs.imlst.addAll(arrayList);
        return arrayList;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0067 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0068  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.String getIMSI() {
        /*
            r6 = this;
            com.mob.tools.utils.BVS r0 = r6.bvs
            java.lang.String r0 = r0.isi
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto Lf
            com.mob.tools.utils.BVS r0 = r6.bvs
            java.lang.String r0 = r0.isi
            return r0
        Lf:
            r0 = 0
            r1 = 0
            int r2 = android.os.Build.VERSION.SDK_INT     // Catch: java.lang.Throwable -> L37
            r3 = 29
            if (r2 >= r3) goto L43
            java.lang.String r2 = "android.permission.READ_PHONE_STATE"
            boolean r2 = r6.checkPermission(r2)     // Catch: java.lang.Throwable -> L37
            if (r2 == 0) goto L43
            java.lang.String r2 = "phone"
            java.lang.Object r2 = r6.getSystemServiceSafe(r2)     // Catch: java.lang.Throwable -> L37
            if (r2 != 0) goto L28
            return r1
        L28:
            r3 = 25
            java.lang.String r3 = com.mob.tools.utils.Strings.getString(r3)     // Catch: java.lang.Throwable -> L37
            java.lang.Object[] r4 = new java.lang.Object[r0]     // Catch: java.lang.Throwable -> L37
            java.lang.Object r2 = com.mob.tools.utils.ReflectHelper.invokeInstanceMethod(r2, r3, r4)     // Catch: java.lang.Throwable -> L37
            java.lang.String r2 = (java.lang.String) r2     // Catch: java.lang.Throwable -> L37
            goto L44
        L37:
            r2 = move-exception
            com.mob.tools.log.NLog r3 = com.mob.tools.MobLog.getInstance()
            java.lang.String r2 = r2.getMessage()
            r3.w(r2)
        L43:
            r2 = r1
        L44:
            r3 = 15
            if (r2 == 0) goto L4e
            int r4 = r2.length()
            if (r4 >= r3) goto L61
        L4e:
            java.lang.String[] r4 = r6.queryIMSI()
            if (r4 == 0) goto L61
            int r5 = r4.length
            if (r5 <= 0) goto L61
            r5 = r4[r0]
            int r5 = r5.length()
            if (r5 < r3) goto L61
            r2 = r4[r0]
        L61:
            boolean r0 = android.text.TextUtils.isEmpty(r2)
            if (r0 == 0) goto L68
            return r1
        L68:
            com.mob.tools.utils.BVS r0 = r6.bvs
            r0.isi = r2
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.tools.utils.DeviceHelper.getIMSI():java.lang.String");
    }

    public String getIPAddress() {
        try {
            if (!checkPermission("android.permission.INTERNET")) {
                return "0.0.0.0";
            }
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress nextElement = inetAddresses.nextElement();
                    if (!nextElement.isLoopbackAddress() && (nextElement instanceof Inet4Address)) {
                        return nextElement.getHostAddress();
                    }
                }
            }
            return "0.0.0.0";
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return "0.0.0.0";
        }
    }

    public List<Object[]> getIntent(String str) throws Throwable {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Intent intent = new Intent();
        Uri parse = Uri.parse(str);
        intent.setData(parse);
        List<ResolveInfo> queryIntentServices = this.context.getPackageManager().queryIntentServices(intent, 0);
        if (queryIntentServices == null || queryIntentServices.isEmpty()) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (ResolveInfo resolveInfo : queryIntentServices) {
            Intent intent2 = new Intent();
            intent2.setData(parse);
            intent2.setComponent(new ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name));
            arrayList.add(new Object[]{resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name, intent2});
        }
        return arrayList;
    }

    public List<Object[]> getIntentA(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Intent intent = new Intent();
        Uri parse = Uri.parse(str);
        intent.setData(parse);
        List<ResolveInfo> queryIntentActivities = this.context.getPackageManager().queryIntentActivities(intent, 0);
        if (queryIntentActivities == null || queryIntentActivities.isEmpty()) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (ResolveInfo resolveInfo : queryIntentActivities) {
            Intent intent2 = new Intent();
            intent2.setFlags(276824064);
            intent2.setData(parse);
            String signMD5 = getSignMD5(resolveInfo.activityInfo.packageName);
            intent2.setComponent(new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name));
            arrayList.add(new Object[]{resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name, intent2, signMD5, Boolean.valueOf(resolveInfo.activityInfo.exported)});
        }
        return arrayList;
    }

    public List<Object[]> getIntentSP(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Intent intent = new Intent();
        intent.setPackage(str);
        List<ResolveInfo> queryIntentServices = this.context.getPackageManager().queryIntentServices(intent, 0);
        if (queryIntentServices == null || queryIntentServices.isEmpty()) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (ResolveInfo resolveInfo : queryIntentServices) {
            Intent intent2 = new Intent();
            intent2.setComponent(new ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name));
            arrayList.add(new Object[]{resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name, intent2, getSignMD5(resolveInfo.serviceInfo.packageName)});
        }
        return arrayList;
    }

    public String getLN() {
        Object systemServiceSafe;
        try {
            if (checkPermission("android.permission.READ_PHONE_STATE")) {
                if (("-1".equals(this.ln) || !isSensitiveDevice()) && (systemServiceSafe = getSystemServiceSafe("phone")) != null) {
                    this.ln = (String) ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(15), new Object[0]);
                    return this.ln;
                }
                return this.ln;
            }
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
        return this.ln;
    }

    public ArrayList<HashMap<String, Object>> getLocalIpInfo() {
        if (this.bvs.lcip != null && !this.bvs.lcip.isEmpty()) {
            return this.bvs.lcip;
        }
        try {
            if (!checkPermission("android.permission.INTERNET")) {
                return null;
            }
            ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();
            ArrayList<NetworkInterface> list = Collections.list(NetworkInterface.getNetworkInterfaces());
            HashMap<String, String> listNetworkHardware = listNetworkHardware();
            for (NetworkInterface networkInterface : list) {
                HashMap<String, Object> hashMap = new HashMap<>();
                String name = networkInterface.getName();
                hashMap.put(IMAPStore.ID_NAME, name);
                if (listNetworkHardware == null || listNetworkHardware.isEmpty() || !listNetworkHardware.containsKey(name)) {
                    hashMap.put(Dic.MAC, byteToHex(networkInterface.getHardwareAddress()));
                } else {
                    hashMap.put(Dic.MAC, listNetworkHardware.get(name));
                }
                List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();
                if (interfaceAddresses != null && interfaceAddresses.size() > 0) {
                    ArrayList arrayList2 = new ArrayList();
                    for (InterfaceAddress interfaceAddress : interfaceAddresses) {
                        HashMap hashMap2 = new HashMap();
                        InetAddress address = interfaceAddress.getAddress();
                        hashMap2.put("haddr", address.getHostAddress());
                        hashMap2.put("hname", address.getHostName());
                        hashMap2.put("lp", Boolean.valueOf(address.isLoopbackAddress()));
                        hashMap2.put("addr", byteToHex(address.getAddress()));
                        hashMap2.put("len", Integer.valueOf(address.getAddress().length));
                        InetAddress broadcast = interfaceAddress.getBroadcast();
                        if (broadcast != null) {
                            HashMap hashMap3 = new HashMap();
                            hashMap3.put("haddrB", broadcast.getHostAddress());
                            hashMap3.put("hnameB", broadcast.getHostName());
                            hashMap3.put("lpB", Boolean.valueOf(broadcast.isLoopbackAddress()));
                            hashMap3.put("addrB", byteToHex(broadcast.getAddress()));
                            hashMap3.put("lenB", Integer.valueOf(broadcast.getAddress().length));
                            hashMap2.put("broadcast", hashMap3);
                        }
                        arrayList2.add(hashMap2);
                    }
                    hashMap.put("inets", arrayList2);
                    arrayList.add(hashMap);
                }
            }
            this.bvs.lcip = new ArrayList<>();
            this.bvs.lcip.addAll(arrayList);
            return arrayList;
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return null;
        }
    }

    public Location getLocation(int i, int i2, boolean z) {
        try {
            if (checkPermission("android.permission.ACCESS_FINE_LOCATION") || (Build.VERSION.SDK_INT >= 29 && checkPermission("android.permission.ACCESS_BACKGROUND_LOCATION"))) {
                return LHelper.getInstance().getLocation(this.context, i, i2, z);
            }
            return null;
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return null;
        }
    }

    public String getMCC() {
        if (!TextUtils.isEmpty(this.bvs.mccid)) {
            return this.bvs.mccid;
        }
        String imsi = getIMSI();
        if (imsi != null && imsi.length() >= 3) {
            this.bvs.mccid = imsi.substring(0, 3);
        }
        return this.bvs.mccid;
    }

    public String getMIUIVersion() {
        if (!TextUtils.isEmpty(this.bufUiVersion)) {
            return this.bufUiVersion;
        }
        String systemProperties = getSystemProperties(Strings.getString(65));
        if (TextUtils.isEmpty(systemProperties)) {
            systemProperties = getSystemProperties(Strings.getString(140));
        }
        if (TextUtils.isEmpty(systemProperties)) {
            systemProperties = getSystemProperties(Strings.getString(66));
        }
        if (TextUtils.isEmpty(systemProperties)) {
            systemProperties = getSystemProperties(Strings.getString(67));
        }
        if (TextUtils.isEmpty(systemProperties)) {
            systemProperties = getSystemProperties(Strings.getString(135));
        }
        if (TextUtils.isEmpty(systemProperties)) {
            systemProperties = getSystemProperties(Strings.getString(136));
        }
        if (TextUtils.isEmpty(systemProperties)) {
            systemProperties = getSystemProperties(Strings.getString(141));
        }
        if (TextUtils.isEmpty(systemProperties)) {
            systemProperties = getSystemProperties(Strings.getString(142));
        }
        if (TextUtils.isEmpty(systemProperties)) {
            systemProperties = getSystemProperties(Strings.getString(143));
        }
        if (TextUtils.isEmpty(systemProperties)) {
            systemProperties = getSystemProperties(Strings.getString(CameraInterface.TYPE_RECORDER));
        }
        if (TextUtils.isEmpty(systemProperties)) {
            systemProperties = getSystemProperties(Strings.getString(CameraInterface.TYPE_CAPTURE));
        }
        if (TextUtils.isEmpty(systemProperties)) {
            systemProperties = getSystemProperties(Strings.getString(146));
        }
        if (TextUtils.isEmpty(systemProperties)) {
            systemProperties = getSystemProperties(Strings.getString(139));
        }
        if (TextUtils.isEmpty(systemProperties)) {
            systemProperties = getSystemProperties(Strings.getString(69));
        }
        this.bufUiVersion = systemProperties;
        return systemProperties;
    }

    public String getMNC() {
        if (!TextUtils.isEmpty(this.bvs.mncid)) {
            return this.bvs.mncid;
        }
        String imsi = getIMSI();
        if (imsi != null && imsi.length() >= 5) {
            this.bvs.mncid = imsi.substring(3, 5);
        }
        return this.bvs.mncid;
    }

    public String getMacAddress() {
        if (!TextUtils.isEmpty(this.wfMc)) {
            return this.wfMc;
        }
        String localWifiMac = getLocalWifiMac();
        if (!TextUtils.isEmpty(localWifiMac) && checkMacIsValid(localWifiMac)) {
            this.wfMc = localWifiMac;
            return localWifiMac;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            String wlanMac = getWlanMac();
            if (wlanMac == null || TextUtils.isEmpty(wlanMac.trim())) {
                this.wfMc = getWifiMac();
                return this.wfMc;
            }
            this.wfMc = wlanMac.trim();
            return this.wfMc;
        }
        String wifiMac = getWifiMac();
        if (wifiMac == null || !checkMacIsValid(wifiMac)) {
            this.wfMc = getWlanMac();
            return this.wfMc;
        }
        this.wfMc = wifiMac.trim();
        saveLocalWifiMac(this.wfMc);
        return this.wfMc;
    }

    public String getManufacturer() {
        if (!TextUtils.isEmpty(this.bvs.manft)) {
            return this.bvs.manft;
        }
        this.bvs.manft = Build.MANUFACTURER;
        return this.bvs.manft;
    }

    public HashMap<String, Long> getMemoryInfo() {
        HashMap<String, Long> hashMap = new HashMap<>();
        hashMap.put("available", -1L);
        hashMap.put(FileDownloadModel.TOTAL, -1L);
        hashMap.put("isLow", -1L);
        hashMap.put("threshold", -1L);
        try {
            Object systemServiceSafe = getSystemServiceSafe(Strings.getString(30));
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(64), memoryInfo);
            hashMap.put("available", Long.valueOf(memoryInfo.availMem));
            hashMap.put(FileDownloadModel.TOTAL, Long.valueOf(memoryInfo.totalMem));
            hashMap.put("isLow", Long.valueOf(memoryInfo.lowMemory ? 1L : 0L));
            hashMap.put("threshold", Long.valueOf(memoryInfo.threshold));
        } catch (Throwable unused) {
        }
        return hashMap;
    }

    public String getModel() {
        if (!TextUtils.isEmpty(this.bufModel)) {
            return this.bufModel;
        }
        String str = Build.MODEL;
        if (!TextUtils.isEmpty(str)) {
            str = str.trim();
        }
        this.bufModel = str;
        return str;
    }

    public ArrayList<HashMap<String, Object>> getNeighboringCellInfo() {
        Object systemServiceSafe;
        List list;
        try {
            if (!checkPermission("android.permission.ACCESS_COARSE_LOCATION") || isScopedStorage() || (systemServiceSafe = getSystemServiceSafe("phone")) == null || (list = (List) ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(61), new Object[0])) == null || list.size() <= 0) {
                return null;
            }
            ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();
            for (Object obj : list) {
                int intValue = ((Integer) ResHelper.forceCast(ReflectHelper.invokeInstanceMethod(obj, Strings.getString(27), new Object[0]), -1)).intValue();
                int intValue2 = ((Integer) ResHelper.forceCast(ReflectHelper.invokeInstanceMethod(obj, Strings.getString(28), new Object[0]), -1)).intValue();
                int intValue3 = ((Integer) ResHelper.forceCast(ReflectHelper.invokeInstanceMethod(obj, Strings.getString(62), new Object[0]), -1)).intValue();
                int intValue4 = ((Integer) ResHelper.forceCast(ReflectHelper.invokeInstanceMethod(obj, Strings.getString(63), new Object[0]), -1)).intValue();
                int intValue5 = ((Integer) ResHelper.forceCast(ReflectHelper.invokeInstanceMethod(obj, Strings.getString(19), new Object[0]), -1)).intValue();
                if (intValue != -1 && intValue2 != -1) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("cell", Integer.valueOf(intValue));
                    hashMap.put("lac", Integer.valueOf(intValue2));
                    hashMap.put("rssi", Integer.valueOf(intValue3));
                    hashMap.put("psc", Integer.valueOf(intValue4));
                    hashMap.put("networkType", Integer.valueOf(intValue5));
                    arrayList.add(hashMap);
                }
            }
            if (arrayList.size() > 0) {
                return arrayList;
            }
            return null;
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return null;
        }
    }

    public String getNetworkOperator() {
        Object systemServiceSafe = getSystemServiceSafe("phone");
        if (systemServiceSafe == null) {
            return null;
        }
        try {
            return (String) ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(21), new Object[0]);
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return null;
        }
    }

    public String getNetworkType() {
        return NtFetcher.getInstance(this.context).getNtType();
    }

    public String getNetworkTypeForStatic() {
        String lowerCase = getNetworkType().toLowerCase();
        return (TextUtils.isEmpty(lowerCase) || SchedulerSupport.NONE.equals(lowerCase)) ? SchedulerSupport.NONE : (lowerCase.startsWith("5g") || lowerCase.startsWith("4g") || lowerCase.startsWith("3g") || lowerCase.startsWith("2g")) ? "cell" : lowerCase.startsWith("wifi") ? "wifi" : "other";
    }

    public String getOSCountry() {
        if (!TextUtils.isEmpty(this.bvs.oscoun)) {
            return this.bvs.oscoun;
        }
        this.bvs.oscoun = Locale.getDefault().getCountry();
        return this.bvs.oscoun;
    }

    public String getOSLanguage() {
        if (!TextUtils.isEmpty(this.bvs.oslang)) {
            return this.bvs.oslang;
        }
        this.bvs.oslang = Locale.getDefault().getLanguage();
        return this.bvs.oslang;
    }

    public int getOSVersionInt() {
        if (this.bvs.osvi > 0) {
            return this.bvs.osvi;
        }
        this.bvs.osvi = Build.VERSION.SDK_INT;
        return this.bvs.osvi;
    }

    public String getOSVersionName() {
        try {
            if (!TextUtils.isEmpty(this.bvs.osvn)) {
                return this.bvs.osvn;
            }
            this.bvs.osvn = Build.VERSION.RELEASE;
            return this.bvs.osvn;
        } catch (Throwable unused) {
            return null;
        }
    }

    public String getPackageName() {
        if (!TextUtils.isEmpty(this.bvs.pkgnm)) {
            return this.bvs.pkgnm;
        }
        this.bvs.pkgnm = this.context.getPackageName();
        return this.bvs.pkgnm;
    }

    public int getPlatformCode() {
        return 1;
    }

    public String getProcessor() {
        BufferedReader bufferedReader;
        Throwable th;
        String str;
        Throwable th2;
        if (!TextUtils.isEmpty(this.bvs.prc)) {
            return this.bvs.prc;
        }
        BufferedReader bufferedReader2 = null;
        r0 = null;
        String str2 = null;
        bufferedReader2 = null;
        try {
            try {
                bufferedReader = new BufferedReader(new FileReader(Strings.getString(41)));
                try {
                    try {
                        Pattern compile = Pattern.compile("Processor\\s*:\\s*(.*)");
                        while (true) {
                            String readLine = bufferedReader.readLine();
                            if (readLine == null) {
                                try {
                                    bufferedReader.close();
                                    return str2;
                                } catch (IOException e) {
                                    MobLog.getInstance().d(e);
                                    return str2;
                                }
                            }
                            Matcher matcher = compile.matcher(readLine);
                            if (matcher.matches()) {
                                str = matcher.group(1);
                                try {
                                    this.bvs.prc = str;
                                    str2 = str;
                                } catch (Throwable th3) {
                                    th2 = th3;
                                    bufferedReader2 = bufferedReader;
                                    MobLog.getInstance().d(th2);
                                    if (bufferedReader2 != null) {
                                        try {
                                            bufferedReader2.close();
                                        } catch (IOException e2) {
                                            MobLog.getInstance().d(e2);
                                        }
                                    }
                                    return str;
                                }
                            }
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e3) {
                                MobLog.getInstance().d(e3);
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th5) {
                    th2 = th5;
                    str = str2;
                }
            } catch (Throwable th6) {
                str = null;
                th2 = th6;
            }
        } catch (Throwable th7) {
            bufferedReader = bufferedReader2;
            th = th7;
        }
    }

    public int getPsc() {
        Object systemServiceSafe;
        Object invokeInstanceMethod;
        try {
            if (checkPermission("android.permission.ACCESS_COARSE_LOCATION") && (systemServiceSafe = getSystemServiceSafe("phone")) != null && (invokeInstanceMethod = ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(26), new Object[0])) != null && !"CdmaCellLocation".equals(invokeInstanceMethod.getClass().getSimpleName())) {
                return ((Integer) ResHelper.forceCast(ReflectHelper.invokeInstanceMethod(invokeInstanceMethod, Strings.getString(63), new Object[0]), -1)).intValue();
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        return -1;
    }

    public String getQemuKernel() {
        try {
            return (String) ReflectHelper.invokeStaticMethod(ReflectHelper.importClass(Strings.getString(9)), Strings.getString(10), Strings.getString(53), AmapLoc.RESULT_TYPE_GPS);
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return AmapLoc.RESULT_TYPE_GPS;
        }
    }

    public List<String> getResolvePkgs(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Intent intent = new Intent();
        intent.setData(Uri.parse(str));
        List<ResolveInfo> queryIntentActivities = this.context.getPackageManager().queryIntentActivities(intent, 0);
        if (queryIntentActivities == null || queryIntentActivities.isEmpty()) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (ResolveInfo resolveInfo : queryIntentActivities) {
            if (resolveInfo.activityInfo != null && resolveInfo.activityInfo.packageName != null && !arrayList.contains(resolveInfo.activityInfo.packageName)) {
                arrayList.add(resolveInfo.activityInfo.packageName);
            }
        }
        return arrayList;
    }

    public ArrayList<HashMap<String, String>> getSA() {
        return getAL(true, true);
    }

    public String getSSID() {
        try {
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        if (!"-1".equals(this.bvs.ssi)) {
            return this.bvs.ssi;
        }
        if (checkPermission("android.permission.ACCESS_WIFI_STATE")) {
            Object systemServiceSafe = getSystemServiceSafe("wifi");
            if (systemServiceSafe == null) {
                this.bvs.ssi = null;
                return null;
            }
            Object invokeInstanceMethod = ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(2), new Object[0]);
            if (invokeInstanceMethod != null) {
                String str = (String) ReflectHelper.invokeInstanceMethod(invokeInstanceMethod, Strings.getString(3), new Object[0]);
                this.bvs.ssi = str == null ? null : str.replace("\"", "");
                return this.bvs.ssi;
            }
        }
        return null;
    }

    public int getScreenBrightness() {
        try {
            return Settings.System.getInt(this.context.getContentResolver(), "screen_brightness");
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return -1;
        }
    }

    public int getScreenBrightnessMode() {
        try {
            return Settings.System.getInt(this.context.getContentResolver(), "screen_brightness_mode");
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return -1;
        }
    }

    public String getScreenSize() {
        int[] screenSize = ResHelper.getScreenSize(this.context);
        if (this.context.getResources().getConfiguration().orientation == 1) {
            return screenSize[0] + "x" + screenSize[1];
        }
        return screenSize[1] + "x" + screenSize[0];
    }

    public String getSdcardPath() {
        try {
            if (!TextUtils.isEmpty(this.bvs.sdp)) {
                return this.bvs.sdp;
            }
            if (Build.VERSION.SDK_INT < 29 || this.context.getApplicationInfo().targetSdkVersion < 29) {
                this.bvs.sdp = Environment.getExternalStorageDirectory().getAbsolutePath();
            } else {
                this.bvs.sdp = this.context.getExternalFilesDir(null).getAbsolutePath();
            }
            return this.bvs.sdp;
        } catch (Throwable unused) {
            return null;
        }
    }

    public boolean getSdcardState() {
        if (!this.hasSdcardWritePermission) {
            try {
                this.hasSdcardWritePermission = checkPermission("android.permission.WRITE_EXTERNAL_STORAGE") && "mounted".equals(Environment.getExternalStorageState());
            } catch (Throwable th) {
                MobLog.getInstance().w(th);
            }
        }
        return this.hasSdcardWritePermission;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(15:6|(10:10|11|12|(1:40)|16|(3:31|32|(1:36))|20|(1:22)|23|(2:25|(1:27)(1:28))(2:29|30))|44|12|(1:14)|40|16|(1:18)|31|32|(2:34|36)|20|(0)|23|(0)(0)) */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0084, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0085, code lost:
    
        com.mob.tools.MobLog.getInstance().w(r0.getMessage());
        r0 = null;
     */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0099  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00a0  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00ab  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.String getSerialno() {
        /*
            r7 = this;
            java.lang.String r0 = r7.srno
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto Lb
            java.lang.String r0 = r7.srno
            return r0
        Lb:
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 0
            r2 = 9
            r3 = 0
            if (r0 < r2) goto L46
            int r0 = android.os.Build.VERSION.SDK_INT
            r4 = 26
            if (r0 >= r4) goto L46
            java.lang.String r0 = com.mob.tools.utils.Strings.getString(r2)     // Catch: java.lang.Throwable -> L3e
            java.lang.String r0 = com.mob.tools.utils.ReflectHelper.importClass(r0)     // Catch: java.lang.Throwable -> L3e
            r2 = 10
            java.lang.String r2 = com.mob.tools.utils.Strings.getString(r2)     // Catch: java.lang.Throwable -> L3e
            r4 = 2
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch: java.lang.Throwable -> L3e
            r5 = 11
            java.lang.String r5 = com.mob.tools.utils.Strings.getString(r5)     // Catch: java.lang.Throwable -> L3e
            r4[r1] = r5     // Catch: java.lang.Throwable -> L3e
            r5 = 1
            java.lang.String r6 = "unknown"
            r4[r5] = r6     // Catch: java.lang.Throwable -> L3e
            java.lang.Object r0 = com.mob.tools.utils.ReflectHelper.invokeStaticMethod(r0, r2, r4)     // Catch: java.lang.Throwable -> L3e
            java.lang.String r0 = (java.lang.String) r0     // Catch: java.lang.Throwable -> L3e
            goto L47
        L3e:
            r0 = move-exception
            com.mob.tools.log.NLog r2 = com.mob.tools.MobLog.getInstance()
            r2.d(r0)
        L46:
            r0 = r3
        L47:
            boolean r2 = android.text.TextUtils.isEmpty(r0)
            if (r2 != 0) goto L55
            java.lang.String r2 = "unknown"
            boolean r2 = r2.equalsIgnoreCase(r0)
            if (r2 == 0) goto L57
        L55:
            java.lang.String r0 = android.os.Build.SERIAL
        L57:
            boolean r2 = android.text.TextUtils.isEmpty(r0)
            if (r2 != 0) goto L65
            java.lang.String r2 = "unknown"
            boolean r2 = r2.equalsIgnoreCase(r0)
            if (r2 == 0) goto L91
        L65:
            java.lang.String r2 = "android.permission.READ_PHONE_STATE"
            boolean r2 = r7.checkPermission(r2)     // Catch: java.lang.Throwable -> L84
            if (r2 == 0) goto L91
            int r2 = android.os.Build.VERSION.SDK_INT     // Catch: java.lang.Throwable -> L84
            r4 = 29
            if (r2 >= r4) goto L91
            java.lang.String r0 = "android.os.Build"
            java.lang.String r0 = com.mob.tools.utils.ReflectHelper.importClass(r0)     // Catch: java.lang.Throwable -> L84
            java.lang.String r2 = "getSerial"
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch: java.lang.Throwable -> L84
            java.lang.Object r0 = com.mob.tools.utils.ReflectHelper.invokeStaticMethod(r0, r2, r1)     // Catch: java.lang.Throwable -> L84
            java.lang.String r0 = (java.lang.String) r0     // Catch: java.lang.Throwable -> L84
            goto L91
        L84:
            r0 = move-exception
            com.mob.tools.log.NLog r1 = com.mob.tools.MobLog.getInstance()
            java.lang.String r0 = r0.getMessage()
            r1.w(r0)
            r0 = r3
        L91:
            java.lang.String r1 = "unknown"
            boolean r1 = r1.equalsIgnoreCase(r0)
            if (r1 == 0) goto L9a
            r0 = r3
        L9a:
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 == 0) goto Lab
            java.lang.String r1 = r7.getLocalSerial()
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            if (r2 != 0) goto Lb4
            return r1
        Lab:
            r7.srno = r0
            java.lang.String r0 = r0.trim()
            r7.saveLocalSerial(r0)
        Lb4:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.tools.utils.DeviceHelper.getSerialno():java.lang.String");
    }

    public String getSignMD5() {
        if (!TextUtils.isEmpty(this.bvs.sign)) {
            return this.bvs.sign;
        }
        try {
            Signature[] signatureArr = this.context.getPackageManager().getPackageInfo(getPackageName(), 64).signatures;
            this.bvs.sign = Data.MD5(signatureArr[0].toByteArray());
        } catch (Exception e) {
            MobLog.getInstance().w(e);
        }
        return this.bvs.sign;
    }

    public String getSignMD5(String str) {
        try {
            return Data.MD5(this.context.getPackageManager().getPackageInfo(str, 64).signatures[0].toByteArray());
        } catch (Exception e) {
            MobLog.getInstance().w(e);
            return null;
        }
    }

    public String getSimSerialNumber() {
        try {
            if (Build.VERSION.SDK_INT >= 29) {
                return "-1";
            }
            if (!"-2".equals(this.bvs.ssn)) {
                return this.bvs.ssn;
            }
            if (!checkPermission("android.permission.READ_PHONE_STATE")) {
                return "-1";
            }
            Object systemServiceSafe = getSystemServiceSafe("phone");
            if (systemServiceSafe == null) {
                this.bvs.ssn = "-1";
                return this.bvs.ssn;
            }
            this.bvs.ssn = (String) ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(14), new Object[0]);
            return this.bvs.ssn;
        } catch (Throwable th) {
            MobLog.getInstance().w(th.getMessage());
            return "-1";
        }
    }

    public HashMap<String, HashMap<String, Long>> getSizeInfo() {
        long availableBlocksLong;
        long freeBlocksLong;
        long blockCountLong;
        long blockSizeLong;
        HashMap<String, HashMap<String, Long>> hashMap = new HashMap<>();
        for (String str : new String[]{"sdcard", AeUtil.ROOT_DATA_PATH_OLD_NAME}) {
            HashMap<String, Long> hashMap2 = new HashMap<>();
            hashMap2.put("available", -1L);
            hashMap2.put("free", -1L);
            hashMap2.put(FileDownloadModel.TOTAL, -1L);
            hashMap.put(str, hashMap2);
        }
        HashMap hashMap3 = new HashMap();
        try {
            String sdcardPath = getSdcardPath();
            if (sdcardPath != null) {
                hashMap3.put("sdcard", new StatFs(sdcardPath));
            }
        } catch (Throwable unused) {
        }
        try {
            File dataDirectory = Environment.getDataDirectory();
            if (dataDirectory != null) {
                hashMap3.put(AeUtil.ROOT_DATA_PATH_OLD_NAME, new StatFs(dataDirectory.getPath()));
            }
        } catch (Throwable unused2) {
        }
        for (Map.Entry entry : hashMap3.entrySet()) {
            StatFs statFs = (StatFs) entry.getValue();
            if (Build.VERSION.SDK_INT <= 18) {
                availableBlocksLong = statFs.getAvailableBlocks() * statFs.getBlockSize();
                freeBlocksLong = statFs.getFreeBlocks() * statFs.getBlockSize();
                blockCountLong = statFs.getBlockCount();
                blockSizeLong = statFs.getBlockSize();
            } else {
                availableBlocksLong = statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong();
                freeBlocksLong = statFs.getFreeBlocksLong() * statFs.getBlockSizeLong();
                blockCountLong = statFs.getBlockCountLong();
                blockSizeLong = statFs.getBlockSizeLong();
            }
            HashMap<String, Long> hashMap4 = hashMap.get(entry.getKey());
            hashMap4.put("available", Long.valueOf(availableBlocksLong));
            hashMap4.put("free", Long.valueOf(freeBlocksLong));
            hashMap4.put(FileDownloadModel.TOTAL, Long.valueOf(blockCountLong * blockSizeLong));
        }
        return hashMap;
    }

    public int getStatusBarHeight() {
        if (this.bvs.sbh != -2) {
            return this.bvs.sbh;
        }
        if (Build.VERSION.SDK_INT < 28) {
            try {
                int intValue = ((Integer) ReflectHelper.getStaticField(ReflectHelper.importClass("com.android.internal.R$dimen"), "status_bar_height")).intValue();
                this.bvs.sbh = this.context.getResources().getDimensionPixelSize(intValue);
                return this.bvs.sbh;
            } catch (Throwable th) {
                MobLog.getInstance().d(th);
            }
        }
        this.bvs.sbh = -1;
        return this.bvs.sbh;
    }

    public HashMap<String, Object> getSupport() {
        if (this.bvs.feat != null && !this.bvs.feat.isEmpty()) {
            return this.bvs.feat;
        }
        this.bvs.feat = new HashMap<>();
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            PackageManager packageManager = this.context.getPackageManager();
            if (packageManager != null) {
                try {
                    hashMap.put(Dic.MOBILE, Boolean.valueOf(packageManager.hasSystemFeature("android.hardware.telephony")));
                } catch (Throwable unused) {
                }
                try {
                    hashMap.put(Dic.WIFI, Boolean.valueOf(packageManager.hasSystemFeature("android.hardware.wifi")));
                } catch (Throwable unused2) {
                }
                try {
                    hashMap.put(Dic.GPS, Boolean.valueOf(packageManager.hasSystemFeature("android.hardware.location.gps")));
                } catch (Throwable unused3) {
                }
                TelephonyManager telephonyManager = (TelephonyManager) this.context.getSystemService("phone");
                hashMap.put(Dic.TELEPHONE, Boolean.valueOf((telephonyManager == null || telephonyManager.getPhoneType() == 0) ? false : true));
                try {
                    hashMap.put(Dic.NFC, Boolean.valueOf(packageManager.hasSystemFeature("android.hardware.nfc")));
                } catch (Throwable unused4) {
                }
                try {
                    hashMap.put(Dic.BLUETOOTH, Boolean.valueOf(packageManager.hasSystemFeature("android.hardware.bluetooth")));
                } catch (Throwable unused5) {
                }
                hashMap.put("otg", Boolean.valueOf(packageManager.hasSystemFeature("android.hardware.usb.host")));
            }
        } catch (Throwable unused6) {
        }
        this.bvs.feat.putAll(hashMap);
        return hashMap;
    }

    public Object getSystemServiceSafe(String str) {
        try {
            return this.context.getSystemService(str);
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
            return null;
        }
    }

    public ArrayList<ArrayList<String>> getTTYDriversInfo() {
        ArrayList<ArrayList<String>> arrayList = new ArrayList<>();
        if (Build.VERSION.SDK_INT < 28) {
            try {
                FileReader fileReader = new FileReader(Strings.getString(52));
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    if (!TextUtils.isEmpty(readLine)) {
                        String[] split = readLine.trim().split(SQLBuilder.BLANK);
                        if (split.length > 1) {
                            ArrayList<String> arrayList2 = new ArrayList<>();
                            for (String str : split) {
                                if (!TextUtils.isEmpty(str)) {
                                    arrayList2.add(str.trim());
                                }
                            }
                            arrayList.add(arrayList2);
                        }
                    }
                }
                bufferedReader.close();
                fileReader.close();
            } catch (Throwable th) {
                MobLog.getInstance().d(th.getMessage(), new Object[0]);
            }
        }
        return arrayList;
    }

    public String getTimezone() {
        if (!TextUtils.isEmpty(this.bvs.tmzn)) {
            return this.bvs.tmzn;
        }
        try {
            Configuration configuration = new Configuration();
            configuration.setToDefaults();
            Settings.System.getConfiguration(this.context.getContentResolver(), configuration);
            Locale locale = configuration.locale;
            if (locale == null) {
                locale = Locale.getDefault();
            }
            Calendar calendar = Calendar.getInstance(locale);
            if (calendar != null) {
                this.bvs.tmzn = calendar.getTimeZone().getID();
            }
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        return this.bvs.tmzn;
    }

    public Activity getTopActivity() {
        if (Build.VERSION.SDK_INT > 27) {
            return null;
        }
        Map map = (Map) ReflectHelper.getInstanceField(currentActivityThread(), Strings.getString(23));
        for (Object obj : map.values()) {
            if (!((Boolean) ReflectHelper.getInstanceField(obj, Strings.getString(29))).booleanValue()) {
                return (Activity) ReflectHelper.getInstanceField(obj, Strings.getString(30));
            }
        }
        for (Object obj2 : map.values()) {
            if (!((Boolean) ReflectHelper.getInstanceField(obj2, Strings.getString(24))).booleanValue()) {
                return (Activity) ReflectHelper.getInstanceField(obj2, Strings.getString(30));
            }
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00b2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r3v0 */
    /* JADX WARN: Type inference failed for: r3v7, types: [java.io.BufferedReader] */
    /* JADX WARN: Type inference failed for: r3v8 */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:25:0x008e -> B:17:0x00a6). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.util.HashMap<java.lang.String, java.lang.Object> getTraffic() {
        /*
            Method dump skipped, instructions count: 235
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.tools.utils.DeviceHelper.getTraffic():java.util.HashMap");
    }

    public String getWAbcd(int i) {
        String[] split;
        HashMap<String, Object> mapFromOtherPlace;
        try {
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        if (!getSdcardState()) {
            return null;
        }
        String sdcardPath = getSdcardPath();
        if (!TextUtils.isEmpty(sdcardPath) && (split = Strings.getString(75).split(",")) != null && split.length > 0) {
            for (String str : split) {
                if (str != null) {
                    String trim = str.trim();
                    if (TextUtils.isEmpty(trim)) {
                        continue;
                    } else {
                        try {
                            File file = new File(sdcardPath + trim, ".mn_" + Strings.getString(137));
                            if (file.exists() && file.isFile() && (mapFromOtherPlace = getMapFromOtherPlace(file.getPath())) != null) {
                                String str2 = (String) mapFromOtherPlace.get(String.valueOf(i));
                                if (!TextUtils.isEmpty(str2)) {
                                    return str2.trim();
                                }
                                continue;
                            }
                        } catch (Throwable th2) {
                            MobLog.getInstance().d(th2);
                        }
                    }
                }
            }
        }
        return null;
    }

    public Bitmap getWallPaper() {
        int i;
        try {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(this.context);
            Drawable peekDrawable = wallpaperManager.peekDrawable();
            if (peekDrawable == null && (peekDrawable = wallpaperManager.getWallpaperInfo().loadThumbnail(this.context.getPackageManager())) == null) {
                return null;
            }
            if (peekDrawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) peekDrawable).getBitmap();
            }
            int i2 = 1;
            if (peekDrawable.getIntrinsicWidth() <= 0 || peekDrawable.getIntrinsicHeight() <= 0) {
                i = 1;
            } else {
                i2 = peekDrawable.getIntrinsicWidth();
                i = peekDrawable.getIntrinsicHeight();
            }
            Bitmap createBitmap = Bitmap.createBitmap(i2, i, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            peekDrawable.setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
            peekDrawable.draw(canvas);
            return createBitmap;
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return null;
        }
    }

    public void hideSoftInput(View view) {
        Object systemServiceSafe = getSystemServiceSafe("input_method");
        if (systemServiceSafe == null) {
            return;
        }
        ((InputMethodManager) systemServiceSafe).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public int ih(Context context) throws Throwable {
        String topApp = getTopApp(context);
        return (topApp == null || !getLauncherPackageNames(context).contains(topApp)) ? 0 : 1;
    }

    public <T> T invokeInstanceMethod(Object obj, String str, Object... objArr) {
        try {
            return (T) ReflectHelper.invokeInstanceMethod(obj, str, objArr);
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return null;
        }
    }

    public <T> T invokeInstanceMethod(Object obj, String str, Object[] objArr, Class<?>[] clsArr) {
        try {
            return (T) ReflectHelper.invokeInstanceMethod(obj, str, objArr, clsArr);
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0156  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0135 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:81:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x012e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0127 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r0v10 */
    /* JADX WARN: Type inference failed for: r0v11 */
    /* JADX WARN: Type inference failed for: r0v13 */
    /* JADX WARN: Type inference failed for: r0v14 */
    /* JADX WARN: Type inference failed for: r0v15, types: [java.lang.Process] */
    /* JADX WARN: Type inference failed for: r0v16, types: [java.lang.Process] */
    /* JADX WARN: Type inference failed for: r0v21, types: [java.lang.Process, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r0v29 */
    /* JADX WARN: Type inference failed for: r4v19 */
    /* JADX WARN: Type inference failed for: r4v21 */
    /* JADX WARN: Type inference failed for: r4v4, types: [java.io.DataInputStream] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int ir(android.content.Context r10, java.lang.String r11) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 365
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.tools.utils.DeviceHelper.ir(android.content.Context, java.lang.String):int");
    }

    public boolean isBT() {
        try {
            return BHelper.getInstance(this.context).isEnabled();
        } catch (Throwable unused) {
            return false;
        }
    }

    public boolean isFakePass(String str) {
        try {
            String importClass = ReflectHelper.importClass(Strings.getString(132));
            return ((Integer) ReflectHelper.invokeStaticMethod(importClass, Strings.getString(134), this.context, (String) ReflectHelper.invokeStaticMethod(importClass, Strings.getString(133), str), getPackageName())).intValue() == 1;
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return false;
        }
    }

    public boolean isFreeMeOS() {
        if (this.bvs.fmos != null) {
            return this.bvs.fmos.booleanValue();
        }
        try {
            String systemProperties = getSystemProperties("ro.build.freeme.label");
            if (!TextUtils.isEmpty(systemProperties) && systemProperties.equalsIgnoreCase("FREEMEOS")) {
                this.bvs.fmos = true;
                return this.bvs.fmos.booleanValue();
            }
        } catch (Throwable unused) {
        }
        this.bvs.fmos = false;
        return this.bvs.fmos.booleanValue();
    }

    public boolean isPackageInstalled(String str) {
        try {
            return this.context.getPackageManager().getPackageInfo(str, 0) != null;
        } catch (Throwable unused) {
            return false;
        }
    }

    public boolean isRooted() {
        return (Build.TAGS != null && Build.TAGS.contains("test-keys")) || checkRootFile() || checkRootApp() || checkRootSu() || checkRootRw() || checkRootProp();
    }

    public boolean isSSUIOS() {
        if (this.bvs.ssuios != null) {
            return this.bvs.ssuios.booleanValue();
        }
        try {
            String systemProperties = getSystemProperties("ro.ssui.product");
            if (!TextUtils.isEmpty(systemProperties) && !systemProperties.equalsIgnoreCase(EnvironmentCompat.MEDIA_UNKNOWN)) {
                this.bvs.ssuios = true;
                return this.bvs.ssuios.booleanValue();
            }
        } catch (Throwable unused) {
        }
        this.bvs.ssuios = false;
        return this.bvs.ssuios.booleanValue();
    }

    public boolean isScopedStorage() {
        if (this.bvs.scpstr != null) {
            return this.bvs.scpstr.booleanValue();
        }
        boolean z = false;
        boolean z2 = Build.VERSION.SDK_INT >= 29;
        boolean z3 = this.context.getApplicationInfo().targetSdkVersion >= 29;
        BVS bvs = this.bvs;
        if (z2 && z3) {
            z = true;
        }
        bvs.scpstr = Boolean.valueOf(z);
        return this.bvs.scpstr.booleanValue();
    }

    public boolean isSensitiveDevice() {
        String manufacturer;
        String mIUIVersion;
        boolean z;
        if (this.bvs.sendev != null) {
            return this.bvs.sendev.booleanValue();
        }
        boolean z2 = false;
        try {
            manufacturer = getManufacturer();
            mIUIVersion = getMIUIVersion();
        } catch (Throwable th) {
            String message = th.getMessage();
            if (message == null) {
                message = "";
            }
            MobLog.getInstance().d(message, new Object[0]);
        }
        if (!TextUtils.isEmpty(mIUIVersion) && mIUIVersion.length() >= 3) {
            try {
            } catch (Throwable th2) {
                String message2 = th2.getMessage();
                if (message2 == null) {
                    message2 = "";
                }
                MobLog.getInstance().d(message2, new Object[0]);
            }
            if (Integer.parseInt(mIUIVersion.substring(1)) >= 12) {
                z = true;
                if ("xiaomi".equalsIgnoreCase(manufacturer) && z) {
                    z2 = true;
                }
                this.bvs.sendev = Boolean.valueOf(z2);
                return z2;
            }
        }
        z = false;
        if ("xiaomi".equalsIgnoreCase(manufacturer)) {
            z2 = true;
        }
        this.bvs.sendev = Boolean.valueOf(z2);
        return z2;
    }

    public boolean isSmlt() {
        try {
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        if (this.isSmlt != null) {
            return this.isSmlt.booleanValue();
        }
        SmltHelper smltHelper = new SmltHelper();
        int i = smltHelper.checkBaseband(this.context) == 1 ? 1 : 0;
        if (smltHelper.checkBoard(this.context) == 1) {
            i++;
        }
        if (i >= 2) {
            Boolean bool = true;
            this.isSmlt = bool;
            return bool.booleanValue();
        }
        if (smltHelper.checkPlatform(this.context) == 1) {
            i++;
        }
        if (i >= 2) {
            Boolean bool2 = true;
            this.isSmlt = bool2;
            return bool2.booleanValue();
        }
        if (smltHelper.checkFlavor(this.context) == 1) {
            i++;
        }
        if (i >= 2) {
            Boolean bool3 = true;
            this.isSmlt = bool3;
            return bool3.booleanValue();
        }
        if (smltHelper.checkCgroup() == 1) {
            i++;
        }
        if (i >= 2) {
            Boolean bool4 = true;
            this.isSmlt = bool4;
            return bool4.booleanValue();
        }
        if (smltHelper.checkBluetooth(this.context) == 1) {
            i++;
        }
        if (i >= 2) {
            Boolean bool5 = true;
            this.isSmlt = bool5;
            return bool5.booleanValue();
        }
        if (smltHelper.checkImei(this.context) == 1) {
            i++;
        }
        if (i >= 2) {
            Boolean bool6 = true;
            this.isSmlt = bool6;
            return bool6.booleanValue();
        }
        if (smltHelper.checkCommonApp(this.context) == 1) {
            i++;
        }
        if (i >= 2) {
            Boolean bool7 = true;
            this.isSmlt = bool7;
            return bool7.booleanValue();
        }
        if (smltHelper.checkCpuInfo() == 1) {
            i++;
        }
        if (i >= 2) {
            Boolean bool8 = true;
            this.isSmlt = bool8;
            return bool8.booleanValue();
        }
        if (i >= 2) {
            Boolean bool9 = true;
            this.isSmlt = bool9;
            return bool9.booleanValue();
        }
        Boolean bool10 = false;
        this.isSmlt = bool10;
        return bool10.booleanValue();
    }

    public boolean isWifiProxy() {
        String host;
        int port;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                host = System.getProperty("http.proxyHost");
                String property = System.getProperty("http.proxyPort");
                if (property == null) {
                    property = "-1";
                }
                try {
                    port = Integer.parseInt(property);
                } catch (Throwable unused) {
                    port = -1;
                }
            } else {
                host = Proxy.getHost(this.context);
                port = Proxy.getPort(this.context);
            }
            return (TextUtils.isEmpty(host) || port == -1) ? false : true;
        } catch (Throwable unused2) {
            return false;
        }
    }

    public HashMap<String, String> listNetworkHardware() throws Throwable {
        if (this.bufMcs != null && !this.bufMcs.isEmpty()) {
            return this.bufMcs;
        }
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        if (networkInterfaces == null) {
            return null;
        }
        ArrayList<NetworkInterface> list = Collections.list(networkInterfaces);
        HashMap<String, String> hashMap = new HashMap<>();
        for (NetworkInterface networkInterface : list) {
            byte[] hardwareAddress = Build.VERSION.SDK_INT >= 9 ? networkInterface.getHardwareAddress() : null;
            if (hardwareAddress != null) {
                hashMap.put(networkInterface.getName(), byteToHex(hardwareAddress));
            }
        }
        this.bufMcs = hashMap;
        return hashMap;
    }

    public HashMap<String, String> ping(String str, int i, int i2) {
        float f;
        float f2;
        ArrayList arrayList = new ArrayList();
        try {
            int i3 = i2 + 8;
            Process process = (Process) ReflectHelper.invokeInstanceMethod(ReflectHelper.invokeStaticMethod(ReflectHelper.importClass(Strings.getString(42)), Strings.getString(43), new Object[0]), Strings.getString(44), "ping -c " + i + " -s " + i2 + SQLBuilder.BLANK + str);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((InputStream) ReflectHelper.invokeInstanceMethod(process, Strings.getString(45), new Object[0])));
            String readLine = bufferedReader.readLine();
            while (readLine != null) {
                if (readLine.startsWith(i3 + " bytes from")) {
                    if (readLine.endsWith("ms")) {
                        readLine = readLine.substring(0, readLine.length() - 2).trim();
                    } else if (readLine.endsWith("s")) {
                        readLine = readLine.substring(0, readLine.length() - 1).trim() + "000";
                    }
                    int indexOf = readLine.indexOf("time=");
                    if (indexOf > 0) {
                        try {
                            arrayList.add(Float.valueOf(Float.parseFloat(readLine.substring(indexOf + 5).trim())));
                        } catch (Throwable th) {
                            MobLog.getInstance().w(th);
                        }
                    }
                }
                readLine = bufferedReader.readLine();
            }
            process.waitFor();
        } catch (Throwable th2) {
            MobLog.getInstance().d(th2);
        }
        int size = arrayList.size();
        int size2 = i - arrayList.size();
        float f3 = 0.0f;
        if (size > 0) {
            f3 = Float.MAX_VALUE;
            float f4 = 0.0f;
            f2 = 0.0f;
            for (int i4 = 0; i4 < size; i4++) {
                float floatValue = ((Float) arrayList.get(i4)).floatValue();
                if (floatValue < f3) {
                    f3 = floatValue;
                }
                if (floatValue > f2) {
                    f2 = floatValue;
                }
                f4 += floatValue;
            }
            f = f4 / size;
        } else {
            f = 0.0f;
            f2 = 0.0f;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(IMAPStore.ID_ADDRESS, str);
        hashMap.put("transmitted", String.valueOf(i));
        hashMap.put("received", String.valueOf(size));
        hashMap.put("loss", String.valueOf(size2));
        hashMap.put("min", String.valueOf(f3));
        hashMap.put("max", String.valueOf(f2));
        hashMap.put("avg", String.valueOf(f));
        return hashMap;
    }

    public String[] queryIMEI() {
        String str;
        try {
            ArrayList arrayList = new ArrayList();
            try {
                if (checkPermission("android.permission.READ_PHONE_STATE") && Build.VERSION.SDK_INT < 29) {
                    if (this.bufIm == null || this.bufIm.isEmpty()) {
                        Object systemServiceSafe = getSystemServiceSafe("phone");
                        if (systemServiceSafe == null) {
                            return null;
                        }
                        String imei = getIMEI();
                        if (TextUtils.isEmpty(imei)) {
                            imei = "-1";
                        }
                        this.bufIm.add(imei);
                        arrayList.add(imei);
                        for (int i = 0; i <= 5; i++) {
                            try {
                                str = (String) ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(8), Integer.valueOf(i));
                            } catch (Throwable unused) {
                                str = null;
                            }
                            if (TextUtils.isEmpty(str)) {
                                str = "-1";
                            }
                            this.bufIm.add(str);
                            arrayList.add(str);
                        }
                    } else {
                        arrayList.addAll(this.bufIm);
                    }
                }
            } catch (Throwable th) {
                MobLog.getInstance().w(th);
            }
            String[] split = Strings.getString(54).split(";");
            String[][] strArr = new String[split.length];
            for (int i2 = 0; i2 < split.length; i2++) {
                strArr[i2] = split[i2].split(",");
            }
            if (this.bufImp == null || this.bufImp.isEmpty()) {
                for (String[] strArr2 : strArr) {
                    for (String str2 : strArr2) {
                        for (String str3 : getSystemProperties(str2).split(",")) {
                            if (!TextUtils.isEmpty(str3) && !arrayList.contains(str3)) {
                                this.bufImp.add(str3);
                                arrayList.add(str3);
                            }
                        }
                    }
                }
            } else {
                arrayList.addAll(this.bufImp);
            }
            if (arrayList.size() > 0) {
                return (String[]) arrayList.toArray(new String[arrayList.size()]);
            }
        } catch (Throwable th2) {
            MobLog.getInstance().w(th2);
        }
        return null;
    }

    public String[] queryIMSI() {
        if (this.bvs.imsArr != null) {
            if (this.bvs.imsArr.size() > 0) {
                return (String[]) this.bvs.imsArr.toArray(new String[this.bvs.imsArr.size()]);
            }
            return null;
        }
        try {
            String systemProperties = getSystemProperties(Strings.getString(55));
            this.bvs.imsArr = new ArrayList<>();
            ArrayList arrayList = new ArrayList();
            for (String str : systemProperties.split(",")) {
                if (!TextUtils.isEmpty(str) && !arrayList.contains(str)) {
                    this.bvs.imsArr.add(str);
                    arrayList.add(str);
                }
            }
            if (arrayList.size() > 0) {
                return (String[]) arrayList.toArray(new String[arrayList.size()]);
            }
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
        return null;
    }

    public String readFile(String str) {
        StringBuilder sb = new StringBuilder();
        try {
            FileReader fileReader = new FileReader(str);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                String trim = readLine.trim();
                if (sb.length() > 0) {
                    sb.append("\n");
                }
                sb.append(trim);
            }
            bufferedReader.close();
            fileReader.close();
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
        return sb.toString();
    }

    public void registerBtWatcher(String str, final BtWatcher btWatcher) {
        if (btWatcher != null) {
            try {
                BHelper.getInstance(this.context).registerBOperationReceiver(str, new BHelper.BOperationCallback() { // from class: com.mob.tools.utils.DeviceHelper.7
                    @Override // com.mob.tools.utils.BHelper.BOperationCallback
                    protected void onConnectionChanged(boolean z, HashMap<String, Object> hashMap) {
                        btWatcher.onBtConnectionChanged(z, hashMap);
                    }

                    @Override // com.mob.tools.utils.BHelper.BOperationCallback
                    protected void onDeviceConnected(HashMap<String, Object> hashMap) {
                        btWatcher.onDeviceConnected(hashMap);
                    }

                    @Override // com.mob.tools.utils.BHelper.BOperationCallback
                    protected void onDeviceDisconnected(HashMap<String, Object> hashMap) {
                        btWatcher.onDeviceDisconnected(hashMap);
                    }

                    @Override // com.mob.tools.utils.BHelper.BOperationCallback
                    protected void onDisabled() {
                        btWatcher.onBtDisabled();
                    }

                    @Override // com.mob.tools.utils.BHelper.BOperationCallback
                    protected void onEnabled() {
                        btWatcher.onBtEnabled();
                    }
                });
            } catch (Throwable th) {
                MobLog.getInstance().d(th);
            }
        }
    }

    public void removeWABCD() {
        String[] split;
        try {
            if (getSdcardState()) {
                String sdcardPath = getSdcardPath();
                if (TextUtils.isEmpty(sdcardPath) || (split = Strings.getString(75).split(",")) == null || split.length <= 0) {
                    return;
                }
                for (String str : split) {
                    if (str != null) {
                        String trim = str.trim();
                        if (!TextUtils.isEmpty(trim)) {
                            try {
                                new File(sdcardPath + trim, ".mn_" + Strings.getString(137)).delete();
                            } catch (Throwable th) {
                                MobLog.getInstance().d(th);
                            }
                        }
                    }
                }
            }
        } catch (Throwable th2) {
            MobLog.getInstance().d(th2);
        }
    }

    public int sa(Context context, Intent intent) throws Throwable {
        try {
            context.startActivity(intent);
            return 1;
        } catch (ActivityNotFoundException e) {
            MobLog.getInstance().d(e);
            return 0;
        }
    }

    public int saInUI(final Context context, final Intent intent) {
        final int[] iArr = new int[1];
        UIHandler.sendEmptyMessage(0, new Handler.Callback() { // from class: com.mob.tools.utils.DeviceHelper.1
            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message message) {
                int[] iArr2;
                synchronized (iArr) {
                    try {
                        try {
                            iArr[0] = DeviceHelper.this.sa(context, intent);
                            iArr2 = iArr;
                        } catch (Throwable th) {
                            iArr[0] = 2;
                            MobLog.getInstance().d(th);
                            iArr2 = iArr;
                        }
                        iArr2.notifyAll();
                    } catch (Throwable th2) {
                        iArr.notifyAll();
                        throw th2;
                    }
                }
                return false;
            }
        });
        synchronized (iArr) {
            try {
                iArr.wait();
            } catch (Throwable th) {
                MobLog.getInstance().w(th);
            }
        }
        return iArr[0];
    }

    public int sap(Context context, String str) throws Throwable {
        try {
            Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(str);
            if (launchIntentForPackage == null) {
                return 0;
            }
            launchIntentForPackage.addFlags(276824064);
            context.startActivity(launchIntentForPackage);
            return 1;
        } catch (ActivityNotFoundException e) {
            MobLog.getInstance().d(e);
            return 0;
        }
    }

    public void saveWabcd(String str, int i) {
        String[] split;
        try {
            if (getSdcardState()) {
                String sdcardPath = getSdcardPath();
                if (TextUtils.isEmpty(sdcardPath) || (split = Strings.getString(75).split(",")) == null || split.length <= 0) {
                    return;
                }
                for (String str2 : split) {
                    if (str2 != null) {
                        String trim = str2.trim();
                        if (!TextUtils.isEmpty(trim)) {
                            try {
                                File file = new File(sdcardPath + trim, ".mn_" + Strings.getString(137));
                                HashMap<String, Object> hashMap = null;
                                if (file.exists() && file.isFile()) {
                                    hashMap = getMapFromOtherPlace(file.getPath());
                                }
                                if (hashMap == null) {
                                    hashMap = new HashMap<>();
                                }
                                hashMap.put(String.valueOf(i), str);
                                hashMap.put(Strings.getString(78), Data.MD5(getSortWabcd(hashMap) + Strings.getString(77)));
                                ResHelper.saveObjectToFile(file.getPath(), Data.AES128Encode(Strings.getString(76), new Hashon().fromHashMap(hashMap)));
                            } catch (Throwable th) {
                                MobLog.getInstance().d(th);
                            }
                        }
                    }
                }
            }
        } catch (Throwable th2) {
            MobLog.getInstance().d(th2);
        }
    }

    public void scanBtList(int i, final BtScanCallback btScanCallback) {
        try {
            BHelper.getInstance(this.context).findLEAndClassic(i, new BHelper.BScanCallback() { // from class: com.mob.tools.utils.DeviceHelper.6
                @Override // com.mob.tools.utils.BHelper.BScanCallback
                public void onScan(ArrayList<HashMap<String, Object>> arrayList) {
                    if (btScanCallback != null) {
                        btScanCallback.onScan(arrayList);
                    }
                }
            });
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
    }

    public boolean scanWifiList() {
        Object systemServiceSafe;
        try {
            if (!checkPermission("android.permission.CHANGE_WIFI_STATE") || (systemServiceSafe = getSystemServiceSafe("wifi")) == null) {
                return false;
            }
            return ((Boolean) ReflectHelper.invokeInstanceMethod(systemServiceSafe, Strings.getString(40), new Object[0])).booleanValue();
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
        return false;
    }

    public int sh(Context context) throws Throwable {
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setFlags(AMapEngineUtils.MAX_P20_WIDTH);
            intent.addCategory("android.intent.category.HOME");
            intent.addCategory("android.intent.category.DEFAULT");
            context.startActivity(intent);
            return 1;
        } catch (ActivityNotFoundException e) {
            MobLog.getInstance().d(e);
            return 0;
        }
    }

    public void showSoftInput(View view) {
        Object systemServiceSafe = getSystemServiceSafe("input_method");
        if (systemServiceSafe == null) {
            return;
        }
        ((InputMethodManager) systemServiceSafe).toggleSoftInputFromWindow(view.getWindowToken(), 2, 0);
    }

    public int ss(Context context, Intent intent) throws Throwable {
        try {
            return context.startService(intent) == null ? 0 : 1;
        } catch (SecurityException e) {
            MobLog.getInstance().d(e);
            return 2;
        }
    }

    public void unRegisterBtScanReceiver() {
        try {
            BHelper.getInstance(this.context).unRegisterBScanReceiver();
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
    }

    public void unRegisterBtWatcher(String str) {
        try {
            BHelper.getInstance(this.context).unRegisterBOperationReceiver(str);
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
        }
    }

    public boolean usbEnable() {
        try {
            if (Build.VERSION.SDK_INT >= 17) {
                if (Settings.Secure.getInt(this.context.getContentResolver(), "adb_enabled", 0) <= 0) {
                    return false;
                }
            } else if (Settings.Secure.getInt(this.context.getContentResolver(), "adb_enabled", 0) <= 0) {
                return false;
            }
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    public boolean vpn() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            if (networkInterfaces == null) {
                return false;
            }
            Iterator it = Collections.list(networkInterfaces).iterator();
            while (it.hasNext()) {
                NetworkInterface networkInterface = (NetworkInterface) it.next();
                if (networkInterface.isUp() && networkInterface.getInterfaceAddresses().size() != 0 && ("tun0".equals(networkInterface.getName()) || "ppp0".equals(networkInterface.getName()))) {
                    return true;
                }
            }
            return false;
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return false;
        }
    }
}
