package ycble.runchinaup.aider;

import android.service.notification.StatusBarNotification;
import ycble.runchinaup.aider.callback.MsgCallback;

/* loaded from: classes2.dex */
public final class MsgNotifyHelper {
    private static MsgNotifyHelper msgNotifyHelper = new MsgNotifyHelper();
    private MsgCallback msgCallback;

    private MsgNotifyHelper() {
    }

    public static MsgNotifyHelper getMsgNotifyHelper() {
        return msgNotifyHelper;
    }

    public void onAppMsgReceiver(String str, MsgType msgType, String str2, String str3) {
        if (this.msgCallback != null) {
            this.msgCallback.onAppMsgReceive(str, msgType, str2, str3);
        }
    }

    public void onMessageReceive(String str, String str2, String str3) {
        if (this.msgCallback != null) {
            this.msgCallback.onMessageReceive(str, str2, str3);
        }
    }

    public void onNotificationPost(StatusBarNotification statusBarNotification) {
        if (this.msgCallback != null) {
            this.msgCallback.onNotificationPost(statusBarNotification);
        }
    }

    public void onPhoneCallIng(String str, String str2, int i) {
        if (this.msgCallback != null) {
            this.msgCallback.onPhoneInComing(str, str2, i);
        }
    }

    public void setMsgCallback(MsgCallback msgCallback) {
        this.msgCallback = msgCallback;
    }
}
