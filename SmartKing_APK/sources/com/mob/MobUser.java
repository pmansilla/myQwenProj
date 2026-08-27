package com.mob;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.mob.commons.MobProduct;
import com.mob.commons.MobProductCollector;
import com.mob.commons.authorize.DeviceAuthorizer;
import com.mob.commons.b;
import com.mob.commons.j;
import com.mob.tools.MobHandlerThread;
import com.mob.tools.MobLog;
import com.mob.tools.proguard.PublicMemberKeeper;
import com.mob.tools.utils.ResHelper;
import com.mob.tools.utils.UIHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/* loaded from: classes.dex */
public final class MobUser implements PublicMemberKeeper {
    private static String d;
    private boolean f;
    private String g;
    private String h;
    private String i;
    private HashMap<String, Object> j;
    private String k;
    private String l;
    private long m;
    private HashSet<UserWatcher> n = new HashSet<>();
    private static final String a = j.c("http://api.u.mob.com");
    private static MobCommunicator b = new MobCommunicator(1024, "009cbd92ccef123be840deec0c6ed0547194c1e471d11b6f375e56038458fb18833e5bab2e1206b261495d7e2d1d9e5aa859e6d4b671a8ca5d78efede48e291a3f", "1dfd1d615cb891ce9a76f42d036af7fce5f8b8efaa11b2f42590ecc4ea4cff28f5f6b0726aeb76254ab5b02a58c1d5b486c39d9da1a58fa6ba2f22196493b3a4cbc283dcf749bf63679ee24d185de70c8dfe05605886c9b53e9f569082eabdf98c4fb0dcf07eb9bb3e647903489ff0b5d933bd004af5be4a1022fdda41f347f1");
    private static Handler c = MobHandlerThread.newHandler("m", new Handler.Callback() { // from class: com.mob.MobUser.1
        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code restructure failed: missing block: B:6:0x0032, code lost:
        
            return false;
         */
        @Override // android.os.Handler.Callback
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public boolean handleMessage(android.os.Message r7) {
            /*
                r6 = this;
                int r0 = r7.what
                r1 = 0
                switch(r0) {
                    case 1: goto L13;
                    case 2: goto Lb;
                    case 3: goto L7;
                    default: goto L6;
                }
            L6:
                goto L32
            L7:
                com.mob.MobUser.b()
                goto L32
            Lb:
                java.lang.Object r7 = r7.obj
                com.mob.MobUser$OnUserGotListener r7 = (com.mob.MobUser.OnUserGotListener) r7
                com.mob.MobUser.b(r7)
                goto L32
            L13:
                java.lang.Object r7 = r7.obj
                java.lang.Object[] r7 = (java.lang.Object[]) r7
                r0 = r7[r1]
                java.lang.String r0 = (java.lang.String) r0
                r2 = 1
                r2 = r7[r2]
                java.lang.String r2 = (java.lang.String) r2
                r3 = 2
                r3 = r7[r3]
                java.lang.String r3 = (java.lang.String) r3
                r4 = 3
                r4 = r7[r4]
                java.util.HashMap r4 = (java.util.HashMap) r4
                r5 = 4
                r7 = r7[r5]
                java.lang.String r7 = (java.lang.String) r7
                com.mob.MobUser.b(r0, r2, r3, r4, r7)
            L32:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.mob.MobUser.AnonymousClass1.handleMessage(android.os.Message):boolean");
        }
    });
    private static MobUser e = new MobUser();

    /* loaded from: classes.dex */
    public interface OnUserGotListener {
        void onUserGot(MobUser mobUser);
    }

    /* loaded from: classes.dex */
    public interface UserWatcher {
        void onUserStateChange(MobUser mobUser);
    }

    private MobUser() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HashMap<String, String> a(String[] strArr) {
        try {
            if (b.ac()) {
                return null;
            }
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("appUserIds", strArr);
            hashMap.put("appkey", MobSDK.getAppkey());
            return (HashMap) b.requestSynchronized(hashMap, a + "/exchange", false);
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a() {
        c.sendEmptyMessage(3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(OnUserGotListener onUserGotListener) {
        Message message = new Message();
        message.what = 2;
        message.obj = onUserGotListener;
        c.sendMessage(message);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(UserWatcher userWatcher) {
        synchronized (e.n) {
            e.n.add(userWatcher);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(String str, String str2, String str3, HashMap<String, Object> hashMap, String str4) {
        Message message = new Message();
        message.what = 1;
        message.obj = new Object[]{str, str2, str3, hashMap, str4};
        c.sendMessage(message);
    }

    private static void a(String str, String str2, HashMap<String, Object> hashMap) {
        if (e.l == null) {
            e.h = str;
            e.i = str2;
            e.j = hashMap;
            return;
        }
        if (!e.f()) {
            d(e.g, str, str2, hashMap, e.k);
            return;
        }
        HashMap<String, Object> hashMap2 = new HashMap<>();
        hashMap2.put("mobUserId", e.l);
        hashMap2.put("nickname", TextUtils.isEmpty(str) ? "" : str);
        hashMap2.put("avatar", TextUtils.isEmpty(str2) ? "" : str2);
        hashMap2.put("appUserMap", hashMap != null ? new HashMap<>() : hashMap);
        try {
            if (!b.ac()) {
                b.requestSynchronized(hashMap2, a + "/modify", false);
                e.h = str;
                e.i = str2;
                e.j = hashMap;
            }
            Iterator<UserWatcher> it = e.n.iterator();
            while (it.hasNext()) {
                it.next().onUserStateChange(e);
            }
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b(UserWatcher userWatcher) {
        synchronized (e.n) {
            e.n.remove(userWatcher);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void c(final OnUserGotListener onUserGotListener) {
        if (e.l == null || !e.f()) {
            d(e.g, e.h, e.i, e.j, e.k);
        }
        if (onUserGotListener != null) {
            UIHandler.sendEmptyMessage(0, new Handler.Callback() { // from class: com.mob.MobUser.2
                @Override // android.os.Handler.Callback
                public boolean handleMessage(Message message) {
                    OnUserGotListener.this.onUserGot(MobUser.e);
                    return false;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void c(String str, String str2, String str3, HashMap<String, Object> hashMap, String str4) {
        if (e.l != null && e.f() && ResHelper.isEqual(str, e.g)) {
            a(str2, str3, hashMap);
        } else {
            d(str, str2, str3, hashMap, str4);
        }
    }

    private static String d() {
        if (d == null) {
            ArrayList<MobProduct> products = MobProductCollector.getProducts();
            d = DeviceAuthorizer.authorize(products.isEmpty() ? null : products.get(0));
        }
        return d;
    }

    private static void d(String str, String str2, String str3, HashMap<String, Object> hashMap, String str4) {
        if (e.l != null) {
            e();
        }
        HashMap<String, Object> hashMap2 = new HashMap<>();
        ArrayList<MobProduct> products = MobProductCollector.getProducts();
        ArrayList arrayList = new ArrayList();
        Iterator<MobProduct> it = products.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().getProductTag());
        }
        hashMap2.put("sdks", arrayList);
        if (!TextUtils.isEmpty(str)) {
            hashMap2.put("appUserId", str);
        }
        hashMap2.put("appkey", MobSDK.getAppkey());
        hashMap2.put("nickname", TextUtils.isEmpty(str2) ? "" : str2);
        hashMap2.put("avatar", TextUtils.isEmpty(str3) ? "" : str3);
        hashMap2.put("appUserMap", hashMap == null ? new HashMap<>() : hashMap);
        hashMap2.put("duid", d());
        if (!TextUtils.isEmpty(str4)) {
            hashMap2.put("sign", str4);
        }
        try {
            if (!b.ac()) {
                HashMap hashMap3 = (HashMap) b.requestSynchronized(hashMap2, a + "/login", false);
                String str5 = (String) hashMap3.get("mobUserId");
                long parseLong = Long.parseLong(String.valueOf(hashMap3.get("loginExpireAt")));
                b.a();
                e.g = str;
                e.f = TextUtils.isEmpty(str);
                e.h = str2;
                e.i = str3;
                e.j = hashMap;
                e.k = str4;
                e.l = str5;
                e.m = parseLong;
            }
            Iterator<UserWatcher> it2 = e.n.iterator();
            while (it2.hasNext()) {
                it2.next().onUserStateChange(e);
            }
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void e() {
        if (e.l != null && !b.ac()) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("mobUserId", e.l);
            try {
                b.requestSynchronized(hashMap, a + "/logout", false);
            } catch (Throwable th) {
                MobLog.getInstance().w(th);
            }
        }
        boolean z = (e.l == null && e.g == null && e.h == null && e.i == null && e.j == null && e.k == null && e.f && e.m == 0) ? false : true;
        e.l = null;
        e.g = null;
        e.h = null;
        e.i = null;
        e.j = null;
        e.k = null;
        e.f = true;
        e.m = 0L;
        if (z) {
            Iterator<UserWatcher> it = e.n.iterator();
            while (it.hasNext()) {
                it.next().onUserStateChange(e);
            }
        }
    }

    private boolean f() {
        return b.a() < this.m;
    }

    public String getAvatar() {
        return this.i;
    }

    public HashMap<String, Object> getExtraInfo() {
        return this.j;
    }

    public String getId() {
        return this.g;
    }

    public String getMobUserId() {
        return this.l;
    }

    public String getNickName() {
        return this.h;
    }

    public boolean isAnonymous() {
        return this.f;
    }
}
