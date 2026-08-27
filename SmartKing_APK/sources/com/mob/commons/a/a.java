package com.mob.commons.a;

import android.os.Handler;
import android.os.Message;
import com.autonavi.amap.mapcore.AeUtil;
import com.mob.MobSDK;
import com.mob.commons.FBListener;
import com.mob.tools.MobHandlerThread;
import com.mob.tools.MobLog;
import com.mob.tools.utils.SharePrefrenceHelper;
import java.util.HashMap;
import java.util.Map;

/* compiled from: ActClt.java */
/* loaded from: classes.dex */
public class a extends d implements Handler.Callback {
    private SharePrefrenceHelper a;
    private FBListener b = null;
    private long c = 0;
    private HashMap<Long, Long> d;
    private boolean e;
    private boolean f;
    private Handler g;

    /* JADX WARN: Type inference failed for: r0v2, types: [com.mob.commons.a.a$1] */
    a() {
        new Thread() { // from class: com.mob.commons.a.a.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                a.this.a_();
            }
        }.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() {
        try {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("type", "PVMT");
            hashMap.put("datetime", Long.valueOf(com.mob.commons.b.a()));
            com.mob.commons.c.a().a(com.mob.commons.b.a(), hashMap);
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
    }

    private synchronized void i() {
        if (this.a == null) {
            this.a = new SharePrefrenceHelper(MobSDK.getContext());
            this.a.open("top_time");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void j() {
        try {
            if (this.d == null) {
                this.d = new HashMap<>();
            }
            long a = com.mob.commons.b.a();
            MobLog.getInstance().d("[cache] foregndAt: " + this.c + ", duration: " + (a - this.c), new Object[0]);
            this.d.put(Long.valueOf(this.c), Long.valueOf(a));
            a(this.d);
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void k() {
        try {
            i();
            HashMap<Long, Long> c = c();
            if (c != null && !c.isEmpty()) {
                for (Map.Entry<Long, Long> entry : c.entrySet()) {
                    long longValue = entry.getKey().longValue();
                    long longValue2 = entry.getValue().longValue();
                    long j = longValue2 - longValue;
                    MobLog.getInstance().d("foregndAt: " + longValue + ", until: " + longValue2 + ", runtimes: " + j, new Object[0]);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("type", "BKIOMT");
                    hashMap.put("datetime", Long.valueOf(com.mob.commons.b.a()));
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put("until", Long.valueOf(longValue2));
                    hashMap2.put("runtimes", Long.valueOf(j));
                    hashMap.put(AeUtil.ROOT_DATA_PATH_OLD_NAME, hashMap2);
                    com.mob.commons.c.a().a(com.mob.commons.b.a(), hashMap);
                }
                if (this.d != null) {
                    this.d.clear();
                }
                a((HashMap<Long, Long>) null);
            }
        } catch (Throwable th) {
            MobLog.getInstance().w(th);
        }
    }

    @Override // com.mob.commons.a.d
    protected void a(Message message) {
        switch (message.what) {
            case 0:
                k();
                this.g.sendEmptyMessage(1);
                return;
            case 1:
                j();
                this.g.sendEmptyMessageDelayed(1, com.mob.commons.b.F() * 1000);
                return;
            default:
                return;
        }
    }

    public void a(HashMap<Long, Long> hashMap) {
        i();
        if (hashMap == null || hashMap.isEmpty()) {
            this.a.remove("key_active_log");
        } else {
            this.a.put("key_active_log", hashMap);
        }
    }

    protected void a_() {
        this.e = com.mob.commons.b.E();
        this.f = com.mob.commons.b.z();
        if (this.e || this.f) {
            this.b = new FBListener() { // from class: com.mob.commons.a.a.2
                @Override // com.mob.commons.FBListener
                public void onFBChanged(boolean z, boolean z2, long j) {
                    if (z2) {
                        a.this.c = com.mob.commons.b.a();
                        if (a.this.e) {
                            a.this.g = MobHandlerThread.newHandler(a.this);
                            a.this.g.sendEmptyMessage(0);
                        }
                    }
                    if (!z) {
                        if (!a.this.e || j <= 0) {
                            return;
                        }
                        a.this.j();
                        a.this.k();
                        a.this.g.removeMessages(1);
                        return;
                    }
                    if (!z2) {
                        a.this.c = com.mob.commons.b.a();
                        a.this.g.sendEmptyMessage(1);
                    }
                    if (a.this.f) {
                        a.this.h();
                    }
                }
            };
            m.a().a(this.b);
        }
    }

    @Override // com.mob.commons.a.d
    protected void b() {
        if (this.b != null) {
            m.a().b(this.b);
        }
    }

    public HashMap<Long, Long> c() {
        HashMap<Long, Long> hashMap;
        try {
            i();
            hashMap = (HashMap) this.a.get("key_active_log");
        } catch (Throwable th) {
            MobLog.getInstance().d(th);
            hashMap = null;
        }
        return hashMap == null ? new HashMap<>() : hashMap;
    }
}
