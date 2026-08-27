package com.mob;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.mob.apc.impl.ACServiceImpl;

/* loaded from: classes.dex */
public class MobACService extends Service {
    private ACServiceImpl a = new ACServiceImpl(this);

    public int a(Intent intent, int i, int i2) {
        return super.onStartCommand(intent, i, i2);
    }

    public boolean a(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.a.onBind(intent);
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.a.onCreate();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        return this.a.onStartCommand(intent, i, i2);
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        return this.a.onUnbind(intent);
    }
}
