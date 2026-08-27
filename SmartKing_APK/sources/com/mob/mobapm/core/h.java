package com.mob.mobapm.core;

import android.content.ContentValues;
import com.mob.MobSDK;
import com.mob.mobapm.bean.SocketTransaction;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class h extends k {
    private static h c;

    /* loaded from: classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            int intValue;
            synchronized (h.this.b) {
                List<HashMap<String, Object>> a = com.mob.mobapm.b.c.a(MobSDK.getContext()).a(new String[]{"Id", "sockets"}, null, null, null, null, null);
                if (a != null && !a.isEmpty()) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("records", a);
                    try {
                        Object d = d.d(hashMap);
                        com.mob.mobapm.d.a.a().d("APM: upload transaction success. object:" + d, new Object[0]);
                        if ((d instanceof HashMap) && ((intValue = ((Integer) ((HashMap) d).get("code")).intValue()) == 200 || intValue == 4131002)) {
                            com.mob.mobapm.b.c.a(MobSDK.getContext()).a();
                        }
                    } catch (Throwable th) {
                        com.mob.mobapm.d.a.a().i("APM: upload socketTransaction has error:" + th, new Object[0]);
                    }
                    h.this.a.sendEmptyMessageDelayed(0, c.c * 1000);
                    return;
                }
                h.this.a.sendEmptyMessageDelayed(0, c.c * 1000);
            }
        }
    }

    private h() {
    }

    public static synchronized h d() {
        h hVar;
        synchronized (h.class) {
            if (c == null) {
                c = new h();
            }
            hVar = c;
        }
        return hVar;
    }

    public void a(SocketTransaction socketTransaction) {
        synchronized (this.b) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("sockets", com.mob.mobapm.e.h.a().a(socketTransaction));
            com.mob.mobapm.b.c.a(MobSDK.getContext()).a(contentValues);
        }
    }

    @Override // com.mob.mobapm.core.k
    public void c() {
        if (c.h) {
            e.b().a(new a());
        }
    }
}
