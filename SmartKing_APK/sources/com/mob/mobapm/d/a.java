package com.mob.mobapm.d;

import com.mob.commons.MOBAPM;
import com.mob.commons.logcollector.DefaultLogsCollector;
import com.mob.tools.log.NLog;

/* loaded from: classes.dex */
public class a {
    private static NLog a;

    public static NLog a() {
        if (a == null) {
            synchronized (a.class) {
                if (a == null) {
                    b();
                }
            }
        }
        return a;
    }

    public static NLog b() {
        DefaultLogsCollector.get().addSDK(MOBAPM.sdkTag, MOBAPM.SDK_VERSION_CODE);
        NLog nLog = NLog.getInstance(MOBAPM.sdkTag);
        a = nLog;
        return nLog;
    }
}
