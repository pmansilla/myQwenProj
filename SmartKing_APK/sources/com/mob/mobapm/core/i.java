package com.mob.mobapm.core;

import android.content.ContentValues;
import com.mob.MobSDK;
import com.mob.tools.utils.DeviceHelper;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class i extends k {
    private static i c;

    /* loaded from: classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            int intValue;
            synchronized (i.this.b) {
                if (!"wifi".equalsIgnoreCase(DeviceHelper.getInstance(MobSDK.getContext()).getNetworkType())) {
                    com.mob.mobapm.d.a.a().d("APM: upload uploadTransaction Data failed, not wifi", new Object[0]);
                    i.this.a.sendEmptyMessageDelayed(0, c.c * 1000);
                    return;
                }
                List<HashMap<String, Object>> b = com.mob.mobapm.b.e.a(MobSDK.getContext()).b(new String[]{"Id", "trans"}, null, null, null, null, null);
                if (b != null && !b.isEmpty()) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("records", b);
                    try {
                        Object f = d.f(hashMap);
                        com.mob.mobapm.d.a.a().d("APM: upload uploadTransaction Data success. object:" + f, new Object[0]);
                        if ((f instanceof HashMap) && ((intValue = ((Integer) ((HashMap) f).get("code")).intValue()) == 200 || intValue == 4131002)) {
                            com.mob.mobapm.b.e.a(MobSDK.getContext()).b();
                        }
                    } catch (Throwable th) {
                        com.mob.mobapm.d.a.a().i("APM: upload transaction has error:" + th, new Object[0]);
                    }
                    i.this.a.sendEmptyMessageDelayed(0, c.c * 1000);
                    return;
                }
                i.this.a.sendEmptyMessageDelayed(0, c.c * 1000);
            }
        }
    }

    private i() {
    }

    public static synchronized i d() {
        i iVar;
        synchronized (i.class) {
            if (c == null) {
                c = new i();
                com.mob.mobapm.b.e.a(MobSDK.getContext()).c();
            }
            iVar = c;
        }
        return iVar;
    }

    public void a(Transaction transaction) {
        synchronized (this.b) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("trans", com.mob.mobapm.e.h.a().b(transaction));
            com.mob.mobapm.b.e.a(MobSDK.getContext()).b(contentValues);
        }
    }

    @Override // com.mob.mobapm.core.k
    public void c() {
        if (c.e) {
            e.b().a(new a());
        }
    }
}
