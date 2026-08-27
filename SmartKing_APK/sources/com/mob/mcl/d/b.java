package com.mob.mcl.d;

import com.mob.tools.MobLog;

/* compiled from: MCLog.java */
/* loaded from: classes.dex */
public class b {
    private static b a = new b();

    private b() {
    }

    public static b a() {
        return a;
    }

    public void a(String str) {
        MobLog.getInstance().d("[[MCLSDK]]" + str, new Object[0]);
    }

    public void a(Throwable th) {
        MobLog.getInstance().d(th, "[[MCLSDK]]", new Object[0]);
    }

    public void b(String str) {
        MobLog.getInstance().d("[[MCLSDK]]" + str, new Object[0]);
    }
}
