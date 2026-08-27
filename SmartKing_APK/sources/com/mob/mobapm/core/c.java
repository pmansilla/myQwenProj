package com.mob.mobapm.core;

import android.text.TextUtils;
import com.mob.MobSDK;
import com.mob.mobapm.MobAPM;
import com.mob.tools.utils.Hashon;
import com.wx.wheelview.common.WheelConstants;
import java.util.HashMap;

/* loaded from: classes.dex */
public class c {
    public static boolean b = false;
    public static int c = 300;
    public static int d = 300;
    public static boolean e = false;
    public static boolean f = false;
    public static boolean g = false;
    public static boolean h = false;
    public static boolean i = false;
    public static int j;
    private String a;

    private void b(String str) {
        HashMap fromJson;
        int intValue;
        try {
            if (TextUtils.isEmpty(str) || (fromJson = new Hashon().fromJson(str)) == null || fromJson.isEmpty()) {
                return;
            }
            Integer num = (Integer) fromJson.get("apm");
            if (num != null) {
                b = num.intValue() == 1;
            }
            try {
                if (MobSDK.isForb()) {
                    return;
                }
                if ((MobSDK.isAuth() == 2 || MobSDK.isAuth() == 1) && b) {
                    MobAPM.goldenKey = true;
                    HashMap<String, Object> d2 = d.d();
                    if (d2 == null || d2.isEmpty()) {
                        return;
                    }
                    Object obj = d2.get("openSentinel");
                    if (obj != null) {
                        e = ((Boolean) obj).booleanValue();
                    }
                    Object obj2 = d2.get("stuckCollection");
                    if (obj2 != null) {
                        g = ((Boolean) obj2).booleanValue();
                    }
                    Object obj3 = d2.get("crashCollection");
                    if (obj3 != null) {
                        f = ((Boolean) obj3).booleanValue();
                    }
                    Object obj4 = d2.get("socketCollection");
                    if (obj4 != null) {
                        h = ((Boolean) obj4).booleanValue();
                    }
                    Object obj5 = d2.get("dnsCollection");
                    if (obj5 != null) {
                        i = ((Boolean) obj5).booleanValue();
                    }
                    Object obj6 = d2.get("apmhuh");
                    if (obj6 != null) {
                        int intValue2 = ((Integer) obj6).intValue();
                        c = intValue2;
                        if (intValue2 <= 0) {
                            c = WheelConstants.WHEEL_SCROLL_DELAY_DURATION;
                        }
                    }
                    Object obj7 = d2.get("apmauh");
                    if (obj7 != null) {
                        int intValue3 = ((Integer) obj7).intValue();
                        d = intValue3;
                        if (intValue3 <= 0) {
                            d = WheelConstants.WHEEL_SCROLL_DELAY_DURATION;
                        }
                    }
                    Object obj8 = d2.get("httpAnalysisSize");
                    if (obj8 == null || (intValue = ((Integer) obj8).intValue()) < 0 || intValue == j) {
                        return;
                    }
                    j = intValue;
                }
            } catch (Throwable th) {
                com.mob.mobapm.d.a.a().i("APM: init error: " + th, new Object[0]);
            }
        } catch (Throwable th2) {
            com.mob.mobapm.d.a.a().d(th2);
        }
    }

    public void a(String str) {
        if (com.mob.mobapm.e.b.a(MobSDK.getContext())) {
            this.a = str;
            b(str);
            com.mob.mobapm.d.a.a().i("APM: init golden key: " + MobAPM.goldenKey + ", apmhuh: " + c + ", apmauh: " + d, new Object[0]);
            com.mob.mobapm.d.a.a().i("APM: init os: " + e + ", sc: " + g + ", cc: " + f + ", soc: " + h + ", dc: " + i, new Object[0]);
            if (MobAPM.goldenKey) {
                com.mob.mobapm.e.g.a();
            }
            if (e) {
                com.mob.mobapm.e.a.b().a();
                a.d().a();
                j.d().a();
                i.d().a();
                b.e().c();
            }
            if (f) {
                new com.mob.mobapm.core.m.a().a();
                g.b().a();
            }
            if (g) {
                new com.mob.mobapm.core.l.a().start();
                f.b().a();
            }
            if (h) {
                h.d().a();
            }
        }
    }
}
