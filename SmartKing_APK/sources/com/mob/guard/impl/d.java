package com.mob.guard.impl;

import com.liulishuo.filedownloader.services.FileDownloadBroadcastHandler;
import com.mob.MobCommunicator;
import com.mob.MobSDK;
import com.mob.guard.MobGuard;
import com.mob.tools.utils.DeviceHelper;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class d {
    protected static String a;
    private static MobCommunicator b;

    static {
        try {
            a = MobSDK.checkRequestUrl("http://sdk.guard.mob.com");
        } catch (Throwable th) {
            e.a().e(th);
        }
    }

    public static <T> T a(String str, String str2, String str3) throws Throwable {
        HashMap<String, Object> a2 = a();
        a2.put("workId", str3);
        a2.put("oldGuardId", str);
        a2.put("newGuardId", str2);
        e.a().d("[request][guardId/uploadV5] request: " + a2, new Object[0]);
        return (T) a("/guard/guardId/uploadV5", a2);
    }

    public static <T> T a(String str, String str2, String str3, String str4, String str5, String str6) throws Throwable {
        HashMap<String, Object> a2 = a();
        a2.put("guardId", str5);
        a2.put("workId", str6);
        a2.put("pullDuid", str);
        a2.put("pullAppkey", str2);
        a2.put("pullPkg", str3);
        a2.put("pullGuardId", str4);
        e.a().d("[request][bePulled/uploadV5] request: " + a2, new Object[0]);
        return (T) a("/guard/bePulled/uploadV5", a2);
    }

    private static <T> T a(String str, HashMap<String, Object> hashMap) throws Throwable {
        HashMap<String, String> hashMap2 = new HashMap<>();
        hashMap2.put("versionTime", MobGuard.getVersionTime());
        return (T) b().requestSynchronized(hashMap2, hashMap, a + str, false);
    }

    public static <T> T a(List<HashMap<String, String>> list, String str) throws Throwable {
        HashMap<String, Object> a2 = a();
        a2.put("guardId", str);
        a2.put("targetAppInfoDtoList", list);
        e.a().d("[request][switchV5] request: " + a2, new Object[0]);
        return (T) a("/guard/switchV5", a2);
    }

    public static <T> T a(List<String> list, List<String> list2, List<String> list3, String str, String str2) throws Throwable {
        HashMap<String, Object> a2 = a();
        a2.put("guardId", str);
        a2.put("workId", str2);
        a2.put("successPkgList", list);
        a2.put("failPkgList", list2);
        a2.put("uncertainPkgList", list3);
        e.a().d("[request][pull/uploadV5] request: " + a2, new Object[0]);
        return (T) a("/guard/pull/uploadV5", a2);
    }

    private static HashMap<String, Object> a() {
        DeviceHelper deviceHelper = DeviceHelper.getInstance(MobSDK.getContext());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("versionTime", MobGuard.getVersionTime());
        hashMap.put("appkey", MobSDK.getAppkey());
        hashMap.put("appver", Integer.valueOf(deviceHelper.getAppVersion()));
        hashMap.put("platVersion", deviceHelper.getOSVersionName());
        hashMap.put("apppkg", MobSDK.getContext().getPackageName());
        hashMap.put("sdkver", Integer.valueOf(MobGuard.SDK_VERSION_CODE));
        hashMap.put("duid", c.e().d());
        hashMap.put("product", 1);
        hashMap.put("plat", 1);
        hashMap.put("brand", DeviceHelper.getInstance(MobSDK.getContext()).getManufacturer());
        hashMap.put(FileDownloadBroadcastHandler.KEY_MODEL, DeviceHelper.getInstance(MobSDK.getContext()).getModel());
        hashMap.put("modelVersion", DeviceHelper.getInstance(MobSDK.getContext()).getOSVersionName());
        return hashMap;
    }

    private static synchronized MobCommunicator b() {
        MobCommunicator mobCommunicator;
        synchronized (d.class) {
            if (b == null) {
                b = new MobCommunicator(1024, "009cbd92ccef123be840deec0c6ed0547194c1e471d11b6f375e56038458fb18833e5bab2e1206b261495d7e2d1d9e5aa859e6d4b671a8ca5d78efede48e291a3f", "1dfd1d615cb891ce9a76f42d036af7fce5f8b8efaa11b2f42590ecc4ea4cff28f5f6b0726aeb76254ab5b02a58c1d5b486c39d9da1a58fa6ba2f22196493b3a4cbc283dcf749bf63679ee24d185de70c8dfe05605886c9b53e9f569082eabdf98c4fb0dcf07eb9bb3e647903489ff0b5d933bd004af5be4a1022fdda41f347f1");
            }
            mobCommunicator = b;
        }
        return mobCommunicator;
    }
}
