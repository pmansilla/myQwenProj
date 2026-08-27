package ycble.runchinaup.aider;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import me.panpf.sketch.uri.FileUriModel;
import ycble.runchinaup.log.ycBleLog;

/* loaded from: classes2.dex */
public final class NPNotificationService extends NotificationListenerService {
    public static boolean NPNotificationServiceCanReceive = false;
    private static String lastMsgStr;

    @Deprecated
    public static void clearLastMessage() {
        if (lastMsgStr == null || TextUtils.isEmpty(lastMsgStr)) {
            return;
        }
        lastMsgStr = null;
    }

    public void handMsg(String str, String str2, String str3) {
        int indexOf;
        int indexOf2;
        String str4 = str + "/from:" + str2 + "/msgContent:" + str3;
        MsgType pck2MsgType = MsgType.pck2MsgType(str);
        if (pck2MsgType == MsgType.WECHAT) {
            if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str3) && str3.length() > 4 && (indexOf2 = str3.indexOf(str2)) != -1) {
                str3 = str3.substring(indexOf2 + str2.length() + 1);
            }
        } else if (pck2MsgType == MsgType.Instagram && !TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str3) && str3.length() > str2.length() && (indexOf = str3.indexOf(str2)) != -1) {
            str3 = str3.substring(indexOf + str2.length() + 1);
        }
        ycBleLog.e(pck2MsgType + FileUriModel.SCHEME + str4);
        MsgNotifyHelper.getMsgNotifyHelper().onAppMsgReceiver(str, pck2MsgType, str2, str3);
        lastMsgStr = str4;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        ycBleLog.e("NPNotificationService===>onCreate");
    }

    @Override // android.service.notification.NotificationListenerService
    public void onListenerConnected() {
        super.onListenerConnected();
        ycBleLog.e("onListenerConnected====>通知栏服务正常，可以获取到通知信息");
    }

    @Override // android.service.notification.NotificationListenerService
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        ycBleLog.e("onListenerConnected====>通知栏服务不正常，不可以获取到通知信息");
    }

    @Override // android.service.notification.NotificationListenerService
    @RequiresApi(api = 19)
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        String str;
        super.onNotificationPosted(statusBarNotification);
        ycBleLog.e("通知栏===>onNotificationPosted");
        NPNotificationServiceCanReceive = true;
        MsgNotifyHelper.getMsgNotifyHelper().onNotificationPost(statusBarNotification);
        if (statusBarNotification == null) {
            return;
        }
        String packageName = statusBarNotification.getPackageName();
        if (TextUtils.isEmpty(packageName)) {
            return;
        }
        if (Build.VERSION.SDK_INT < 18) {
            ycBleLog.e("Android platform version is lower than 18.");
            return;
        }
        Bundle bundle = statusBarNotification.getNotification().extras;
        if (bundle == null) {
            return;
        }
        String string = bundle.getString(NotificationCompat.EXTRA_TITLE);
        String str2 = TextUtils.isEmpty(string) ? "" : string;
        try {
            str = bundle.getCharSequence(NotificationCompat.EXTRA_TEXT).toString();
        } catch (NullPointerException unused) {
            str = "";
        }
        ycBleLog.e("通知栏获取到消息==>{" + str + "}===>pckName:" + packageName);
        handMsg(packageName, str2, str);
    }

    @Override // android.service.notification.NotificationListenerService
    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {
        super.onNotificationRemoved(statusBarNotification);
    }

    @Override // android.service.notification.NotificationListenerService
    public void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
        super.onNotificationRemoved(statusBarNotification, rankingMap);
        ycBleLog.e("onNotificationRemoved====>");
        if (NotificationMsgUtil.isServiceExisted(this, NPNotificationService.class)) {
            return;
        }
        startService(new Intent(this, (Class<?>) NPNotificationService.class));
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        ycBleLog.e("通知栏onStartCommand");
        return super.onStartCommand(intent, i, i2);
    }
}
