package com.amap.api.mapcore.util;

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
public class he {
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
        public f G;
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
        public C0023a x;
        public d y;
        public e z;

        /* compiled from: AuthConfigManager.java */
        /* renamed from: com.amap.api.mapcore.util.he$a$a, reason: collision with other inner class name */
        /* loaded from: classes.dex */
        public static class C0023a {
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
        }

        /* compiled from: AuthConfigManager.java */
        /* loaded from: classes.dex */
        public static class f {
            public boolean a;
            public boolean b;
            public boolean c;
            public String d;
            public String e;
            public String f;
        }

        public boolean a() {
            return this.H;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AuthConfigManager.java */
    /* loaded from: classes.dex */
    public static class b extends it {
        private String f;
        private Map<String, String> g;
        private boolean h;

        b(Context context, ho hoVar, String str, Map<String, String> map) {
            super(context, hoVar);
            this.f = str;
            this.g = map;
            this.h = Build.VERSION.SDK_INT != 19;
        }

        private Map<String, String> k() {
            String u = hi.u(this.d);
            if (TextUtils.isEmpty(u)) {
                u = hi.i(this.d);
            }
            if (!TextUtils.isEmpty(u)) {
                u = hl.b(new StringBuilder(u).reverse().toString());
            }
            HashMap hashMap = new HashMap();
            hashMap.put("authkey", this.f);
            hashMap.put("plattype", "android");
            hashMap.put("product", this.e.a());
            hashMap.put(IMAPStore.ID_VERSION, this.e.b());
            hashMap.put("output", "json");
            hashMap.put("androidversion", Build.VERSION.SDK_INT + "");
            hashMap.put("deviceId", u);
            hashMap.put("manufacture", Build.MANUFACTURER);
            if (this.g != null && !this.g.isEmpty()) {
                hashMap.putAll(this.g);
            }
            hashMap.put("abitype", hp.a(this.d));
            hashMap.put("ext", this.e.e());
            return hashMap;
        }

        public boolean a() {
            return this.h;
        }

        @Override // com.amap.api.mapcore.util.it
        public byte[] d() {
            return null;
        }

        @Override // com.amap.api.mapcore.util.it
        public byte[] e() {
            return hp.a(hp.b(k()));
        }

        @Override // com.amap.api.mapcore.util.it
        protected String f() {
            return "3.0";
        }

        @Override // com.amap.api.mapcore.util.ix
        public Map<String, String> getRequestHead() {
            return null;
        }

        @Override // com.amap.api.mapcore.util.ix
        public String getURL() {
            return this.h ? "https://restapi.amap.com/v3/iasdkauth" : "http://restapi.amap.com/v3/iasdkauth";
        }
    }

    public static a a(Context context, ho hoVar, String str, Map<String, String> map) {
        return a(context, hoVar, str, map, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x00c7 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x00c8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.amap.api.mapcore.util.he.a a(android.content.Context r8, com.amap.api.mapcore.util.ho r9, java.lang.String r10, java.util.Map<java.lang.String, java.lang.String> r11, boolean r12) {
        /*
            Method dump skipped, instructions count: 610
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.api.mapcore.util.he.a(android.content.Context, com.amap.api.mapcore.util.ho, java.lang.String, java.util.Map, boolean):com.amap.api.mapcore.util.he$a");
    }

    public static String a(JSONObject jSONObject, String str) throws JSONException {
        return (jSONObject == null || !jSONObject.has(str) || jSONObject.getString(str).equals("[]")) ? "" : jSONObject.optString(str);
    }

    public static void a(Context context, String str) {
        hd.a(context, str);
    }

    private static void a(Context context, JSONObject jSONObject) {
        try {
            JSONObject jSONObject2 = jSONObject.getJSONObject("15K");
            boolean a2 = a(jSONObject2.optString("isTargetAble"), false);
            if (a(jSONObject2.optString("able"), false)) {
                hk.a().a(context, a2);
            } else {
                hk.a().b(context);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private static void a(a aVar, JSONObject jSONObject) {
        try {
            if (hp.a(jSONObject, "11B")) {
                aVar.h = jSONObject.getJSONObject("11B");
            }
            if (hp.a(jSONObject, "11C")) {
                aVar.k = jSONObject.getJSONObject("11C");
            }
            if (hp.a(jSONObject, "11I")) {
                aVar.l = jSONObject.getJSONObject("11I");
            }
            if (hp.a(jSONObject, "11H")) {
                aVar.m = jSONObject.getJSONObject("11H");
            }
            if (hp.a(jSONObject, "11E")) {
                aVar.n = jSONObject.getJSONObject("11E");
            }
            if (hp.a(jSONObject, "11F")) {
                aVar.o = jSONObject.getJSONObject("11F");
            }
            if (hp.a(jSONObject, "13A")) {
                aVar.q = jSONObject.getJSONObject("13A");
            }
            if (hp.a(jSONObject, "13J")) {
                aVar.i = jSONObject.getJSONObject("13J");
            }
            if (hp.a(jSONObject, "11G")) {
                aVar.p = jSONObject.getJSONObject("11G");
            }
            if (hp.a(jSONObject, "006")) {
                aVar.r = jSONObject.getJSONObject("006");
            }
            if (hp.a(jSONObject, "010")) {
                aVar.s = jSONObject.getJSONObject("010");
            }
            if (hp.a(jSONObject, "11Z")) {
                JSONObject jSONObject2 = jSONObject.getJSONObject("11Z");
                a.b bVar = new a.b();
                a(jSONObject2, bVar);
                aVar.C = bVar;
            }
            if (hp.a(jSONObject, "135")) {
                aVar.j = jSONObject.getJSONObject("135");
            }
            if (hp.a(jSONObject, "13S")) {
                aVar.g = jSONObject.getJSONObject("13S");
            }
            if (hp.a(jSONObject, "121")) {
                JSONObject jSONObject3 = jSONObject.getJSONObject("121");
                a.b bVar2 = new a.b();
                a(jSONObject3, bVar2);
                aVar.D = bVar2;
            }
            if (hp.a(jSONObject, "122")) {
                JSONObject jSONObject4 = jSONObject.getJSONObject("122");
                a.b bVar3 = new a.b();
                a(jSONObject4, bVar3);
                aVar.E = bVar3;
            }
            if (hp.a(jSONObject, "123")) {
                JSONObject jSONObject5 = jSONObject.getJSONObject("123");
                a.b bVar4 = new a.b();
                a(jSONObject5, bVar4);
                aVar.F = bVar4;
            }
            if (hp.a(jSONObject, "011")) {
                aVar.c = jSONObject.getJSONObject("011");
            }
            if (hp.a(jSONObject, "012")) {
                aVar.d = jSONObject.getJSONObject("012");
            }
            if (hp.a(jSONObject, "013")) {
                aVar.e = jSONObject.getJSONObject("013");
            }
            if (hp.a(jSONObject, "014")) {
                aVar.f = jSONObject.getJSONObject("014");
            }
            if (hp.a(jSONObject, "145")) {
                aVar.t = jSONObject.getJSONObject("145");
            }
            if (hp.a(jSONObject, "14B")) {
                aVar.u = jSONObject.getJSONObject("14B");
            }
            if (hp.a(jSONObject, "14D")) {
                aVar.v = jSONObject.getJSONObject("14D");
            }
        } catch (Throwable th) {
            ic.c(th, "at", "pe");
        }
    }

    private static void a(JSONObject jSONObject, a.b bVar) {
        if (bVar != null) {
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
                hz.a(th, "at", "pe");
            }
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
                hz.a(th, "at", "psc");
            }
        }
    }

    private static void a(JSONObject jSONObject, a.d dVar) {
        if (jSONObject != null) {
            try {
                String a2 = a(jSONObject, "md5");
                String a3 = a(jSONObject, FileDownloadModel.URL);
                String a4 = a(jSONObject, "sdkversion");
                if (!TextUtils.isEmpty(a2) && !TextUtils.isEmpty(a3) && !TextUtils.isEmpty(a4)) {
                    dVar.a = a3;
                    dVar.b = a2;
                    dVar.c = a4;
                }
            } catch (Throwable th) {
                hz.a(th, "at", "psu");
            }
        }
    }

    private static void a(JSONObject jSONObject, a.e eVar) {
        if (eVar == null || jSONObject == null) {
            return;
        }
        eVar.a = a(jSONObject.optString("able"), false);
    }

    private static void a(JSONObject jSONObject, a.f fVar) {
        if (fVar != null) {
            try {
                String a2 = a(jSONObject, "md5");
                String a3 = a(jSONObject, "md5info");
                String a4 = a(jSONObject, FileDownloadModel.URL);
                String a5 = a(jSONObject, "able");
                String a6 = a(jSONObject, DebugKt.DEBUG_PROPERTY_VALUE_ON);
                String a7 = a(jSONObject, "mobileable");
                fVar.e = a2;
                fVar.f = a3;
                fVar.d = a4;
                fVar.a = a(a5, false);
                fVar.b = a(a6, false);
                fVar.c = a(a7, false);
            } catch (Throwable th) {
                hz.a(th, "at", "pes");
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
