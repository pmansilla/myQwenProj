package com.mob.mobapm.core.l;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.mob.MobSDK;
import com.mob.mobapm.bean.ExceptionType;
import com.mob.mobapm.core.c;
import com.mob.mobapm.core.d;
import com.mob.mobapm.core.e;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import no.nordicsemi.android.dfu.internal.scanner.BootloaderScanner;

/* loaded from: classes.dex */
public class a extends Thread {
    private static int c;
    private boolean b = true;
    private Handler a = new HandlerC0065a(Looper.getMainLooper());

    /* renamed from: com.mob.mobapm.core.l.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    class HandlerC0065a extends Handler {
        HandlerC0065a(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            int unused = a.c = 0;
            a.this.b = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                long currentTimeMillis = System.currentTimeMillis();
                HashMap hashMap = new HashMap();
                HashMap hashMap2 = new HashMap();
                Map c = a.this.c();
                hashMap2.put("type", ExceptionType.STUCK.name);
                hashMap2.put("happenTime", Long.valueOf(currentTimeMillis));
                hashMap2.put("threadName", Looper.getMainLooper().getThread().getId() + "_" + Looper.getMainLooper().getThread().getName());
                hashMap2.put("errType", "STUCK_EXCEPTION");
                Object obj = null;
                hashMap2.put("errDesc", (c == null || !c.containsKey("desc")) ? null : c.get("desc"));
                if (c != null && c.containsKey("stack")) {
                    obj = c.get("stack");
                }
                hashMap2.put("stackDetail", obj);
                hashMap.put(String.valueOf(currentTimeMillis), hashMap2);
                com.mob.mobapm.b.a.d(hashMap);
                HashMap hashMap3 = new HashMap();
                ArrayList arrayList = new ArrayList();
                arrayList.add(hashMap2);
                hashMap3.put("bundleName", MobSDK.getContext().getPackageName());
                hashMap3.put("uploadTime", Long.valueOf(System.currentTimeMillis()));
                hashMap3.put("errorStack", arrayList);
                com.mob.mobapm.d.a.a().d("APM: upload anr Object: " + hashMap3, new Object[0]);
                Object a = d.a(hashMap3, c.g);
                com.mob.mobapm.d.a.a().d("APM: upload anr result. object:" + a, new Object[0]);
                if ((a instanceof HashMap) && ((Integer) ((HashMap) a).get("code")).intValue() == 200) {
                    com.mob.mobapm.b.a.a((HashMap<String, Object>) hashMap2);
                }
            } catch (Throwable th) {
                com.mob.mobapm.d.a.a().d("APM: upload anr error:" + th, new Object[0]);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Map<String, Object> c() {
        Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
        if (allStackTraces != null && !allStackTraces.isEmpty()) {
            Thread thread = Looper.getMainLooper().getThread();
            if (allStackTraces.containsKey(thread)) {
                return com.mob.mobapm.e.c.a(allStackTraces.get(thread));
            }
        }
        return null;
    }

    private void d() {
        e.b().a(new b());
    }

    public void a() {
        b();
    }

    public void b() {
        while (!isInterrupted()) {
            c++;
            this.a.sendEmptyMessage(0);
            try {
                Thread.sleep(BootloaderScanner.TIMEOUT);
                if (c != 0 && this.b) {
                    this.b = false;
                    d();
                }
            } catch (InterruptedException e) {
                com.mob.mobapm.d.a.a().d("APM: processs stack error: " + e, new Object[0]);
            }
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        super.run();
        a();
    }
}
