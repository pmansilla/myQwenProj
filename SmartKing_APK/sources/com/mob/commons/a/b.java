package com.mob.commons.a;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Message;
import com.mob.MobSDK;
import com.mob.tools.MobLog;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.ReflectHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* compiled from: ArtClt.java */
/* loaded from: classes.dex */
public class b extends d {
    private boolean a(long j) {
        Object obj;
        Object obj2;
        Object invokeInstanceMethod;
        int i = 0;
        try {
            Object systemServiceSafe = DeviceHelper.getInstance(MobSDK.getContext()).getSystemServiceSafe("usagestats");
            if (systemServiceSafe == null) {
                return false;
            }
            ReflectHelper.importClass("android.app.usage.UsageStatsManager");
            int i2 = 21;
            List list = Build.VERSION.SDK_INT >= 21 ? (List) ReflectHelper.invokeInstanceMethod(systemServiceSafe, "queryUsageStats", new Object[]{0, 0, Long.valueOf(System.currentTimeMillis())}, new Class[]{Integer.TYPE, Long.TYPE, Long.TYPE}) : null;
            int i3 = 28;
            if (Build.VERSION.SDK_INT < 28 && ((list == null || list.isEmpty()) && (invokeInstanceMethod = ReflectHelper.invokeInstanceMethod(ReflectHelper.getInstanceField(systemServiceSafe, "mService"), "queryUsageStats", new Object[]{0, 0, Long.valueOf(System.currentTimeMillis()), "com.android.settings"}, new Class[]{Integer.TYPE, Long.TYPE, Long.TYPE, String.class})) != null)) {
                list = (List) ReflectHelper.invokeInstanceMethod(invokeInstanceMethod, "getList", new Object[0]);
            }
            if (list != null && !list.isEmpty()) {
                long e = com.mob.commons.i.e();
                long a = com.mob.commons.b.a();
                long j2 = 0;
                if (e > 0 && a < e) {
                    return true;
                }
                int size = list.size();
                ReflectHelper.importClass("android.app.usage.UsageStats");
                PackageManager packageManager = MobSDK.getContext().getPackageManager();
                int i4 = size - 1;
                HashMap hashMap = null;
                HashMap hashMap2 = null;
                while (i4 >= 0) {
                    Object obj3 = list.get(i4);
                    if (Build.VERSION.SDK_INT >= i2) {
                        long longValue = ((Long) ReflectHelper.invokeInstanceMethod(obj3, "getLastTimeUsed", new Object[i])).longValue();
                        if (longValue > j2) {
                            String str = (String) ReflectHelper.invokeInstanceMethod(obj3, "getPackageName", new Object[i]);
                            try {
                                if (!a(packageManager, str)) {
                                    if (hashMap2 == null) {
                                        hashMap2 = new HashMap();
                                    }
                                    Long l = (Long) hashMap2.get(str);
                                    if (l == null || l.longValue() <= longValue) {
                                        hashMap2.put(str, Long.valueOf(longValue));
                                        Object invokeInstanceMethod2 = ReflectHelper.invokeInstanceMethod(obj3, "getFirstTimeStamp", new Object[i]);
                                        Object invokeInstanceMethod3 = ReflectHelper.invokeInstanceMethod(obj3, "getLastTimeStamp", new Object[i]);
                                        Object invokeInstanceMethod4 = ReflectHelper.invokeInstanceMethod(obj3, "getTotalTimeInForeground", new Object[i]);
                                        if (Build.VERSION.SDK_INT < i3) {
                                            obj2 = ReflectHelper.getInstanceField(obj3, "mLaunchCount");
                                            obj = ReflectHelper.getInstanceField(obj3, "mLastEvent");
                                        } else {
                                            obj = null;
                                            obj2 = null;
                                        }
                                        HashMap hashMap3 = new HashMap();
                                        hashMap3.put("packageName", str);
                                        hashMap3.put("firstTimeStamp", invokeInstanceMethod2);
                                        hashMap3.put("lastTimeStamp", invokeInstanceMethod3);
                                        hashMap3.put("lastTimeUsed", Long.valueOf(longValue));
                                        hashMap3.put("totalTimeInForeground", invokeInstanceMethod4);
                                        if (obj2 != null) {
                                            hashMap3.put("launchCount", obj2);
                                        }
                                        if (obj != null) {
                                            hashMap3.put("lastEvent", obj);
                                        }
                                        if (hashMap == null) {
                                            hashMap = new HashMap();
                                        }
                                        hashMap.put(str, hashMap3);
                                    }
                                }
                                i4--;
                                i = 0;
                                i2 = 21;
                                i3 = 28;
                                j2 = 0;
                            } catch (Throwable th) {
                                th = th;
                                MobLog.getInstance().d(th);
                                return false;
                            }
                        }
                    }
                    i4--;
                    i = 0;
                    i2 = 21;
                    i3 = 28;
                    j2 = 0;
                }
                if (hashMap != null && !hashMap.isEmpty()) {
                    ArrayList arrayList = new ArrayList();
                    Iterator it = hashMap.entrySet().iterator();
                    while (it.hasNext()) {
                        arrayList.add(((Map.Entry) it.next()).getValue());
                    }
                    if (arrayList.size() <= 0) {
                        return false;
                    }
                    HashMap<String, Object> hashMap4 = new HashMap<>();
                    hashMap4.put("type", "XM_ARTSMT");
                    hashMap4.put("list", arrayList);
                    hashMap4.put("datetime", Long.valueOf(com.mob.commons.b.a()));
                    com.mob.commons.c.a().a(com.mob.commons.b.a(), hashMap4);
                    com.mob.commons.i.c(com.mob.commons.b.a() + j);
                    return true;
                }
                return false;
            }
            return false;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private boolean a(PackageManager packageManager, String str) {
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(str, 0);
            return ((packageInfo.applicationInfo.flags & 1) == 1) || ((packageInfo.applicationInfo.flags & 128) == 1);
        } catch (Throwable unused) {
            return true;
        }
    }

    @Override // com.mob.commons.a.d
    protected File a() {
        return com.mob.commons.e.a("comm/locks/.artc_lock");
    }

    @Override // com.mob.commons.a.d
    protected void a(Message message) {
        if (message.what != 1) {
            return;
        }
        long D = com.mob.commons.b.D();
        if (D <= 0 || !a(D)) {
            e();
        } else {
            a(1, D);
        }
    }

    @Override // com.mob.commons.a.d
    protected boolean b_() {
        return com.mob.commons.b.D() > 0;
    }

    @Override // com.mob.commons.a.d
    protected void d() {
        b(1);
    }
}
