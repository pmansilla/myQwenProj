package com.czw.smartkit.liveservice;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import com.czw.smartkit.bleModule.BleManager;
import com.czw.smartkit.homeModule.MainActivity;
import com.czw.smartkit.liveservice.RunServiceInterface;
import com.czw.utils.LogUtil;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.lang.time.DateUtils;
import ycble.runchinaup.core.BleConnState;
import ycble.runchinaup.core.callback.BleConnCallback;

/* loaded from: classes.dex */
public class BgMainService extends BaseService implements BleConnCallback {
    public static final int APP_NOTIFICATION_ID = 1111;
    private String TAG = "BgMainService";
    private InnerBinder innerBinder = null;
    private ServiceConnection serviceConnection = null;
    private BleManager bleManager = BleManager.getBleManager();
    private Timer timer = new Timer();

    private void initConnService() {
        if (this.serviceConnection == null) {
            this.serviceConnection = new ServiceConnection() { // from class: com.czw.smartkit.liveservice.BgMainService.2
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
                    LogUtil.e("BgMainService 远程服务挂掉了,远程服务被杀死");
                    Intent intent = new Intent(BgMainService.this, (Class<?>) GuardService.class);
                    if (Build.VERSION.SDK_INT > 25) {
                        BgMainService.this.startForegroundService(intent);
                    }
                    BgMainService.this.startService(intent);
                    BgMainService.this.bindService(new Intent(BgMainService.this, (Class<?>) GuardService.class), BgMainService.this.serviceConnection, 64);
                }
            };
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openNotify() {
        this.bleManager.registerConnCallback(this);
        PendingIntent.getActivity(this, 0, new Intent(this, (Class<?>) MainActivity.class), 0);
        this.bleManager.isConn();
        LogUtil.e("debug===>发送通知");
    }

    private void startGuardService() {
        if (ServiceUtil.isServiceExisted(getApplicationContext(), "com.czw.smartkit.liveservice.GuardService")) {
            return;
        }
        LogUtil.e("重新启动守护服务");
        Intent intent = new Intent(getApplicationContext(), (Class<?>) GuardService.class);
        if (Build.VERSION.SDK_INT > 25) {
            startForegroundService(intent);
        }
        startService(intent);
    }

    private void startTaskService() {
        if (ServiceUtil.isServiceExisted(getApplicationContext(), "com.czw.smartkit.liveservice.ServiceAfter5_0")) {
            return;
        }
        LogUtil.e("重新启动8.0以后的守护服务");
        if (Build.VERSION.SDK_INT >= 21) {
            Intent intent = new Intent(getApplicationContext(), (Class<?>) ServiceAfter5_0.class);
            if (Build.VERSION.SDK_INT > 25) {
                startForegroundService(intent);
            }
            startService(intent);
        }
    }

    @Override // com.czw.smartkit.liveservice.BaseService
    protected void init() {
        this.innerBinder = new InnerBinder("BgMainService");
        initConnService();
        this.timer.schedule(new TimerTask() { // from class: com.czw.smartkit.liveservice.BgMainService.1
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                ((NotificationManager) BgMainService.this.getSystemService("notification")).cancel(BgMainService.APP_NOTIFICATION_ID);
                BgMainService.this.openNotify();
            }
        }, 0L, DateUtils.MILLIS_PER_MINUTE);
    }

    @Override // android.app.Service
    @Nullable
    public IBinder onBind(Intent intent) {
        return this.innerBinder;
    }

    @Override // ycble.runchinaup.core.callback.BleConnCallback
    public void onConnState(BleConnState bleConnState) {
        ((NotificationManager) getSystemService("notification")).cancel(APP_NOTIFICATION_ID);
        openNotify();
    }

    @Override // com.czw.smartkit.liveservice.BaseService, android.app.Service
    public /* bridge */ /* synthetic */ void onCreate() {
        super.onCreate();
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        this.bleManager.unRegisterConnCallback(this);
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        startGuardService();
        startTaskService();
        return 1;
    }
}
