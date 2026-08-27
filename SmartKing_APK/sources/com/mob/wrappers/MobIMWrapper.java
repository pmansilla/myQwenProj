package com.mob.wrappers;

import com.mob.tools.proguard.PublicMemberKeeper;

/* loaded from: classes2.dex */
public class MobIMWrapper extends SDKWrapper implements PublicMemberKeeper {
    private static int state;

    private static synchronized boolean isAvailable() {
        boolean z;
        synchronized (MobIMWrapper.class) {
            if (state == 0) {
                state = isAvailable("MOBIM");
            }
            z = state == 1;
        }
        return z;
    }
}
