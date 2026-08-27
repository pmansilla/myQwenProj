package com.loc;

import android.content.Context;
import com.amap.api.maps.AMap;
import com.amap.location.common.model.AmapLoc;
import io.reactivex.annotations.SchedulerSupport;
import java.util.HashMap;
import java.util.Locale;
import no.nordicsemi.android.dfu.DfuBaseService;

/* compiled from: LocNetManager.java */
/* loaded from: classes.dex */
public final class en {
    private static en b;
    bg a;
    private Context c;
    private int d = 0;
    private int e = es.f;
    private boolean f = false;
    private int g = 0;

    private en(Context context) {
        this.a = null;
        this.c = null;
        try {
            z.a().a(context);
        } catch (Throwable unused) {
        }
        this.c = context;
        this.a = bg.a();
    }

    public static en a(Context context) {
        if (b == null) {
            b = new en(context);
        }
        return b;
    }

    public final int a() {
        return this.d;
    }

    public final bk a(eo eoVar) throws Throwable {
        long c = fa.c();
        bk a = bg.a(eoVar, this.f || fa.k(this.c));
        this.d = Long.valueOf(fa.c() - c).intValue();
        return a;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public final eo a(Context context, byte[] bArr, String str, boolean z) {
        String str2;
        String str3;
        try {
            HashMap hashMap = new HashMap(16);
            eo eoVar = new eo(context, es.b());
            try {
                hashMap.put("Content-Type", DfuBaseService.MIME_TYPE_OCTET_STREAM);
                hashMap.put("Accept-Encoding", "gzip");
                hashMap.put("gzipped", AmapLoc.RESULT_TYPE_WIFI_ONLY);
                hashMap.put("Connection", "Keep-Alive");
                hashMap.put("User-Agent", "AMAP_Location_SDK_Android 4.7.1");
                hashMap.put("KEY", u.f(context));
                hashMap.put("enginever", "5.1");
                String a = w.a();
                String a2 = w.a(context, a, "key=" + u.f(context));
                hashMap.put("ts", a);
                hashMap.put("scode", a2);
                hashMap.put("encr", AmapLoc.RESULT_TYPE_WIFI_ONLY);
                eoVar.f = hashMap;
                String str4 = z ? "loc" : "locf";
                eoVar.m = true;
                eoVar.k = String.format(Locale.US, "platform=Android&sdkversion=%s&product=%s&loc_channel=%s", "4.7.1", str4, 3);
                eoVar.j = z;
                eoVar.g = str;
                eoVar.h = fa.a(bArr);
                eoVar.a(ab.a(context));
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
                eoVar.l = hashMap2;
                eoVar.a(this.e);
                eoVar.b(this.e);
                if ((!this.f && !fa.k(context)) || !str.startsWith("http:")) {
                    return eoVar;
                }
                eoVar.g = eoVar.c().replace("https:", "https:");
                return eoVar;
            } catch (Throwable unused) {
                return eoVar;
            }
        } catch (Throwable unused2) {
            return null;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public final String a(Context context, double d, double d2) {
        String str;
        String str2;
        byte[] b2;
        try {
            HashMap hashMap = new HashMap(16);
            eo eoVar = new eo(context, es.b());
            hashMap.clear();
            hashMap.put("Content-Type", "application/x-www-form-urlencoded");
            hashMap.put("Connection", "Keep-Alive");
            hashMap.put("User-Agent", "AMAP_Location_SDK_Android 4.7.1");
            HashMap hashMap2 = new HashMap(16);
            hashMap2.put(SchedulerSupport.CUSTOM, "26260A1F00020002");
            hashMap2.put("key", u.f(context));
            switch (this.g) {
                case 0:
                    hashMap2.remove("language");
                    break;
                case 1:
                    str = "language";
                    str2 = "zh-CN";
                    hashMap2.put(str, str2);
                    break;
                case 2:
                    str = "language";
                    str2 = AMap.ENGLISH;
                    hashMap2.put(str, str2);
                    break;
                default:
                    hashMap2.remove("language");
                    break;
            }
            String a = w.a();
            String a2 = w.a(context, a, ad.b(hashMap2));
            hashMap2.put("ts", a);
            hashMap2.put("scode", a2);
            eoVar.b(("output=json&radius=1000&extensions=all&location=" + d2 + "," + d).getBytes("UTF-8"));
            eoVar.m = false;
            eoVar.j = true;
            eoVar.k = String.format(Locale.US, "platform=Android&sdkversion=%s&product=%s&loc_channel=%s", "4.7.1", "loc", 3);
            eoVar.l = hashMap2;
            eoVar.f = hashMap;
            eoVar.a(ab.a(context));
            eoVar.a(es.f);
            eoVar.b(es.f);
            try {
                if (fa.k(context)) {
                    eoVar.g = "http://restapi.amap.com/v3/geocode/regeo".replace("http:", "https:");
                    b2 = bg.a(eoVar);
                } else {
                    eoVar.g = "http://restapi.amap.com/v3/geocode/regeo";
                    b2 = bg.b(eoVar);
                }
                return new String(b2, "utf-8");
            } catch (Throwable th) {
                es.a(th, "LocNetManager", "post");
                return null;
            }
        } catch (Throwable unused) {
        }
    }

    public final void a(long j, boolean z, int i) {
        try {
            this.f = z;
            try {
                z.a().a(z);
            } catch (Throwable unused) {
            }
            this.e = Long.valueOf(j).intValue();
            this.g = i;
        } catch (Throwable th) {
            es.a(th, "LocNetManager", "setOption");
        }
    }
}
