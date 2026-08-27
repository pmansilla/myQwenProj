package com.loc;

import android.content.Context;
import android.telephony.TelephonyManager;
import java.util.Random;

/* compiled from: PhoneInfoUtils.java */
/* loaded from: classes.dex */
public final class dv {
    private static String a() {
        int currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        int nanoTime = (int) System.nanoTime();
        int nextInt = new Random().nextInt();
        int nextInt2 = new Random().nextInt();
        byte[] a = du.a(currentTimeMillis);
        byte[] a2 = du.a(nanoTime);
        byte[] a3 = du.a(nextInt);
        byte[] a4 = du.a(nextInt2);
        byte[] bArr = new byte[16];
        System.arraycopy(a, 0, bArr, 0, 4);
        System.arraycopy(a2, 0, bArr, 4, 4);
        System.arraycopy(a3, 0, bArr, 8, 4);
        System.arraycopy(a4, 0, bArr, 12, 4);
        return dt.a(bArr, 2);
    }

    public static String a(Context context) {
        String str = null;
        if (context != null) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
                if (telephonyManager != null) {
                    str = telephonyManager.getDeviceId();
                }
            } catch (Exception unused) {
            }
        }
        return dw.a(str) ? a() : str;
    }

    public static String b(Context context) {
        String str = null;
        if (context != null) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
                if (telephonyManager != null) {
                    str = telephonyManager.getSubscriberId();
                }
            } catch (Exception unused) {
            }
        }
        return dw.a(str) ? a() : str;
    }
}
