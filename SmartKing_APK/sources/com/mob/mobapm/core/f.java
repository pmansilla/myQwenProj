package com.mob.mobapm.core;

import android.text.TextUtils;
import com.mob.MobSDK;
import com.mob.tools.utils.DeviceHelper;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class f {
    private static volatile f a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class a implements Runnable {
        a(f fVar) {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                String c = com.mob.mobapm.b.a.c();
                String valueOf = String.valueOf(DeviceHelper.getInstance(MobSDK.getContext()).getAppVersion());
                if (!TextUtils.isEmpty(c) && !valueOf.equals(c)) {
                    com.mob.mobapm.b.a.l();
                    com.mob.mobapm.b.a.k();
                    return;
                }
                HashMap<String, Object> b = com.mob.mobapm.b.a.b();
                if (b != null && !b.isEmpty()) {
                    HashMap hashMap = new HashMap();
                    ArrayList arrayList = new ArrayList();
                    arrayList.addAll(b.values());
                    hashMap.put("bundleName", MobSDK.getContext().getPackageName());
                    hashMap.put("uploadTime", Long.valueOf(System.currentTimeMillis()));
                    hashMap.put("errorStack", arrayList);
                    Object a = d.a(hashMap, c.g);
                    com.mob.mobapm.d.a.a().d("APM: upload anr success. object:" + a, new Object[0]);
                    if (a instanceof HashMap) {
                        int intValue = ((Integer) ((HashMap) a).get("code")).intValue();
                        if (intValue == 200 || intValue == 4131002) {
                            com.mob.mobapm.b.a.k();
                        }
                    }
                }
            } catch (Throwable th) {
                com.mob.mobapm.d.a.a().d("APM: upload anr fail. error:" + th, new Object[0]);
            }
        }
    }

    private f() {
    }

    public static f b() {
        if (a == null) {
            synchronized (f.class) {
                if (a == null) {
                    a = new f();
                }
            }
        }
        return a;
    }

    public void a() {
        e.b().a(new a(this));
    }
}
