package com.loc;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.sun.mail.imap.IMAPStore;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import kotlinx.coroutines.DebugKt;
import me.panpf.sketch.uri.FileUriModel;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: AuthConfigManager.java */
/* loaded from: classes.dex */
public final class v {
    public static int a = -1;
    public static String b = "";

    /* compiled from: AuthConfigManager.java */
    /* loaded from: classes.dex */
    public static class a {

        @Deprecated
        public c A;
        public c B;
        public b C;
        public b D;
        public b E;
        public b F;
        public e G;
        private boolean H;
        public String a;
        public int b = -1;

        @Deprecated
        public JSONObject c;

        @Deprecated
        public JSONObject d;

        @Deprecated
        public JSONObject e;

        @Deprecated
        public JSONObject f;

        @Deprecated
        public JSONObject g;

        @Deprecated
        public JSONObject h;

        @Deprecated
        public JSONObject i;

        @Deprecated
        public JSONObject j;

        @Deprecated
        public JSONObject k;

        @Deprecated
        public JSONObject l;

        @Deprecated
        public JSONObject m;

        @Deprecated
        public JSONObject n;

        @Deprecated
        public JSONObject o;

        @Deprecated
        public JSONObject p;

        @Deprecated
        public JSONObject q;

        @Deprecated
        public JSONObject r;

        @Deprecated
        public JSONObject s;

        @Deprecated
        public JSONObject t;

        @Deprecated
        public JSONObject u;

        @Deprecated
        public JSONObject v;
        public JSONObject w;
        public C0049a x;
        public d y;
        public f z;

        /* compiled from: AuthConfigManager.java */
        /* renamed from: com.loc.v$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        public static class C0049a {
            public boolean a;
            public boolean b;
            public JSONObject c;
        }

        /* compiled from: AuthConfigManager.java */
        /* loaded from: classes.dex */
        public static class b {
            public boolean a;
            public String b;
            public String c;
            public String d;
            public boolean e;
        }

        /* compiled from: AuthConfigManager.java */
        /* loaded from: classes.dex */
        public static class c {
            public String a;
            public String b;
        }

        /* compiled from: AuthConfigManager.java */
        /* loaded from: classes.dex */
        public static class d {
            public String a;
            public String b;
            public String c;
        }

        /* compiled from: AuthConfigManager.java */
        /* loaded from: classes.dex */
        public static class e {
            public boolean a;
            public boolean b;
            public boolean c;
            public String d;
            public String e;
            public String f;
        }

        /* compiled from: AuthConfigManager.java */
        /* loaded from: classes.dex */
        public static class f {
            public boolean a;
        }
    }

    /* compiled from: AuthConfigManager.java */
    /* loaded from: classes.dex */
    static class b extends bh {
        private String f;
        private Map<String, String> g;
        private boolean h;

        b(Context context, ac acVar, String str) {
            super(context, acVar);
            this.f = str;
            this.g = null;
            this.h = Build.VERSION.SDK_INT != 19;
        }

        public final boolean a() {
            return this.h;
        }

        @Override // com.loc.bh
        public final byte[] a_() {
            return null;
        }

        @Override // com.loc.bj
        public final Map<String, String> b() {
            return null;
        }

        @Override // com.loc.bj
        public final String c() {
            return this.h ? "https://restapi.amap.com/v3/iasdkauth" : "http://restapi.amap.com/v3/iasdkauth";
        }

        @Override // com.loc.bh
        public final byte[] e() {
            String u = x.u(this.a);
            if (TextUtils.isEmpty(u)) {
                u = x.h(this.a);
            }
            if (!TextUtils.isEmpty(u)) {
                u = aa.b(new StringBuilder(u).reverse().toString());
            }
            HashMap hashMap = new HashMap();
            hashMap.put("authkey", this.f);
            hashMap.put("plattype", "android");
            hashMap.put("product", this.b.a());
            hashMap.put(IMAPStore.ID_VERSION, this.b.b());
            hashMap.put("output", "json");
            StringBuilder sb = new StringBuilder();
            sb.append(Build.VERSION.SDK_INT);
            hashMap.put("androidversion", sb.toString());
            hashMap.put("deviceId", u);
            hashMap.put("manufacture", Build.MANUFACTURER);
            if (this.g != null && !this.g.isEmpty()) {
                hashMap.putAll(this.g);
            }
            hashMap.put("abitype", ad.a(this.a));
            hashMap.put("ext", this.b.d());
            return ad.a(ad.a(hashMap));
        }

        @Override // com.loc.bh
        protected final String f() {
            return "3.0";
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x00b7 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x00b8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.loc.v.a a(android.content.Context r11, com.loc.ac r12, java.lang.String r13) {
        /*
            Method dump skipped, instructions count: 763
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.v.a(android.content.Context, com.loc.ac, java.lang.String):com.loc.v$a");
    }

    private static String a(JSONObject jSONObject, String str) throws JSONException {
        return (jSONObject == null || !jSONObject.has(str) || jSONObject.getString(str).equals("[]")) ? "" : jSONObject.optString(str);
    }

    public static void a(Context context, String str) {
        u.a(context, str);
    }

    private static void a(a aVar, JSONObject jSONObject) {
        try {
            if (ad.a(jSONObject, "11B")) {
                aVar.h = jSONObject.getJSONObject("11B");
            }
            if (ad.a(jSONObject, "11C")) {
                aVar.k = jSONObject.getJSONObject("11C");
            }
            if (ad.a(jSONObject, "11I")) {
                aVar.l = jSONObject.getJSONObject("11I");
            }
            if (ad.a(jSONObject, "11H")) {
                aVar.m = jSONObject.getJSONObject("11H");
            }
            if (ad.a(jSONObject, "11E")) {
                aVar.n = jSONObject.getJSONObject("11E");
            }
            if (ad.a(jSONObject, "11F")) {
                aVar.o = jSONObject.getJSONObject("11F");
            }
            if (ad.a(jSONObject, "13A")) {
                aVar.q = jSONObject.getJSONObject("13A");
            }
            if (ad.a(jSONObject, "13J")) {
                aVar.i = jSONObject.getJSONObject("13J");
            }
            if (ad.a(jSONObject, "11G")) {
                aVar.p = jSONObject.getJSONObject("11G");
            }
            if (ad.a(jSONObject, "006")) {
                aVar.r = jSONObject.getJSONObject("006");
            }
            if (ad.a(jSONObject, "010")) {
                aVar.s = jSONObject.getJSONObject("010");
            }
            if (ad.a(jSONObject, "11Z")) {
                JSONObject jSONObject2 = jSONObject.getJSONObject("11Z");
                a.b bVar = new a.b();
                a(jSONObject2, bVar);
                aVar.C = bVar;
            }
            if (ad.a(jSONObject, "135")) {
                aVar.j = jSONObject.getJSONObject("135");
            }
            if (ad.a(jSONObject, "13S")) {
                aVar.g = jSONObject.getJSONObject("13S");
            }
            if (ad.a(jSONObject, "121")) {
                JSONObject jSONObject3 = jSONObject.getJSONObject("121");
                a.b bVar2 = new a.b();
                a(jSONObject3, bVar2);
                aVar.D = bVar2;
            }
            if (ad.a(jSONObject, "122")) {
                JSONObject jSONObject4 = jSONObject.getJSONObject("122");
                a.b bVar3 = new a.b();
                a(jSONObject4, bVar3);
                aVar.E = bVar3;
            }
            if (ad.a(jSONObject, "123")) {
                JSONObject jSONObject5 = jSONObject.getJSONObject("123");
                a.b bVar4 = new a.b();
                a(jSONObject5, bVar4);
                aVar.F = bVar4;
            }
            if (ad.a(jSONObject, "011")) {
                aVar.c = jSONObject.getJSONObject("011");
            }
            if (ad.a(jSONObject, "012")) {
                aVar.d = jSONObject.getJSONObject("012");
            }
            if (ad.a(jSONObject, "013")) {
                aVar.e = jSONObject.getJSONObject("013");
            }
            if (ad.a(jSONObject, "014")) {
                aVar.f = jSONObject.getJSONObject("014");
            }
            if (ad.a(jSONObject, "145")) {
                aVar.t = jSONObject.getJSONObject("145");
            }
            if (ad.a(jSONObject, "14B")) {
                aVar.u = jSONObject.getJSONObject("14B");
            }
            if (ad.a(jSONObject, "14D")) {
                aVar.v = jSONObject.getJSONObject("14D");
            }
        } catch (Throwable th) {
            aq.b(th, "at", "pe");
        }
    }

    private static void a(JSONObject jSONObject, a.b bVar) {
        try {
            String a2 = a(jSONObject, "m");
            String a3 = a(jSONObject, "u");
            String a4 = a(jSONObject, "v");
            String a5 = a(jSONObject, "able");
            String a6 = a(jSONObject, DebugKt.DEBUG_PROPERTY_VALUE_ON);
            bVar.c = a2;
            bVar.b = a3;
            bVar.d = a4;
            bVar.a = a(a5, false);
            bVar.e = a(a6, true);
        } catch (Throwable th) {
            an.a(th, "at", "pe");
        }
    }

    private static void a(JSONObject jSONObject, a.c cVar) {
        if (jSONObject != null) {
            try {
                String a2 = a(jSONObject, "md5");
                String a3 = a(jSONObject, FileDownloadModel.URL);
                cVar.b = a2;
                cVar.a = a3;
            } catch (Throwable th) {
                an.a(th, "at", "psc");
            }
        }
    }

    public static boolean a(String str, boolean z) {
        try {
            if (TextUtils.isEmpty(str)) {
                return z;
            }
            String[] split = URLDecoder.decode(str).split(FileUriModel.SCHEME);
            return split[split.length - 1].charAt(4) % 2 == 1;
        } catch (Throwable unused) {
            return z;
        }
    }
}
