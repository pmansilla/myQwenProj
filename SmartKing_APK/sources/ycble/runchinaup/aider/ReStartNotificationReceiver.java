package ycble.runchinaup.aider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import ycble.runchinaup.log.ycBleLog;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class ReStartNotificationReceiver extends BroadcastReceiver {
    public static IntentFilter createIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.TIME_TICK");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        return intentFilter;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (NotificationMsgUtil.isServiceExisted(context, NPNotificationService.class)) {
            return;
        }
        ycBleLog.e("通知没有打开");
        PushAiderHelper.getAiderHelper().start(context);
    }
}
