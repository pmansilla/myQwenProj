package com.mob.guard.impl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.TextUtils;
import com.mob.MobSDK;
import com.mob.apc.APCMessage;
import com.mob.apc.MobAPC;
import com.mob.commons.MOBGUARD;
import com.mob.commons.authorize.DeviceAuthorizer;
import com.mob.elp.MobELP;
import com.mob.guard.MobGuard;
import com.mob.mcl.MCLSDK;
import com.mob.tools.utils.ActivityTracker;
import com.mob.tools.utils.ResHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import no.nordicsemi.android.dfu.internal.scanner.BootloaderScanner;

/* loaded from: classes.dex */
public class c implements MobAPC.MobAPCMessageListener, ActivityTracker.Tracker {
    private static c m = new c();
    private ExecutorService a = Executors.newSingleThreadExecutor();
    private volatile boolean b = false;
    private String c = null;
    private String d = null;
    private boolean e = false;
    private int f = 0;
    private int g = 0;
    private List<HashMap<String, Object>> h = null;
    private boolean i;
    private boolean j;
    private long k;
    private Activity l;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements Runnable {
        final /* synthetic */ String a;
        final /* synthetic */ boolean b;

        a(String str, boolean z) {
            this.a = str;
            this.b = z;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                Thread.sleep(200L);
                c.this.a(false, this.a);
                if (this.b) {
                    Thread.sleep(500L);
                    c.this.f();
                }
            } catch (Throwable unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements ServiceConnection {
        b(c cVar) {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            try {
                MobSDK.getContext().unbindService(this);
            } catch (Throwable th) {
                e.a().e(th);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
        }
    }

    /* renamed from: com.mob.guard.impl.c$c, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    class RunnableC0058c implements Runnable {
        RunnableC0058c() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (c.this.j) {
                c.this.f();
            }
        }
    }

    /* loaded from: classes.dex */
    class d extends g {
        final /* synthetic */ String a;
        final /* synthetic */ long b;
        final /* synthetic */ String c;
        final /* synthetic */ String d;

        d(c cVar, String str, long j, String str2, String str3) {
            this.a = str;
            this.b = j;
            this.c = str2;
            this.d = str3;
        }

        @Override // com.mob.guard.impl.g
        protected void a() throws Throwable {
            MCLSDK.syncSuid(this.a, this.b);
            com.mob.guard.impl.d.a(this.c, this.a, this.d);
        }
    }

    private c() {
    }

    private void a(String str, long j) {
        this.j = false;
        Iterator<HashMap<String, Object>> it = this.h.iterator();
        while (it.hasNext()) {
            String str2 = (String) ResHelper.forceCast(it.next().get("pkg"), null);
            try {
                APCMessage aPCMessage = new APCMessage();
                aPCMessage.what = 1003;
                Bundle bundle = new Bundle();
                bundle.putString("guardId", str);
                bundle.putLong("timestamp", j);
                bundle.putString("workId", this.d);
                aPCMessage.data = bundle;
                APCMessage sendMessage = MobAPC.sendMessage(1, str2, MobGuard.getSdkTag(), aPCMessage, BootloaderScanner.TIMEOUT);
                e.a().d("[Guard] syncId updateClientIDs sendAPCMessage :" + str2 + ", response: " + sendMessage, new Object[0]);
            } catch (Throwable th) {
                e.a().d(th);
                this.j = true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:31:0x011d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void a(boolean r17, java.lang.String r18) {
        /*
            Method dump skipped, instructions count: 773
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.guard.impl.c.a(boolean, java.lang.String):void");
    }

    private boolean a() {
        Bundle bundle;
        Object obj;
        try {
            String packageName = MobSDK.getContext().getPackageName();
            ArrayList arrayList = new ArrayList();
            HashSet hashSet = new HashSet();
            List<ResolveInfo> queryIntentServices = MobSDK.getContext().getPackageManager().queryIntentServices(new Intent("com.mob.intent.MOB_GUARD_SERVICE"), 0);
            if (queryIntentServices != null) {
                for (ResolveInfo resolveInfo : queryIntentServices) {
                    if (resolveInfo.serviceInfo.exported && !packageName.equals(resolveInfo.serviceInfo.packageName) && (bundle = MobSDK.getContext().getPackageManager().getPackageInfo(resolveInfo.serviceInfo.packageName, 128).applicationInfo.metaData) != null && !bundle.isEmpty() && (obj = bundle.get("mob_guard_version")) != null && !hashSet.contains(resolveInfo.serviceInfo.packageName)) {
                        hashSet.add(resolveInfo.serviceInfo.packageName);
                        String valueOf = String.valueOf(obj);
                        HashMap hashMap = new HashMap();
                        hashMap.put("appPackage", resolveInfo.serviceInfo.packageName);
                        hashMap.put("targetVer", valueOf);
                        arrayList.add(hashMap);
                    }
                }
            }
            HashMap hashMap2 = (HashMap) com.mob.guard.impl.d.a(arrayList, MCLSDK.getSuid());
            e.a().d("[Guard] getGuardListV5 response:" + hashMap2, new Object[0]);
            if (hashMap2 != null && !hashMap2.isEmpty()) {
                this.d = (String) ResHelper.forceCast(hashMap2.get("workId"), null);
                this.e = ((Boolean) ResHelper.forceCast(hashMap2.get("syncIdState"), Boolean.FALSE)).booleanValue();
                this.g = ((Integer) ResHelper.forceCast(hashMap2.get("asMaster"), 0)).intValue();
                this.f = ((Integer) ResHelper.forceCast(hashMap2.get("pollTotal"), 0)).intValue();
                this.h = (List) hashMap2.get("pkgList");
                return true;
            }
        } catch (Throwable th) {
            e.a().e(th);
        }
        return false;
    }

    private static boolean a(Context context, String str) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(str, 0);
            return ((packageInfo.applicationInfo.flags & 1) == 0 && (packageInfo.applicationInfo.flags & 128) == 0) && ((packageInfo.applicationInfo.flags & 2097152) == 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.a().e(e);
            return false;
        }
    }

    private void c() {
        try {
            if (this.b) {
                return;
            }
            this.b = true;
            e.a().d("[Guard] init..................", new Object[0]);
            MobSDK.init(MobSDK.getContext());
            MobAPC.init(MobSDK.getContext());
            String authorize = DeviceAuthorizer.authorize(new MOBGUARD());
            this.c = authorize;
            MobELP.init(authorize);
            MobAPC.addAPCMessageListener(MobGuard.getSdkTag(), this);
            MCLSDK.initMCLink(MobSDK.getContext(), MobSDK.getAppkey(), this.c);
            MCLSDK.getSuid();
            e.a().d("[Guard] init guardId:" + MCLSDK.getSuid() + ", time: " + MCLSDK.getCreateSuidTime(), new Object[0]);
            ActivityTracker.getInstance(MobSDK.getContext()).addTracker(this);
        } catch (Throwable th) {
            e.a().e(th);
        }
    }

    public static c e() {
        return m;
    }

    private void h() {
        Bundle bundle;
        e.a().d("[Guard] syncId upPkgList: " + this.h, new Object[0]);
        List<HashMap<String, Object>> list = this.h;
        if (list == null || list.size() == 0) {
            return;
        }
        String suid = MCLSDK.getSuid();
        long createSuidTime = MCLSDK.getCreateSuidTime();
        Iterator<HashMap<String, Object>> it = this.h.iterator();
        long j = createSuidTime;
        String str = suid;
        while (it.hasNext()) {
            APCMessage aPCMessage = null;
            String str2 = (String) ResHelper.forceCast(it.next().get("pkg"), null);
            APCMessage aPCMessage2 = new APCMessage();
            aPCMessage2.what = 1001;
            try {
                aPCMessage = MobAPC.sendMessage(1, str2, MobGuard.getSdkTag(), aPCMessage2, BootloaderScanner.TIMEOUT);
            } catch (Throwable th) {
                e.a().e(th);
            }
            e.a().d("[Guard] syncId getClientIDs sendAPCMessage pkg: " + str2 + ", response:" + aPCMessage, new Object[0]);
            if (aPCMessage != null && (bundle = aPCMessage.data) != null) {
                String string = bundle.getString("guardId");
                long j2 = bundle.getLong("timestamp");
                if (!TextUtils.isEmpty(string) && j2 > 0 && j2 < j) {
                    str = string;
                    j = j2;
                }
            }
        }
        e.a().d("[Guard] syncId update guardId :" + str + ", oldId: " + suid, new Object[0]);
        boolean equals = str.equals(suid) ^ true;
        if (equals) {
            MCLSDK.syncSuid(str, j);
        }
        a(str, j);
        if (equals) {
            try {
                com.mob.guard.impl.d.a(suid, str, this.d);
            } catch (Throwable th2) {
                e.a().d(th2);
            }
        }
    }

    public void a(String str) {
        e.a().d("[Guard] syncId newClientPkg : " + str + " syncIdFailed : " + this.j, new Object[0]);
        if (this.j) {
            this.a.execute(new RunnableC0058c());
        }
    }

    public void a(String str, boolean z) {
        this.a.execute(new a(str, z));
    }

    public boolean b() {
        return this.g == 1;
    }

    public String d() {
        return this.c;
    }

    public void f() {
        if (this.e) {
            h();
        }
    }

    public void g() throws Throwable {
        Object obj;
        c();
        Bundle bundle = MobSDK.getContext().getPackageManager().getPackageInfo(MobSDK.getContext().getPackageName(), 128).applicationInfo.metaData;
        String valueOf = (bundle == null || bundle.isEmpty() || (obj = bundle.get("disable_mob_a_guard")) == null) ? null : String.valueOf(obj);
        e.a().d("[Guard] run disable_mob_a_guard:" + valueOf, new Object[0]);
        if ("true".equals(valueOf)) {
            return;
        }
        boolean a2 = a();
        e.a().d("[Guard] checkAndInitGuardParams:" + a2, new Object[0]);
        if (a2) {
            if (b()) {
                LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
                e.a().d("[Guard] registerServerSocket", new Object[0]);
                com.mob.guard.impl.a.d().a(linkedBlockingQueue);
                boolean booleanValue = ((Boolean) linkedBlockingQueue.take()).booleanValue();
                e.a().d("[Guard] registerServerSocket: " + booleanValue, new Object[0]);
                if (booleanValue) {
                    a(this.e, (String) null);
                    if (this.e) {
                        Thread.sleep(500L);
                        h();
                        return;
                    }
                    return;
                }
            }
            e.a().d("[Guard] registerClientSocket", new Object[0]);
            com.mob.guard.impl.a.d().e();
        }
    }

    @Override // com.mob.tools.utils.ActivityTracker.Tracker
    public void onCreated(Activity activity, Bundle bundle) {
    }

    @Override // com.mob.tools.utils.ActivityTracker.Tracker
    public void onDestroyed(Activity activity) {
    }

    @Override // com.mob.apc.MobAPC.MobAPCMessageListener
    public APCMessage onMessageReceive(String str, APCMessage aPCMessage, long j) {
        Bundle bundle;
        e.a().d("[Guard] onAPCMessageReceive APCMessage:" + aPCMessage + ", pkg:" + str, new Object[0]);
        APCMessage aPCMessage2 = new APCMessage();
        String suid = MCLSDK.getSuid();
        long createSuidTime = MCLSDK.getCreateSuidTime();
        int i = aPCMessage.what;
        if (i == 1001) {
            Bundle bundle2 = new Bundle();
            bundle2.putString("guardId", suid);
            bundle2.putLong("timestamp", createSuidTime);
            bundle2.putString("pkg", MobSDK.getContext().getPackageName());
            aPCMessage2.data = bundle2;
        } else if (i == 1003 && (bundle = aPCMessage.data) != null) {
            String string = bundle.getString("guardId");
            long j2 = bundle.getLong("timestamp");
            String string2 = bundle.getString("workId");
            if (string != null && j2 > 0 && !suid.equals(string) && j2 < createSuidTime) {
                new d(this, string, j2, suid, string2).start();
            }
        }
        return aPCMessage2;
    }

    @Override // com.mob.tools.utils.ActivityTracker.Tracker
    public void onPaused(Activity activity) {
    }

    @Override // com.mob.tools.utils.ActivityTracker.Tracker
    public void onResumed(Activity activity) {
        if (this.k == 0) {
            this.k = SystemClock.elapsedRealtime();
            if (this.i) {
                a((String) null, true);
            }
        }
        this.l = activity;
    }

    @Override // com.mob.tools.utils.ActivityTracker.Tracker
    public void onSaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override // com.mob.tools.utils.ActivityTracker.Tracker
    public void onStarted(Activity activity) {
    }

    @Override // com.mob.tools.utils.ActivityTracker.Tracker
    public void onStopped(Activity activity) {
        Activity activity2 = this.l;
        if (activity2 == null || activity2 == activity) {
            this.k = 0L;
            this.l = null;
        }
    }
}
