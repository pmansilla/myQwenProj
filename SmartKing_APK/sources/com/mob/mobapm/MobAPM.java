package com.mob.mobapm;

import com.mob.mobapm.core.c;
import com.mob.tools.proguard.ClassKeeper;

/* loaded from: classes.dex */
public class MobAPM implements ClassKeeper {
    public static boolean goldenKey = false;
    private static c impl;

    static {
        ensureInit();
    }

    private static void ensureInit() {
        if (impl == null) {
            synchronized (MobAPM.class) {
                if (impl == null) {
                    impl = new c();
                }
            }
        }
    }

    public static void setJson(String str) {
        ensureInit();
        impl.a(str);
    }
}
