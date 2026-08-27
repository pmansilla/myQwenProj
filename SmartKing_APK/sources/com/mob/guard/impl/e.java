package com.mob.guard.impl;

import com.mob.guard.MobGuard;
import com.mob.tools.MobLog;
import com.mob.tools.log.LogCollector;
import com.mob.tools.log.NLog;

/* loaded from: classes.dex */
public class e {
    private static NLog a;
    public static String b = MobGuard.getSdkTag();
    private static Object c = new Object();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class a implements LogCollector {
        a() {
        }

        @Override // com.mob.tools.log.LogCollector
        public void log(String str, int i, int i2, String str2, String str3) {
            MobLog.getInstance().d("[[MOBGUARD]]" + str3, new Object[0]);
        }
    }

    public static NLog a() {
        if (a == null) {
            synchronized (c) {
                if (a == null) {
                    b();
                }
            }
        }
        return a;
    }

    public static NLog b() {
        NLog nLog = NLog.getInstance(b);
        a = nLog;
        nLog.setCollector(new a());
        return a;
    }
}
