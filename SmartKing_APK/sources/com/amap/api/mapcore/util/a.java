package com.amap.api.mapcore.util;

import android.content.Context;
import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import com.autonavi.amap.mapcore.AeUtil;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: NetReuestParam.java */
/* loaded from: classes.dex */
public class a {
    private static boolean a = false;

    public static String a(Context context) {
        c a2 = c.a(context);
        if (a2.a()) {
            return a2.b();
        }
        return null;
    }

    public static String a(String str, String str2, String str3, String str4) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("tid", str);
            jSONObject.put("diu", str2);
            jSONObject.put("diu2", str3);
            jSONObject.put("diu3", str4);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        String jSONObject2 = jSONObject.toString();
        if (TextUtils.isEmpty(jSONObject2)) {
            return null;
        }
        String a2 = i.a();
        if (!TextUtils.isEmpty(a2)) {
            String a3 = f.a(b.a((jSONObject2 + "\u0000").getBytes(), a2.getBytes()));
            if (!TextUtils.isEmpty(a3)) {
                try {
                    return "key=" + URLEncoder.encode(f.a(h.a(a2.getBytes("utf-8"), h.a("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDUi5cTlCSDfczffuYb2UyvrKXuW/7xqBhLYG1ro+PmCNdJ01U1o7uc18YP6VNl5ZBF1IadY/XC6JphzBzCARVOqk1OE/Qfb1dQF6tO2nEZmDVDFeMHXsDtM1Jic/ntBcAy7Y6GP3OyqPRLgUribU7+m4CmAtk8b5y117cyWMBsXwIDAQAB")))) + "&data=" + URLEncoder.encode(a3);
                } catch (Throwable th2) {
                    th2.printStackTrace();
                }
            }
        }
        return null;
    }

    public static synchronized Map<String, String> a() {
        synchronized (a.class) {
            if (a) {
                return null;
            }
            a = true;
            HashMap hashMap = new HashMap();
            hashMap.put("ent", AmapLoc.RESULT_TYPE_FUSED);
            StringBuilder sb = new StringBuilder();
            sb.append("channel=");
            sb.append("lbs_sdk");
            sb.append("&div=");
            sb.append("ANDSDK10");
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("lbs_sdk");
            stringBuffer.append("ANDSDK10");
            stringBuffer.append("@");
            stringBuffer.append("Z2naXvxC0K5f6IEJhtQzUolm9S8yOTkq");
            String a2 = i.a(stringBuffer.toString());
            sb.append("&sign=");
            sb.append(a2.toUpperCase(Locale.US));
            sb.append("&output=json\u0000");
            hashMap.put("in", f.a(b.a(sb.toString().getBytes(), "DVGeEYOpNkFbzfQA".getBytes())));
            hashMap.put("keyt", "100");
            return hashMap;
        }
    }

    public static void a(Context context, String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.optInt("code") == 1) {
                String optString = new JSONObject(jSONObject.optString(AeUtil.ROOT_DATA_PATH_OLD_NAME)).optString("adiu");
                if (TextUtils.isEmpty(optString)) {
                    return;
                }
                g.a(optString);
                c.a(context).a(optString);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String b() {
        return g.b();
    }
}
