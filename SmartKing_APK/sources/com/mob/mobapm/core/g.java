package com.mob.mobapm.core;

import android.text.TextUtils;
import com.mob.MobSDK;
import com.mob.tools.utils.DeviceHelper;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class g {
    private static volatile g a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements Runnable {
        a(g gVar) {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                String d = com.mob.mobapm.b.a.d();
                String valueOf = String.valueOf(DeviceHelper.getInstance(MobSDK.getContext()).getAppVersion());
                if (!TextUtils.isEmpty(d) && !valueOf.equals(d)) {
                    com.mob.mobapm.b.a.l();
                    com.mob.mobapm.b.a.k();
                    return;
                }
                HashMap<String, Object> h = com.mob.mobapm.b.a.h();
                if (h != null && !h.isEmpty()) {
                    HashMap hashMap = new HashMap();
                    ArrayList arrayList = new ArrayList();
                    arrayList.addAll(h.values());
                    hashMap.put("bundleName", MobSDK.getContext().getPackageName());
                    hashMap.put("uploadTime", Long.valueOf(System.currentTimeMillis()));
                    hashMap.put("errorStack", arrayList);
                    Object a = d.a(hashMap, c.f);
                    com.mob.mobapm.d.a.a().d("APM: upload crash success. object:" + a, new Object[0]);
                    if (a instanceof HashMap) {
                        int intValue = ((Integer) ((HashMap) a).get("code")).intValue();
                        if (intValue == 200 || intValue == 4131002) {
                            com.mob.mobapm.b.a.l();
                        }
                    }
                }
            } catch (Throwable th) {
                com.mob.mobapm.d.a.a().d("APM: upload crash fail. error:" + th, new Object[0]);
            }
        }
    }

    private g() {
    }

    public static g b() {
        if (a == null) {
            synchronized (g.class) {
                if (a == null) {
                    a = new g();
                }
            }
        }
        return a;
    }

    public void a() {
        e.b().a(new a(this));
    }
}
