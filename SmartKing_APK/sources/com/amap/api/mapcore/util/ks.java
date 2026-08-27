package com.amap.api.mapcore.util;

import android.content.Context;
import com.amap.location.common.model.AmapLoc;
import io.reactivex.annotations.SchedulerSupport;
import java.util.HashMap;
import java.util.Locale;
import no.nordicsemi.android.dfu.DfuBaseService;

/* compiled from: LocNetManager.java */
/* loaded from: classes.dex */
public final class ks {
    private static ks b;
    is a;
    private Context c;
    private int d = 0;
    private int e = kw.f;
    private boolean f = false;
    private int g = 0;

    private ks(Context context) {
        this.a = null;
        this.c = null;
        try {
            hk.a().a(context);
        } catch (Throwable unused) {
        }
        this.c = context;
        this.a = is.a();
    }

    public static ks a(Context context) {
        if (b == null) {
            b = new ks(context);
        }
        return b;
    }

    public final iz a(kt ktVar) throws Throwable {
        long b2 = la.b();
        iz a = this.a.a(ktVar, this.f || la.e(this.c));
        this.d = Long.valueOf(la.b() - b2).intValue();
        return a;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public final kt a(Context context, byte[] bArr, String str) {
        String str2;
        String str3;
        try {
            HashMap hashMap = new HashMap(16);
            kt ktVar = new kt(context, kw.b());
            try {
                hashMap.put("Content-Type", DfuBaseService.MIME_TYPE_OCTET_STREAM);
                hashMap.put("Accept-Encoding", "gzip");
                hashMap.put("gzipped", AmapLoc.RESULT_TYPE_WIFI_ONLY);
                hashMap.put("Connection", "Keep-Alive");
                hashMap.put("User-Agent", "AMAP_Location_SDK_Android 4.7.1");
                hashMap.put("KEY", hd.f(context));
                hashMap.put("enginever", "5.1");
                String a = hg.a();
                String a2 = hg.a(context, a, "key=" + hd.f(context));
                hashMap.put("ts", a);
                hashMap.put("scode", a2);
                hashMap.put("encr", AmapLoc.RESULT_TYPE_WIFI_ONLY);
                ktVar.b(hashMap);
                ktVar.k();
                ktVar.a(String.format(Locale.US, "platform=Android&sdkversion=%s&product=%s&loc_channel=%s", "4.7.1", "loc", 3));
                ktVar.a();
                ktVar.b(str);
                ktVar.b(la.a(bArr));
                ktVar.setProxy(hm.a(context));
                HashMap hashMap2 = new HashMap(16);
                hashMap2.put("output", "bin");
                hashMap2.put("policy", "3103");
                switch (this.g) {
                    case 0:
                        hashMap2.remove(SchedulerSupport.CUSTOM);
                        break;
                    case 1:
                        str2 = SchedulerSupport.CUSTOM;
                        str3 = "language:cn";
                        hashMap2.put(str2, str3);
                        break;
                    case 2:
                        str2 = SchedulerSupport.CUSTOM;
                        str3 = "language:en";
                        hashMap2.put(str2, str3);
                        break;
                    default:
                        hashMap2.remove(SchedulerSupport.CUSTOM);
                        break;
                }
                ktVar.a(hashMap2);
                ktVar.setConnectionTimeout(this.e);
                ktVar.setSoTimeout(this.e);
                if ((!this.f && !la.e(context)) || !str.startsWith("http:")) {
                    return ktVar;
                }
                ktVar.b(ktVar.getURL().replace("https:", "https:"));
                return ktVar;
            } catch (Throwable unused) {
                return ktVar;
            }
        } catch (Throwable unused2) {
            return null;
        }
    }

    public final void a(long j, boolean z) {
        try {
            this.f = z;
            try {
                hk.a().a(z);
            } catch (Throwable unused) {
            }
            this.e = Long.valueOf(j).intValue();
            this.g = 0;
        } catch (Throwable th) {
            kw.a(th, "LocNetManager", "setOption");
        }
    }
}
