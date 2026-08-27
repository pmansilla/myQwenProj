package com.mob.guard.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* loaded from: classes.dex */
public class PingBroadcast extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent == null || context.getPackageName().equals(intent.getPackage())) {
            return;
        }
        String action = intent.getAction();
        e.a().d("[GuardConnect] PING broadcast received", new Object[0]);
        if ("com.mob.guard.intent.PING".equals(action)) {
            a.d().h();
        }
    }
}
