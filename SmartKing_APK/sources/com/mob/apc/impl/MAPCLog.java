package com.mob.apc.impl;

import com.mob.tools.MobLog;

/* loaded from: classes.dex */
public class MAPCLog {
    private static MAPCLog instance = new MAPCLog();

    private MAPCLog() {
    }

    public static MAPCLog getInstance() {
        return instance;
    }

    public void d(Throwable th) {
        MobLog.getInstance().d(th, "[[MOBAPC]]", new Object[0]);
    }

    public void e(Throwable th) {
        MobLog.getInstance().e(th, "[[MOBAPC]]", new Object[0]);
    }

    public void i(String str, Object... objArr) {
        MobLog.getInstance().d("[[MOBAPC]]" + String.format(str, objArr), new Object[0]);
    }
}
