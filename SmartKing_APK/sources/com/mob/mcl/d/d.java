package com.mob.mcl.d;

import com.mob.MobSDK;
import com.mob.tools.utils.SharePrefrenceHelper;

/* compiled from: SpHelper.java */
/* loaded from: classes.dex */
public class d {
    private static SharePrefrenceHelper a;

    private static void a() {
        if (a == null) {
            SharePrefrenceHelper sharePrefrenceHelper = new SharePrefrenceHelper(MobSDK.getContext().getApplicationContext());
            a = sharePrefrenceHelper;
            sharePrefrenceHelper.open("mcl", 0);
        }
    }

    public static synchronized void a(long j) {
        synchronized (d.class) {
            a();
            a.putLong("create_suid_time", Long.valueOf(j));
        }
    }

    public static synchronized void a(String str) {
        synchronized (d.class) {
            a();
            a.putString("tcp_config", str);
        }
    }

    public static synchronized void a(boolean z) {
        synchronized (d.class) {
            a();
            a.putBoolean("use_config", Boolean.valueOf(z));
        }
    }

    public static synchronized String b() {
        String string;
        synchronized (d.class) {
            a();
            string = a.getString("tcp_config");
        }
        return string;
    }

    public static synchronized void b(String str) {
        synchronized (d.class) {
            a();
            a.putString("suid", str);
        }
    }

    public static synchronized String c() {
        String string;
        synchronized (d.class) {
            a();
            string = a.getString("suid");
        }
        return string;
    }

    public static synchronized long d() {
        long j;
        synchronized (d.class) {
            a();
            j = a.getLong("create_suid_time");
        }
        return j;
    }

    public static synchronized boolean e() {
        boolean z;
        synchronized (d.class) {
            a();
            z = a.getBoolean("use_config", true);
        }
        return z;
    }
}
