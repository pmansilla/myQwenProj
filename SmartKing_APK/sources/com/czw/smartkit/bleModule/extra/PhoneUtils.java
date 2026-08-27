package com.czw.smartkit.bleModule.extra;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import com.android.internal.telephony.ITelephony;
import com.sun.mail.imap.IMAPStore;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class PhoneUtils {
    private static final String TAG = "ta";

    private PhoneUtils() {
    }

    public static void answerCall(Context context) {
        if (Build.VERSION.SDK_INT >= 28) {
            TelecomManager telecomManager = (TelecomManager) context.getSystemService("telecom");
            if (telecomManager != null) {
                telecomManager.acceptRingingCall();
                return;
            }
            return;
        }
        try {
            Log.e(TAG, "用于Android2.3及2.3以上的版本上");
            Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
            intent.putExtra("android.intent.extra.KEY_EVENT", new KeyEvent(0, 79));
            context.sendOrderedBroadcast(intent, "android.permission.CALL_PRIVILEGED");
            Intent intent2 = new Intent("android.intent.action.MEDIA_BUTTON");
            intent2.putExtra("android.intent.extra.KEY_EVENT", new KeyEvent(1, 79));
            context.sendOrderedBroadcast(intent2, "android.permission.CALL_PRIVILEGED");
            Intent intent3 = new Intent("android.intent.action.HEADSET_PLUG");
            intent3.addFlags(1073741824);
            intent3.putExtra("state", 1);
            intent3.putExtra("microphone", 1);
            intent3.putExtra(IMAPStore.ID_NAME, "Headset");
            context.sendOrderedBroadcast(intent3, "android.permission.CALL_PRIVILEGED");
            Intent intent4 = new Intent("android.intent.action.MEDIA_BUTTON");
            intent4.putExtra("android.intent.extra.KEY_EVENT", new KeyEvent(0, 79));
            context.sendOrderedBroadcast(intent4, "android.permission.CALL_PRIVILEGED");
            Intent intent5 = new Intent("android.intent.action.MEDIA_BUTTON");
            intent5.putExtra("android.intent.extra.KEY_EVENT", new KeyEvent(1, 79));
            context.sendOrderedBroadcast(intent5, "android.permission.CALL_PRIVILEGED");
            Intent intent6 = new Intent("android.intent.action.HEADSET_PLUG");
            intent6.addFlags(1073741824);
            intent6.putExtra("state", 0);
            intent6.putExtra("microphone", 1);
            intent6.putExtra(IMAPStore.ID_NAME, "Headset");
            context.sendOrderedBroadcast(intent6, "android.permission.CALL_PRIVILEGED");
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent7 = new Intent("android.intent.action.MEDIA_BUTTON");
            intent7.putExtra("android.intent.extra.KEY_EVENT", new KeyEvent(1, 79));
            context.sendOrderedBroadcast(intent7, null);
        }
    }

    public static void endCall(Context context) {
        if (Build.VERSION.SDK_INT >= 28) {
            TelecomManager telecomManager = (TelecomManager) context.getSystemService("telecom");
            if (telecomManager != null) {
                telecomManager.endCall();
                return;
            }
            return;
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        try {
            Method declaredMethod = TelephonyManager.class.getDeclaredMethod("getITelephony", new Class[0]);
            declaredMethod.setAccessible(true);
            Log.e("bleService", "End call.");
            ((ITelephony) declaredMethod.invoke(telephonyManager, new Object[0])).endCall();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bleService", "Fail to answer ring call.", e);
        }
    }
}
