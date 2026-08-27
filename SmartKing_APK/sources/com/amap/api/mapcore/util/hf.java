package com.amap.api.mapcore.util;

import android.content.Context;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: AuthManager.java */
@Deprecated
/* loaded from: classes.dex */
public class hf {
    public static int a = -1;
    public static String b = "";
    private static ho c = null;
    private static String d = "http://apiinit.amap.com/v3/log/init";
    private static String e;

    private static String a() {
        return d;
    }

    private static Map<String, String> a(Context context) {
        HashMap hashMap = new HashMap();
        try {
            hashMap.put("resType", "json");
            hashMap.put("encode", "UTF-8");
            String a2 = hg.a();
            hashMap.put("ts", a2);
            hashMap.put("key", hd.f(context));
            hashMap.put("scode", hg.a(context, a2, hp.d("resType=json&encode=UTF-8&key=" + hd.f(context))));
        } catch (Throwable th) {
            hz.a(th, "Auth", "gParams");
        }
        return hashMap;
    }

    @Deprecated
    public static synchronized boolean a(Context context, ho hoVar) {
        boolean a2;
        synchronized (hf.class) {
            a2 = a(context, hoVar, false);
        }
        return a2;
    }

    private static boolean a(Context context, ho hoVar, boolean z) {
        c = hoVar;
        try {
            String a2 = a();
            HashMap hashMap = new HashMap();
            hashMap.put("Content-Type", "application/x-www-form-urlencoded");
            hashMap.put("Accept-Encoding", "gzip");
            hashMap.put("Connection", "Keep-Alive");
            hashMap.put("User-Agent", c.d());
            hashMap.put("X-INFO", hg.b(context));
            hashMap.put("logversion", "2.1");
            hashMap.put("platinfo", String.format("platform=Android&sdkversion=%s&product=%s", c.b(), c.a()));
            is a3 = is.a();
            hq hqVar = new hq();
            hqVar.setProxy(hm.a(context));
            hqVar.a(hashMap);
            hqVar.b(a(context));
            hqVar.a(a2);
            return a(a3.b(hqVar));
        } catch (Throwable th) {
            hz.a(th, "Auth", "getAuth");
            return true;
        }
    }

    private static boolean a(byte[] bArr) {
        if (bArr == null) {
            return true;
        }
        try {
            JSONObject jSONObject = new JSONObject(hp.a(bArr));
            if (jSONObject.has("status")) {
                int i = jSONObject.getInt("status");
                if (i == 1) {
                    a = 1;
                } else if (i == 0) {
                    a = 0;
                }
            }
            if (jSONObject.has("info")) {
                b = jSONObject.getString("info");
            }
            if (a == 0) {
                Log.i("AuthFailure", b);
            }
            return a == 1;
        } catch (JSONException e2) {
            hz.a(e2, "Auth", "lData");
            return false;
        } catch (Throwable th) {
            hz.a(th, "Auth", "lData");
            return false;
        }
    }
}
