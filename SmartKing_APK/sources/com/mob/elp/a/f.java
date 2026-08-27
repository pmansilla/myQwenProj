package com.mob.elp.a;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import com.amap.location.common.model.AmapLoc;
import com.autonavi.amap.mapcore.AMapEngineUtils;
import com.liulishuo.filedownloader.services.FileDownloadBroadcastHandler;
import com.mob.MobCommunicator;
import com.mob.MobSDK;
import com.mob.elp.PushMessage;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.Hashon;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/* compiled from: EventHandler.java */
/* loaded from: classes.dex */
public class f {
    private static volatile f d;
    private MobCommunicator a = new MobCommunicator(1024, "009cbd92ccef123be840deec0c6ed0547194c1e471d11b6f375e56038458fb18833e5bab2e1206b261495d7e2d1d9e5aa859e6d4b671a8ca5d78efede48e291a3f", "1dfd1d615cb891ce9a76f42d036af7fce5f8b8efaa11b2f42590ecc4ea4cff28f5f6b0726aeb76254ab5b02a58c1d5b486c39d9da1a58fa6ba2f22196493b3a4cbc283dcf749bf63679ee24d185de70c8dfe05605886c9b53e9f569082eabdf98c4fb0dcf07eb9bb3e647903489ff0b5d933bd004af5be4a1022fdda41f347f1");
    private HashMap<String, Object> b;
    private int c;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: EventHandler.java */
    /* loaded from: classes.dex */
    public class a implements Runnable {
        final /* synthetic */ Context a;
        final /* synthetic */ String b;
        final /* synthetic */ String c;

        a(Context context, String str, String str2) {
            this.a = context;
            this.b = str;
            this.c = str2;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("Content-Type", "application/json");
                HashMap<String, Object> hashMap2 = new HashMap<>();
                HashMap a = f.a(f.this, this.a);
                a.put("clientTime", Long.valueOf(System.currentTimeMillis()));
                a.put("eventType", this.b);
                a.put("workId", this.c);
                ArrayList arrayList = new ArrayList();
                arrayList.add(a);
                hashMap2.put("events", arrayList);
                f.this.a.requestSynchronized(hashMap, hashMap2, MobSDK.checkRequestUrl("http://c.mpl.dutils.com/monitor/event"), false);
            } catch (Throwable th) {
                com.mob.elp.d.d.a().a(th);
            }
        }
    }

    private f() {
    }

    public static f a() {
        if (d == null) {
            synchronized (f.class) {
                if (d == null) {
                    d = new f();
                }
            }
        }
        return d;
    }

    static HashMap a(f fVar, Context context) {
        if (fVar.b == null) {
            DeviceHelper deviceHelper = DeviceHelper.getInstance(context);
            HashMap<String, Object> hashMap = new HashMap<>();
            fVar.b = hashMap;
            hashMap.put("appKey", MobSDK.getAppkey());
            fVar.b.put("appPkg", deviceHelper.getPackageName());
            fVar.b.put("appVersion", deviceHelper.getAppVersion() + "");
            fVar.b.put("plat", AmapLoc.RESULT_TYPE_WIFI_ONLY);
            fVar.b.put("factory", deviceHelper.getManufacturer());
            fVar.b.put(FileDownloadBroadcastHandler.KEY_MODEL, deviceHelper.getModel());
            fVar.b.put("systemVersion", deviceHelper.getOSVersionName());
            fVar.b.put("systemVersionInt", Integer.valueOf(deviceHelper.getOSVersionInt()));
            fVar.b.put("language", deviceHelper.getOSLanguage());
            fVar.b.put("duid", com.mob.elp.a.a.b().a);
            fVar.b.put("deviceId", deviceHelper.getDeviceKey());
        }
        return fVar.b;
    }

    private Intent c() {
        Boolean bool;
        Context context = MobSDK.getContext();
        String packageName = context.getPackageName();
        Intent intent = new Intent(packageName + ".default.MAIN");
        try {
            List<ResolveInfo> queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 0);
            if (queryIntentActivities != null && queryIntentActivities.size() > 0) {
                int size = queryIntentActivities.size();
                for (int i = 0; i < size; i++) {
                    if (queryIntentActivities.get(i).activityInfo != null && packageName.equals(queryIntentActivities.get(i).activityInfo.applicationInfo.packageName)) {
                        bool = Boolean.TRUE;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            com.mob.elp.d.d.a().a("MobPush findActivityByIntent " + e.toString());
        }
        bool = Boolean.FALSE;
        if (!bool.booleanValue()) {
            intent = null;
        }
        if (intent == null) {
            intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        }
        if (intent != null) {
            intent.addFlags(67108864);
        }
        return intent;
    }

    public void a(Context context, PushMessage pushMessage) {
        if (pushMessage != null) {
            Intent intent = null;
            try {
                if (pushMessage.clickAction.action == 0) {
                    intent = c();
                } else if (pushMessage.clickAction.action == 1) {
                    intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setData(Uri.parse(pushMessage.clickAction.url));
                } else if (pushMessage.clickAction.action == 2) {
                    intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setData(Uri.parse(pushMessage.clickAction.scheme));
                    intent.addFlags(AMapEngineUtils.MAX_P20_WIDTH);
                }
                if (intent != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("extras", new Hashon().fromHashMap(pushMessage.extras));
                    intent.putExtras(bundle);
                    com.mob.elp.d.a.a(intent, b());
                }
            } catch (Throwable th) {
                com.mob.elp.d.d.a().a(th);
            }
        }
        a(context, "click", pushMessage.workId);
    }

    public void a(Context context, String str, String str2) {
        com.mob.elp.a.a.h.execute(new a(context, str, str2));
    }

    public int b() {
        int nextInt = new Random().nextInt(100) + 1;
        Calendar calendar = Calendar.getInstance();
        int intValue = Integer.valueOf(calendar.get(12)).intValue();
        int intValue2 = nextInt + intValue + Integer.valueOf(calendar.get(13)).intValue() + Integer.valueOf(calendar.get(14)).intValue();
        if (this.c == intValue2) {
            return b();
        }
        this.c = intValue2;
        return intValue2;
    }
}
