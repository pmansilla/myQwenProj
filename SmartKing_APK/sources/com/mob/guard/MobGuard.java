package com.mob.guard;

import com.mob.guard.impl.b;
import com.mob.guard.impl.e;
import com.mob.guard.impl.f;
import com.mob.mcl.MCLSDK;
import com.mob.tools.proguard.ClassKeeper;

/* loaded from: classes.dex */
public class MobGuard implements ClassKeeper {
    public static final String SDK_TAG = "MOBGUARD";
    public static final int SDK_VERSION_CODE = 20002;
    public static final String SDK_VERSION_NAME = "2.0.2";

    static {
        b.a();
    }

    public static String getGuardId() {
        try {
            return MCLSDK.getSuid();
        } catch (Throwable th) {
            e.a().d(th);
            return null;
        }
    }

    public static String getSdkTag() {
        return SDK_TAG;
    }

    public static String getVersionTime() {
        return "2021.04.09 10:33";
    }

    public static void setOnAppActiveListener(OnAppActiveListener onAppActiveListener) {
        f.a(onAppActiveListener);
    }
}
