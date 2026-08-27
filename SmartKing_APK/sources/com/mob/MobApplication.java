package com.mob;

import android.app.Application;
import com.mob.tools.proguard.ProtectedMemberKeeper;

/* loaded from: classes.dex */
public class MobApplication extends Application implements ProtectedMemberKeeper {
    protected String getAppSecret() {
        return null;
    }

    protected String getAppkey() {
        return null;
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        MobSDK.init(this, getAppkey(), getAppSecret());
    }
}
