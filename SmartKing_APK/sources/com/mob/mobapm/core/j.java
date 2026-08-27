package com.mob.mobapm.core;

import android.content.ContentValues;
import com.mob.MobSDK;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class j extends k {
    private static j c;

    /* loaded from: classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            int intValue;
            synchronized (j.this.b) {
                List<HashMap<String, Object>> a = com.mob.mobapm.b.e.a(MobSDK.getContext()).a(new String[]{"Id", "trans"}, null, null, null, null, null);
                if (a != null && !a.isEmpty()) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("records", a);
                    try {
                        Object e = d.e(hashMap);
                        com.mob.mobapm.d.a.a().d("APM: upload transaction success. object:" + e, new Object[0]);
                        if ((e instanceof HashMap) && ((intValue = ((Integer) ((HashMap) e).get("code")).intValue()) == 200 || intValue == 4131002)) {
                            com.mob.mobapm.b.e.a(MobSDK.getContext()).a();
                        }
                    } catch (Throwable th) {
                        com.mob.mobapm.d.a.a().i("APM: upload transaction has error:" + th, new Object[0]);
                    }
                    j.this.a.sendEmptyMessageDelayed(0, c.c * 1000);
                    return;
                }
                j.this.a.sendEmptyMessageDelayed(0, c.c * 1000);
            }
        }
    }

    private j() {
    }

    public static synchronized j d() {
        j jVar;
        synchronized (j.class) {
            if (c == null) {
                c = new j();
            }
            jVar = c;
        }
        return jVar;
    }

    public void a(Transaction transaction) {
        synchronized (this.b) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("trans", com.mob.mobapm.e.h.a().a(transaction));
            com.mob.mobapm.b.e.a(MobSDK.getContext()).a(contentValues);
        }
    }

    @Override // com.mob.mobapm.core.k
    public void c() {
        if (c.e) {
            e.b().a(new a());
        }
    }
}
