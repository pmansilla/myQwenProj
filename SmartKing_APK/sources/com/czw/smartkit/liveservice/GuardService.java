package com.czw.smartkit.liveservice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import com.czw.smartkit.liveservice.RunServiceInterface;
import com.czw.utils.LogUtil;

/* loaded from: classes.dex */
public class GuardService extends BaseService {
    private InnerBinder innerBinder = null;
    private ServiceConnection serviceConnection = null;

    private void initConnService() {
        if (this.serviceConnection == null) {
            this.serviceConnection = new ServiceConnection() { // from class: com.czw.smartkit.liveservice.GuardService.1
                @Override // android.content.ServiceConnection
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    try {
                        LogUtil.e("连接上了==>" + RunServiceInterface.Stub.asInterface(iBinder).getServiceName());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

                @Override // android.content.ServiceConnection
                public void onServiceDisconnected(ComponentName componentName) {
                    LogUtil.e("GuardService 远程服务挂掉了,远程服务被杀死");
                    Intent intent = new Intent(GuardService.this, (Class<?>) BgMainService.class);
                    if (Build.VERSION.SDK_INT > 25) {
                        GuardService.this.startForegroundService(intent);
                    }
                    GuardService.this.startService(intent);
                    GuardService.this.bindService(intent, GuardService.this.serviceConnection, 64);
                }
            };
        }
    }

    @Override // com.czw.smartkit.liveservice.BaseService
    protected void init() {
        this.innerBinder = new InnerBinder("GuardService");
        initConnService();
    }

    @Override // android.app.Service
    @Nullable
    public IBinder onBind(Intent intent) {
        return this.innerBinder;
    }

    @Override // com.czw.smartkit.liveservice.BaseService, android.app.Service
    public /* bridge */ /* synthetic */ void onCreate() {
        super.onCreate();
    }
}
