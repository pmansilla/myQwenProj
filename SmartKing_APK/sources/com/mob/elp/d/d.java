package com.mob.elp.d;

import com.mob.tools.MobLog;

/* compiled from: ELPLog.java */
/* loaded from: classes.dex */
public class d {
    private static d a = new d();

    private d() {
    }

    public static d a() {
        return a;
    }

    public void a(String str) {
        MobLog.getInstance().d("[[ELPSDK]]" + str, new Object[0]);
    }

    public void a(Throwable th) {
        MobLog.getInstance().d(th, "[[ELPSDK]]", new Object[0]);
    }
}
