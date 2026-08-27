package com.mob.apc.impl;

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import com.mob.MobACService;
import com.mob.apc.MobAPC;

/* loaded from: classes.dex */
public class ACServiceImpl {
    private final IAidlInterface iBinder = new IAidlInterface() { // from class: com.mob.apc.impl.ACServiceImpl.1
        @Override // com.mob.apc.impl.IAidlInterface
        public InnerMessage send(InnerMessage innerMessage) throws RemoteException {
            return APCImpl.getInstance().onAIDLMessageReceive(innerMessage);
        }
    };
    private MobACService service;

    public ACServiceImpl(MobACService mobACService) {
        this.service = mobACService;
    }

    public IBinder onBind(Intent intent) {
        return this.iBinder;
    }

    public void onCreate() {
        try {
            MobAPC.init(this.service.getApplicationContext());
        } catch (Throwable th) {
            MAPCLog.getInstance().d(th);
        }
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        return this.service.a(intent, i, i2);
    }

    public boolean onUnbind(Intent intent) {
        return this.service.a(intent);
    }
}
