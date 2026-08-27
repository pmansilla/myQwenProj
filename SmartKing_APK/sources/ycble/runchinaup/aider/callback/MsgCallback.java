package ycble.runchinaup.aider.callback;

import android.service.notification.StatusBarNotification;
import ycble.runchinaup.aider.MsgType;

/* loaded from: classes.dex */
public abstract class MsgCallback {
    public abstract void onAppMsgReceive(String str, MsgType msgType, String str2, String str3);

    public abstract void onMessageReceive(String str, String str2, String str3);

    public void onNotificationPost(StatusBarNotification statusBarNotification) {
    }

    public abstract void onPhoneInComing(String str, String str2, int i);
}
