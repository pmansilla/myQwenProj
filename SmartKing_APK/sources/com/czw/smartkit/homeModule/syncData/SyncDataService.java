package com.czw.smartkit.homeModule.syncData;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.czw.utils.LogUtil;

/* loaded from: classes.dex */
public class SyncDataService extends Service {
    private SyncDataUtil syncDataUtil = SyncDataUtil.getInstance();

    @Override // android.app.Service
    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        LogUtil.e("==同步------->onStartCommand");
        this.syncDataUtil.startSyncAllData();
        return super.onStartCommand(intent, i, i2);
    }
}
