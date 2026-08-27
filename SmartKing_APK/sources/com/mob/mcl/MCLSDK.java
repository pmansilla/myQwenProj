package com.mob.mcl;

import android.content.Context;
import android.os.Bundle;
import com.mob.mcl.b.a;
import com.mob.mcl.c.i;
import com.mob.mcl.d.b;

/* loaded from: classes.dex */
public class MCLSDK {
    public static final String SDK_TAG = "MCLSDK";

    /* loaded from: classes.dex */
    public interface ELPMessageListener {
        boolean messageReceived(Bundle bundle);
    }

    static {
        b.a().a("MCLSDK : 1.0.3");
    }

    public static void addBusinessMessageListener(int i, BusinessMessageListener businessMessageListener) {
        a.a(i, businessMessageListener);
    }

    public static void addELPMessageListener(ELPMessageListener eLPMessageListener) {
        a.a(eLPMessageListener);
    }

    public static void deleteMsg(String str) {
        i.c().a(str);
    }

    public static void getClientTcpStatus(BusinessCallBack<Boolean> businessCallBack) {
        i.c().a(businessCallBack);
    }

    public static long getCreateSuidTime() {
        return a.b();
    }

    public static String getSuid() {
        return a.c();
    }

    public static void getTcpStatus(BusinessCallBack<Boolean> businessCallBack) {
        i.c().b(businessCallBack);
    }

    public static void initMCLink(Context context, String str, String str2) {
        new a().a(context, str, str2);
    }

    public static boolean syncSuid(String str, long j) {
        return a.a(str, j);
    }
}
