package com.mob.commons.b;

import com.mob.tools.MobLog;

/* compiled from: FidsLog.java */
/* loaded from: classes.dex */
public class c {
    private static final c a = new c();

    private c() {
    }

    public static c a() {
        return a;
    }

    private void b(Object obj) {
        try {
            if (obj instanceof Throwable) {
                MobLog.getInstance().d((Throwable) obj);
            } else {
                MobLog.getInstance().d(obj, new Object[0]);
            }
        } catch (Throwable unused) {
        }
    }

    public void a(Object obj) {
        b(obj);
    }
}
