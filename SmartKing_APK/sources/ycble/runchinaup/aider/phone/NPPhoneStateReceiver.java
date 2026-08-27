package ycble.runchinaup.aider.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import me.panpf.sketch.uri.FileUriModel;
import ycble.runchinaup.aider.MsgNotifyHelper;
import ycble.runchinaup.log.ycBleLog;

/* loaded from: classes2.dex */
public final class NPPhoneStateReceiver extends BroadcastReceiver {
    private static final String PHONE_STATE = "android.intent.action.PHONE_STATE";
    private String lastStateAndNumber = "";

    public static IntentFilter createIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PHONE_STATE);
        intentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        return intentFilter;
    }

    public void onCallStateChanged(Context context, String str, String str2) {
        ycBleLog.e("state:" + str + ";incomingNumber:" + str2);
        if (TextUtils.isEmpty(str2)) {
            ycBleLog.e("来电号码为空，没有读取通话记录权限，不往下执行");
            return;
        }
        String str3 = str + FileUriModel.SCHEME + str2;
        if (str3.equalsIgnoreCase(this.lastStateAndNumber)) {
            return;
        }
        this.lastStateAndNumber = str3;
        String queryContact = NPContactsUtil.queryContact(context, str2);
        if (str.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
            ycBleLog.e("NPPhoneStateListener==>手机铃声响了，来电人:" + queryContact);
            MsgNotifyHelper.getMsgNotifyHelper().onPhoneCallIng(str2, queryContact, 0);
            return;
        }
        if (str.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)) {
            ycBleLog.e("NPPhoneStateListener==>非通话状态" + queryContact);
            MsgNotifyHelper.getMsgNotifyHelper().onPhoneCallIng(str2, queryContact, 2);
            return;
        }
        if (str.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            ycBleLog.e("NPPhoneStateListener==>电话被接通了,可能是打出去的，也可能是接听的" + str2);
            MsgNotifyHelper.getMsgNotifyHelper().onPhoneCallIng(str2, queryContact, 1);
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equalsIgnoreCase(PHONE_STATE)) {
            onCallStateChanged(context, intent.getStringExtra("state"), intent.getStringExtra("incoming_number"));
        } else if (action.equalsIgnoreCase("android.intent.action.NEW_OUTGOING_CALL")) {
            ycBleLog.e("NPPhoneStateListener==>拨打电话出去");
        }
    }
}
