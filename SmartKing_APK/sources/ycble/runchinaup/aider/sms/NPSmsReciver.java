package ycble.runchinaup.aider.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import com.google.gson.Gson;
import ycble.runchinaup.aider.MsgNotifyHelper;
import ycble.runchinaup.aider.phone.NPContactsUtil;
import ycble.runchinaup.log.ycBleLog;

/* loaded from: classes2.dex */
public class NPSmsReciver extends BroadcastReceiver {
    public static String messageWithNoPermissionText = "请授予app读取短信权限,否则无法显示短信内容";
    private static String strLastContent;

    @Deprecated
    public static void clearLastMessage() {
        strLastContent = null;
    }

    public static IntentFilter createIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(Integer.MAX_VALUE);
        return intentFilter;
    }

    public static void setMessageWithNoPermissionText(String str) {
        messageWithNoPermissionText = str;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Object[] objArr;
        ycBleLog.e("debug sms短信来了===>");
        Bundle extras = intent.getExtras();
        StringBuilder sb = new StringBuilder();
        if (extras == null || (objArr = (Object[]) extras.get("pdus")) == null) {
            return;
        }
        String str = null;
        for (Object obj : objArr) {
            SmsMessage createFromPdu = SmsMessage.createFromPdu((byte[]) obj);
            if (createFromPdu != null) {
                str = createFromPdu.getOriginatingAddress();
                String displayMessageBody = createFromPdu.getDisplayMessageBody();
                ycBleLog.e("短信:" + new Gson().toJson(new String[]{str, displayMessageBody}));
                if (!TextUtils.isEmpty(displayMessageBody)) {
                    sb.append(displayMessageBody);
                }
            }
        }
        if (TextUtils.isEmpty(sb.toString())) {
            sb.append(messageWithNoPermissionText);
        }
        MsgNotifyHelper.getMsgNotifyHelper().onMessageReceive(str, NPContactsUtil.queryContact(context, str), sb.toString());
        strLastContent = sb.toString();
    }
}
