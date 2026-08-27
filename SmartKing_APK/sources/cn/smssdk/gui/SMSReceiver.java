package cn.smssdk.gui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

/* loaded from: classes.dex */
public class SMSReceiver extends BroadcastReceiver {
    private static final String ACTION_SMS_RECEIVER = "android.provider.Telephony.SMS_RECEIVED";
    private SMSSDK.VerifyCodeReadListener listener;

    public SMSReceiver() {
        SMSLog.getInstance().i("SMSReceiver:Please dynamically register an instance of this class with Context.registerReceiver.\r\nIf not, the SMSSDK.VerifyCodeReadListener will be null!", new Object[0]);
    }

    public SMSReceiver(SMSSDK.VerifyCodeReadListener verifyCodeReadListener) {
        this.listener = verifyCodeReadListener;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Bundle extras;
        if (!ACTION_SMS_RECEIVER.equals(intent.getAction()) || (extras = intent.getExtras()) == null) {
            return;
        }
        Object[] objArr = (Object[]) extras.get("pdus");
        SmsMessage[] smsMessageArr = new SmsMessage[objArr.length];
        for (int i = 0; i < objArr.length; i++) {
            smsMessageArr[i] = SmsMessage.createFromPdu((byte[]) objArr[i]);
        }
        for (SmsMessage smsMessage : smsMessageArr) {
            if (smsMessage != null) {
                SMSSDK.readVerificationCode(smsMessage, this.listener);
            }
        }
    }
}
