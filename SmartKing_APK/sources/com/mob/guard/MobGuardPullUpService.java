package com.mob.guard;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.mob.guard.impl.e;
import com.mob.guard.impl.f;
import com.mob.tools.proguard.ClassKeeper;

/* loaded from: classes.dex */
public class MobGuardPullUpService extends Service implements ClassKeeper {
    private void getIntentData(Intent intent) {
        f.a(this, intent, false);
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        getIntentData(intent);
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        try {
            MobGuard.getSdkTag();
            e.a().d("[MobGuard] MobGuardPullUpService onCreate", new Object[0]);
        } catch (Throwable unused) {
        }
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent != null) {
            getIntentData(intent);
        }
        return super.onStartCommand(intent, i, i2);
    }
}
