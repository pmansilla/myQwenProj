package com.mob.mobapm.core.m;

import com.mob.MobSDK;
import com.mob.mobapm.bean.ExceptionType;
import com.mob.mobapm.core.b;
import com.mob.mobapm.core.d;
import com.mob.mobapm.core.e;
import com.mob.mobapm.core.i;
import com.mob.mobapm.core.j;
import com.mob.mobapm.e.c;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class a implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler a;

    /* renamed from: com.mob.mobapm.core.m.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    class RunnableC0066a implements Runnable {
        final /* synthetic */ Thread a;
        final /* synthetic */ Throwable b;

        RunnableC0066a(Thread thread, Throwable th) {
            this.a = thread;
            this.b = th;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                long currentTimeMillis = System.currentTimeMillis();
                HashMap hashMap = new HashMap();
                HashMap hashMap2 = new HashMap();
                hashMap2.put("type", ExceptionType.CRASH.name);
                hashMap2.put("happenTime", Long.valueOf(currentTimeMillis));
                hashMap2.put("threadName", this.a.getId() + "_" + this.a.getName());
                hashMap2.put("errType", this.b.getClass().getName());
                hashMap2.put("errDesc", this.b.getLocalizedMessage());
                hashMap2.put("stackDetail", c.a(this.b));
                hashMap.put(String.valueOf(currentTimeMillis), hashMap2);
                com.mob.mobapm.b.a.h(hashMap);
                HashMap hashMap3 = new HashMap();
                ArrayList arrayList = new ArrayList();
                arrayList.add(hashMap2);
                hashMap3.put("bundleName", MobSDK.getContext().getPackageName());
                hashMap3.put("uploadTime", Long.valueOf(System.currentTimeMillis()));
                hashMap3.put("errorStack", arrayList);
                com.mob.mobapm.d.a.a().d("APM: upload crash Object: " + hashMap3, new Object[0]);
                Object a = d.a(hashMap3, com.mob.mobapm.core.c.f);
                com.mob.mobapm.d.a.a().d("APM: upload crash result. object:" + a, new Object[0]);
                if ((a instanceof HashMap) && ((Integer) ((HashMap) a).get("code")).intValue() == 200) {
                    com.mob.mobapm.b.a.b((HashMap<String, Object>) hashMap2);
                }
                e.b().a();
                b.e().d();
                com.mob.mobapm.core.a.d().b();
                j.d().b();
                i.d().b();
                if (a.this.a == null) {
                }
            } catch (Throwable th) {
                try {
                    com.mob.mobapm.d.a.a().d("APM: upload crash error:" + th, new Object[0]);
                } finally {
                    e.b().a();
                    b.e().d();
                    com.mob.mobapm.core.a.d().b();
                    j.d().b();
                    i.d().b();
                    if (a.this.a != null) {
                        a.this.a.uncaughtException(this.a, this.b);
                    }
                }
            }
        }
    }

    public void a() {
        this.a = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) {
        com.mob.mobapm.d.a.a().d("APM Thread: " + thread.getName() + ", Throwable: " + th + ", currentThread:" + Thread.currentThread().getName(), new Object[0]);
        if (com.mob.mobapm.core.c.f) {
            e.b().a(new RunnableC0066a(thread, th));
        }
    }
}
