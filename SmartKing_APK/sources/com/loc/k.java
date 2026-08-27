package com.loc;

import android.content.Context;
import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import java.util.HashMap;
import java.util.Map;

/* compiled from: GeoFenceNetManager.java */
/* loaded from: classes.dex */
public final class k {
    bg a;

    public k(Context context) {
        this.a = null;
        try {
            z.a().a(context);
        } catch (Throwable unused) {
        }
        this.a = bg.a();
    }

    public static String a(Context context, String str, String str2) {
        Map<String, String> b = b(context, str2, null, null, null, null, null, null);
        b.put("extensions", "all");
        return a(context, str, b);
    }

    public static String a(Context context, String str, String str2, String str3, String str4, String str5) {
        Map<String, String> b = b(context, str2, str3, str4, str5, null, null, null);
        b.put("children", AmapLoc.RESULT_TYPE_WIFI_ONLY);
        b.put("page", AmapLoc.RESULT_TYPE_WIFI_ONLY);
        b.put("extensions", "base");
        return a(context, str, b);
    }

    public static String a(Context context, String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        Map<String, String> b = b(context, str2, str3, null, str4, str5, str6, str7);
        b.put("children", AmapLoc.RESULT_TYPE_WIFI_ONLY);
        b.put("page", AmapLoc.RESULT_TYPE_WIFI_ONLY);
        b.put("extensions", "base");
        return a(context, str, b);
    }

    private static String a(Context context, String str, Map<String, String> map) {
        byte[] b;
        try {
            HashMap hashMap = new HashMap(16);
            em emVar = new em();
            hashMap.clear();
            hashMap.put("Content-Type", "application/x-www-form-urlencoded");
            hashMap.put("Connection", "Keep-Alive");
            hashMap.put("User-Agent", "AMAP_Location_SDK_Android 4.7.1");
            String a = w.a();
            String a2 = w.a(context, a, ad.b(map));
            map.put("ts", a);
            map.put("scode", a2);
            emVar.b(map);
            emVar.a(hashMap);
            emVar.a(str);
            emVar.a(ab.a(context));
            emVar.a(es.f);
            emVar.b(es.f);
            try {
                if (fa.k(context)) {
                    emVar.a(str.replace("http:", "https:"));
                    b = bg.a(emVar);
                } else {
                    b = bg.b(emVar);
                }
                return new String(b, "utf-8");
            } catch (Throwable th) {
                es.a(th, "GeoFenceNetManager", "post");
                return null;
            }
        } catch (Throwable unused) {
        }
    }

    private static Map<String, String> b(Context context, String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        HashMap hashMap = new HashMap(16);
        hashMap.put("key", u.f(context));
        if (!TextUtils.isEmpty(str)) {
            hashMap.put("keywords", str);
        }
        if (!TextUtils.isEmpty(str2)) {
            hashMap.put("types", str2);
        }
        if (!TextUtils.isEmpty(str5) && !TextUtils.isEmpty(str6)) {
            hashMap.put("location", str6 + "," + str5);
        }
        if (!TextUtils.isEmpty(str3)) {
            hashMap.put("city", str3);
        }
        if (!TextUtils.isEmpty(str4)) {
            hashMap.put("offset", str4);
        }
        if (!TextUtils.isEmpty(str7)) {
            hashMap.put("radius", str7);
        }
        return hashMap;
    }
}
