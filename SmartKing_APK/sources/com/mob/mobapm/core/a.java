package com.mob.mobapm.core;

import android.os.Message;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class a extends k {
    private static a c;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.mob.mobapm.core.a$a, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public class RunnableC0064a implements Runnable {
        RunnableC0064a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                List<HashMap<String, Object>> f = com.mob.mobapm.b.a.f();
                if (f != null && !f.isEmpty()) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("records", f);
                    Object b = d.b(hashMap);
                    com.mob.mobapm.d.a.a().d("APM: upload app running time success. object:" + b, new Object[0]);
                    Message obtain = Message.obtain();
                    obtain.what = 1;
                    obtain.obj = f;
                    a.this.a.sendMessage(obtain);
                }
            } catch (Throwable th) {
                com.mob.mobapm.d.a.a().i("APM: upload transaction has error:" + th, new Object[0]);
            }
            a.this.a.sendEmptyMessageDelayed(0, c.d * 1000);
        }
    }

    private a() {
        try {
            HashMap<String, Object> g = com.mob.mobapm.b.a.g();
            if (g == null || g.isEmpty()) {
                return;
            }
            com.mob.mobapm.b.a.f(g);
        } catch (Throwable unused) {
        }
    }

    private void a(List<HashMap<String, Object>> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        com.mob.mobapm.b.a.b(list);
    }

    public static synchronized a d() {
        a aVar;
        synchronized (a.class) {
            if (c == null) {
                c = new a();
            }
            aVar = c;
        }
        return aVar;
    }

    @Override // com.mob.mobapm.core.k
    public void c() {
        e.b().a(new RunnableC0064a());
    }

    @Override // com.mob.mobapm.core.k, android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        try {
            int i = message.what;
            if (i != 0) {
                if (i == 1) {
                    a((List) message.obj);
                }
            } else if (c.e) {
                c();
            }
            return false;
        } catch (Throwable unused) {
            return false;
        }
    }
}
