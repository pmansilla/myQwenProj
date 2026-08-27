package ycble.runchinaup.aider;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import java.util.Iterator;
import java.util.List;
import ycble.runchinaup.log.ycBleLog;

/* loaded from: classes2.dex */
public class NPAccessibilityService extends AccessibilityService {
    public static boolean NPAccessibilityServiceCanReceive = false;
    private static String lastMsgStr;

    @Deprecated
    public static void clearLastMessage() {
        if (lastMsgStr == null || TextUtils.isEmpty(lastMsgStr)) {
            return;
        }
        lastMsgStr = null;
    }

    public void handMsg(String str, String str2, String str3) {
        String str4 = str + str2 + str3;
        MsgType pck2MsgType = MsgType.pck2MsgType(str);
        if (TextUtils.isEmpty(lastMsgStr)) {
            lastMsgStr = str4;
        } else if (!lastMsgStr.equals(str4)) {
            lastMsgStr = str4;
        }
        if (pck2MsgType != null) {
            MsgNotifyHelper.getMsgNotifyHelper().onAppMsgReceiver(str, pck2MsgType, str2, str3);
        }
    }

    @Override // android.accessibilityservice.AccessibilityService
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        String[] split;
        if (accessibilityEvent.getEventType() == 64 && !accessibilityEvent.getPackageName().equals(getPackageName())) {
            if (NPNotificationService.NPNotificationServiceCanReceive) {
                ycBleLog.e("通知栏可以获取到消息，就不用辅助推送了");
                return;
            }
            NPAccessibilityServiceCanReceive = true;
            String charSequence = accessibilityEvent.getPackageName().toString();
            Notification notification = (Notification) accessibilityEvent.getParcelableData();
            if (notification == null) {
                ycBleLog.e("通知栏内容为空，不推送消息");
                return;
            }
            String str = "";
            if (notification.tickerText != null) {
                str = notification.tickerText.toString();
            } else {
                List<CharSequence> text = accessibilityEvent.getText();
                if (text != null) {
                    StringBuilder sb = new StringBuilder();
                    Iterator<CharSequence> it = text.iterator();
                    while (it.hasNext()) {
                        sb.append(it.next());
                    }
                    str = sb.toString();
                }
            }
            if (TextUtils.isEmpty(str) || (split = str.split(":")) == null || split.length <= 1) {
                return;
            }
            handMsg(charSequence, split[0], split[1]);
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
            startForeground(1, new Notification());
        }
    }

    @Override // android.accessibilityservice.AccessibilityService
    public void onInterrupt() {
    }

    @Override // android.accessibilityservice.AccessibilityService
    protected void onServiceConnected() {
        ycBleLog.e("辅助通知栏服务正常，可以获取到通知信息");
        AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
        accessibilityServiceInfo.eventTypes = 64;
        accessibilityServiceInfo.notificationTimeout = 100L;
        accessibilityServiceInfo.feedbackType = -1;
        setServiceInfo(accessibilityServiceInfo);
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        if (Build.VERSION.SDK_INT >= 26) {
            startForeground(1, new Notification());
        }
        return super.onStartCommand(intent, i, i2);
    }
}
