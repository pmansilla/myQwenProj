package com.mob.commons.authorize;

import android.content.Context;
import android.text.TextUtils;
import com.mob.commons.MobProduct;
import com.mob.commons.MobProductCollector;
import com.mob.commons.b;
import com.mob.commons.b.d;
import com.mob.tools.MobLog;
import com.mob.tools.proguard.PublicMemberKeeper;
import java.util.HashSet;

/* loaded from: classes.dex */
public final class DeviceAuthorizer implements PublicMemberKeeper {
    static String a;
    private static HashSet<String> b = new HashSet<>();
    private static Object c = new Object();

    /* JADX WARN: Type inference failed for: r1v6, types: [com.mob.commons.authorize.DeviceAuthorizer$1] */
    public static synchronized String authorize(final MobProduct mobProduct) {
        synchronized (DeviceAuthorizer.class) {
            boolean z = false;
            if (mobProduct != null) {
                try {
                    MobProductCollector.registerProduct(mobProduct);
                    z = !b.contains(mobProduct.getProductTag());
                    if (z) {
                        b.add(mobProduct.getProductTag());
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            if (TextUtils.isEmpty(a)) {
                a = new a().a(true, true);
                z = true;
            }
            if (!TextUtils.isEmpty(a)) {
                if (z) {
                    new Thread() { // from class: com.mob.commons.authorize.DeviceAuthorizer.1
                        @Override // java.lang.Thread, java.lang.Runnable
                        public void run() {
                            try {
                                String b2 = DeviceAuthorizer.b(MobProduct.this, DeviceAuthorizer.a);
                                if (TextUtils.isEmpty(b2)) {
                                    return;
                                }
                                DeviceAuthorizer.a = b2;
                            } catch (Throwable th2) {
                                MobLog.getInstance().d(th2);
                            }
                        }
                    }.start();
                }
                return a;
            }
            a = b(mobProduct, null);
            if (TextUtils.isEmpty(a)) {
                return new a().a();
            }
            return a;
        }
    }

    public static String authorizeForOnce() {
        if (isFor()) {
            return null;
        }
        return a != null ? a : new a().a(true, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String b(MobProduct mobProduct, String str) {
        synchronized (c) {
            a aVar = new a();
            com.mob.commons.a.b();
            if (b.ac() || !b.l()) {
                return aVar.a(false, true);
            }
            return aVar.a(mobProduct, str);
        }
    }

    public static String getMString(Context context) {
        return d.d(context);
    }

    public static boolean isFor() {
        return b.ad();
    }
}
