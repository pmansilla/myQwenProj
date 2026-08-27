package com.mob.apc;

import android.content.Context;
import android.os.Looper;
import com.mob.apc.impl.APCImpl;
import com.mob.apc.impl.MAPCLog;
import java.util.List;

/* loaded from: classes.dex */
public class MobAPC {
    public static final int TYPE_AIDL = 1;
    private static Context a;

    /* loaded from: classes.dex */
    public interface MobAPCMessageListener {
        APCMessage onMessageReceive(String str, APCMessage aPCMessage, long j);
    }

    static {
        MAPCLog.getInstance().i("MOBAPC : 1.0.1", new Object[0]);
    }

    public static void addAPCMessageListener(long j, MobAPCMessageListener mobAPCMessageListener) {
        addAPCMessageListener("BS_FORWARD_" + j, mobAPCMessageListener);
    }

    public static void addAPCMessageListener(String str, MobAPCMessageListener mobAPCMessageListener) {
        APCImpl.getInstance().addMobIpcMsgListener(str, mobAPCMessageListener);
    }

    public static Context getContext() {
        return a;
    }

    public static List<String> getMAPCServiceList() {
        return APCImpl.getInstance().getMAPCServiceList();
    }

    public static void init(Context context) {
        a = context.getApplicationContext();
        APCImpl.getInstance().init(context);
    }

    public static APCMessage sendMessage(int i, String str, long j, APCMessage aPCMessage, long j2) throws Throwable {
        return sendMessage(i, str, "BS_FORWARD_" + j, aPCMessage, j2);
    }

    public static APCMessage sendMessage(int i, String str, String str2, APCMessage aPCMessage, long j) throws Throwable {
        if (Thread.currentThread().getId() != Looper.getMainLooper().getThread().getId()) {
            return APCImpl.getInstance().sendMessage(i, str, str2, aPCMessage, j);
        }
        MAPCLog.getInstance().i("[sendMessage] not allow main thread to invoke", new Object[0]);
        throw new APCException("not allow main thread to invoke");
    }
}
