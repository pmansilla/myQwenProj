package com.amap.api.mapcore.util;

import android.content.Context;
import android.text.TextUtils;
import com.amap.api.maps.AMapException;
import java.io.ByteArrayOutputStream;

/* compiled from: StatisticsEntity.java */
/* loaded from: classes.dex */
public class jh {
    private Context a;
    private String b;
    private String c;
    private String d;
    private String e;

    public jh(Context context, String str, String str2, String str3) throws hc {
        if (TextUtils.isEmpty(str3) || str3.length() > 256) {
            throw new hc(AMapException.ERROR_INVALID_PARAMETER);
        }
        this.a = context.getApplicationContext();
        this.c = str;
        this.d = str2;
        this.b = str3;
    }

    public void a(String str) throws hc {
        if (TextUtils.isEmpty(str) || str.length() > 65536) {
            throw new hc(AMapException.ERROR_INVALID_PARAMETER);
        }
        this.e = str;
    }

    public byte[] a() {
        ByteArrayOutputStream byteArrayOutputStream;
        int i = 0;
        byte[] bArr = new byte[0];
        ByteArrayOutputStream byteArrayOutputStream2 = null;
        try {
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
            } catch (Throwable th) {
                th = th;
            }
            try {
                try {
                    hp.a(byteArrayOutputStream, this.c);
                    hp.a(byteArrayOutputStream, this.d);
                    hp.a(byteArrayOutputStream, this.b);
                    hp.a(byteArrayOutputStream, String.valueOf(hi.q(this.a)));
                    try {
                        i = (int) (System.currentTimeMillis() / 1000);
                    } catch (Throwable unused) {
                    }
                    byteArrayOutputStream.write(a(i));
                    byteArrayOutputStream.write(b(this.e));
                    byteArrayOutputStream.write(hp.a(this.e));
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    try {
                        byteArrayOutputStream.close();
                        return byteArray;
                    } catch (Throwable th2) {
                        th2.printStackTrace();
                        return byteArray;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (byteArrayOutputStream != null) {
                        try {
                            byteArrayOutputStream.close();
                        } catch (Throwable th4) {
                            th4.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Throwable th5) {
                th = th5;
                byteArrayOutputStream2 = byteArrayOutputStream;
                ic.c(th, "se", "tds");
                if (byteArrayOutputStream2 != null) {
                    try {
                        byteArrayOutputStream2.close();
                    } catch (Throwable th6) {
                        th6.printStackTrace();
                    }
                }
                return bArr;
            }
        } catch (Throwable th7) {
            th = th7;
            byteArrayOutputStream = byteArrayOutputStream2;
        }
    }

    public byte[] a(int i) {
        return new byte[]{(byte) ((i >> 24) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 8) & 255), (byte) (i & 255)};
    }

    public byte[] b(String str) {
        byte[] a;
        if (!TextUtils.isEmpty(str) && (a = hp.a(this.e)) != null) {
            int length = a.length;
            return new byte[]{(byte) (length / 256), (byte) (length % 256)};
        }
        return new byte[]{0, 0};
    }
}
