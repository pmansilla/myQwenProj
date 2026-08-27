package com.loc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.ArrayList;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class dp extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        String str;
        try {
            if (!isInitialStickyBroadcast() && intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                String a = Cdo.a();
                if (a != "None_Network") {
                    str = Cdo.c;
                    if (!a.equalsIgnoreCase(str)) {
                        dk.a("[BroadcastReceiver.onReceive] - Network state changed");
                        dg.a();
                        ArrayList d = dg.d();
                        dg.a();
                        dg.c();
                        if (Cdo.a && dc.a != null) {
                            dk.a("[BroadcastReceiver.onReceive] - refresh host");
                            dc.a.a(d);
                        }
                    }
                }
                String unused = Cdo.c = a;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
