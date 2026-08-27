package ycble.runchinaup.aider;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import ycble.runchinaup.aider.callback.MsgCallback;
import ycble.runchinaup.aider.phone.NPPhoneStateReceiver;
import ycble.runchinaup.aider.sms.NPSmsReciver;
import ycble.runchinaup.log.ycBleLog;

/* loaded from: classes.dex */
public final class PushAiderHelper {
    private static PushAiderHelper aiderHelper = new PushAiderHelper();
    private MsgNotifyHelper notifyHelper = MsgNotifyHelper.getMsgNotifyHelper();
    private Handler handler = new Handler();
    private ReStartNotificationReceiver npLiveReceiver = new ReStartNotificationReceiver();
    private NPPhoneStateReceiver npPhoneStateReceiver = new NPPhoneStateReceiver();
    private NPSmsReciver npSmsReciver = new NPSmsReciver();

    private PushAiderHelper() {
    }

    public static PushAiderHelper getAiderHelper() {
        return aiderHelper;
    }

    private void registerReceiver(Context context) {
        context.registerReceiver(this.npPhoneStateReceiver, NPPhoneStateReceiver.createIntentFilter());
        context.registerReceiver(this.npSmsReciver, NPSmsReciver.createIntentFilter());
    }

    private void unregisterReceiver(Context context) {
        try {
            context.unregisterReceiver(this.npPhoneStateReceiver);
            context.unregisterReceiver(this.npSmsReciver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goToSettingAccessibility(Context context) {
        NotificationMsgUtil.goToSettingAccessibility(context);
    }

    public void goToSettingNotificationAccess(Context context) {
        NotificationMsgUtil.goToSettingNotificationAccess(context);
    }

    public boolean isNotifyEnable(Context context) {
        return NotificationMsgUtil.isEnabled(context);
    }

    public void setMsgReceiveCallback(MsgCallback msgCallback) {
        this.notifyHelper.setMsgCallback(msgCallback);
    }

    public void start(final Context context) {
        try {
            context.registerReceiver(this.npLiveReceiver, ReStartNotificationReceiver.createIntentFilter());
            NotificationMsgUtil.reStartNotifyListenService(context);
            this.handler.postDelayed(new Runnable() { // from class: ycble.runchinaup.aider.PushAiderHelper.1
                @Override // java.lang.Runnable
                public void run() {
                    if (!NotificationMsgUtil.isServiceExisted(context, NPNotificationService.class)) {
                        ycBleLog.e("监听服务已经开启");
                        return;
                    }
                    ycBleLog.e("如果5秒后没有启动服务，那么就开启service");
                    context.startService(new Intent(context, (Class<?>) NPNotificationService.class));
                    NotificationMsgUtil.reStartNotifyListenService(context);
                }
            }, 2000L);
            registerReceiver(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop(Context context) {
        try {
            context.unregisterReceiver(this.npLiveReceiver);
            context.stopService(new Intent(context, (Class<?>) NPNotificationService.class));
            NotificationMsgUtil.closeService(context);
            unregisterReceiver(context);
            this.handler.removeCallbacksAndMessages(null);
        } catch (Exception unused) {
        }
    }
}
