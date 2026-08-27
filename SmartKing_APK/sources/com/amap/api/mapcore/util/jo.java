package com.amap.api.mapcore.util;

import android.content.Context;
import android.os.Build;
import java.io.ByteArrayOutputStream;

/* compiled from: StatisticsHeaderDataStrategy.java */
/* loaded from: classes.dex */
public class jo extends jq {
    public static int a = 13;
    public static int b = 6;
    private Context e;

    public jo(Context context, jq jqVar) {
        super(jqVar);
        this.e = context;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private byte[] a(Context context) {
        byte[] bArr;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr2 = new byte[0];
        try {
            try {
                hp.a(byteArrayOutputStream, "1.2." + a + "." + b);
                hp.a(byteArrayOutputStream, "Android");
                hp.a(byteArrayOutputStream, hi.u(context));
                hp.a(byteArrayOutputStream, hi.m(context));
                hp.a(byteArrayOutputStream, hi.h(context));
                hp.a(byteArrayOutputStream, Build.MANUFACTURER);
                hp.a(byteArrayOutputStream, Build.MODEL);
                hp.a(byteArrayOutputStream, Build.DEVICE);
                hp.a(byteArrayOutputStream, hi.w(context));
                hp.a(byteArrayOutputStream, hd.c(context));
                hp.a(byteArrayOutputStream, hd.d(context));
                hp.a(byteArrayOutputStream, hd.f(context));
                byteArrayOutputStream.write(new byte[]{0});
                bArr = byteArrayOutputStream.toByteArray();
                try {
                    byteArrayOutputStream.close();
                    byteArrayOutputStream = byteArrayOutputStream;
                } catch (Throwable th) {
                    th.printStackTrace();
                    byteArrayOutputStream = th;
                }
            } catch (Throwable th2) {
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable th3) {
                    th3.printStackTrace();
                }
                throw th2;
            }
        } catch (Throwable th4) {
            ic.c(th4, "sm", "gh");
            try {
                byteArrayOutputStream.close();
            } catch (Throwable th5) {
                th5.printStackTrace();
            }
            bArr = bArr2;
            byteArrayOutputStream = byteArrayOutputStream;
        }
        return bArr;
    }

    @Override // com.amap.api.mapcore.util.jq
    protected byte[] a(byte[] bArr) {
        byte[] a2 = a(this.e);
        byte[] bArr2 = new byte[a2.length + bArr.length];
        System.arraycopy(a2, 0, bArr2, 0, a2.length);
        System.arraycopy(bArr, 0, bArr2, a2.length, bArr.length);
        return bArr2;
    }
}
