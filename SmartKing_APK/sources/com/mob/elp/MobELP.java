package com.mob.elp;

import com.mob.elp.a.a;
import com.mob.elp.d.d;

/* loaded from: classes.dex */
public class MobELP {
    private static final String SDK_TAG = "ELPSDK";
    private static final String SDK_VERSION = "1.0.4";

    /* loaded from: classes.dex */
    public interface PushMessageListener {
        void messageReceived(PushMessage pushMessage);
    }

    static {
        d.a().a("ELPSDK : 1.0.4");
    }

    public static void addMessageListener(String str, PushMessageListener pushMessageListener) {
        a.b().a(str, pushMessageListener);
    }

    public static void init(String str) {
        a.b().a(str);
    }
}
