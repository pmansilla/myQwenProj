package com.loc;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/* renamed from: com.loc.do, reason: invalid class name */
/* loaded from: classes.dex */
public final class Cdo {
    static boolean a = false;
    private static Context b;
    private static String c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ String a() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) b.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable()) {
            return "None_Network";
        }
        String typeName = activeNetworkInfo.getTypeName();
        dk.a("[detectCurrentNetwork] - Network name:" + typeName + " subType name: " + activeNetworkInfo.getSubtypeName());
        return typeName != null ? typeName : "None_Network";
    }

    public static void a(Context context) {
        if (context == null) {
            throw new IllegalStateException("Context can't be null");
        }
        b = context;
        dp dpVar = new dp();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        b.registerReceiver(dpVar, intentFilter);
    }
}
