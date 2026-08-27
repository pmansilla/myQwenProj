package com.mob.mcl.c;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.mob.tools.utils.DeviceHelper;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: HeartBeatHelper.java */
/* loaded from: classes.dex */
public class c extends BroadcastReceiver {
    final /* synthetic */ d a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(d dVar) {
        this.a = dVar;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                String networkType = DeviceHelper.getInstance(context).getNetworkType();
                com.mob.mcl.d.b.a().a("ServiceInit receiver network " + networkType);
                int i = 0;
                if ("wifi".equalsIgnoreCase(networkType)) {
                    i = 1;
                } else if ("4G".equalsIgnoreCase(networkType)) {
                    i = 4;
                } else if ("3G".equalsIgnoreCase(networkType)) {
                    i = 3;
                } else if ("2G".equalsIgnoreCase(networkType)) {
                    i = 2;
                }
                d.a(this.a, i);
            }
        } catch (Throwable th) {
            com.mob.mcl.d.b.a().a(th);
        }
    }
}
