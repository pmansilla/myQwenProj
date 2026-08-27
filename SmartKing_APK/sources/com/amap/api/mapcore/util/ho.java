package com.amap.api.mapcore.util;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

/* compiled from: SDKInfo.java */
@ih(a = "a")
/* loaded from: classes.dex */
public class ho {

    @ii(a = "a1", b = 6)
    private String a;

    @ii(a = "a2", b = 6)
    private String b;

    @ii(a = "a6", b = 2)
    private int c;

    @ii(a = "a3", b = 6)
    private String d;

    @ii(a = "a4", b = 6)
    private String e;

    @ii(a = "a5", b = 6)
    private String f;
    private String g;
    private String h;
    private String i;
    private String j;
    private String k;
    private String[] l;

    /* compiled from: SDKInfo.java */
    /* loaded from: classes.dex */
    public static class a {
        private String a;
        private String b;
        private String c;
        private String d;
        private boolean e = true;
        private String f = "standard";
        private String[] g = null;

        public a(String str, String str2, String str3) {
            this.a = str2;
            this.b = str2;
            this.d = str3;
            this.c = str;
        }

        public a a(String str) {
            this.b = str;
            return this;
        }

        public a a(String[] strArr) {
            if (strArr != null) {
                this.g = (String[]) strArr.clone();
            }
            return this;
        }

        public ho a() throws hc {
            if (this.g != null) {
                return new ho(this);
            }
            throw new hc("sdk packages is null");
        }
    }

    private ho() {
        this.c = 1;
        this.l = null;
    }

    private ho(a aVar) {
        this.c = 1;
        this.l = null;
        this.g = aVar.a;
        this.h = aVar.b;
        this.j = aVar.c;
        this.i = aVar.d;
        this.c = aVar.e ? 1 : 0;
        this.k = aVar.f;
        this.l = aVar.g;
        this.b = hp.b(this.h);
        this.a = hp.b(this.j);
        this.d = hp.b(this.i);
        this.e = hp.b(a(this.l));
        this.f = hp.b(this.k);
    }

    public static String a(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("a1", hp.b(str));
        return ig.a((Map<String, String>) hashMap);
    }

    private String a(String[] strArr) {
        if (strArr == null) {
            return null;
        }
        try {
            StringBuilder sb = new StringBuilder();
            for (String str : strArr) {
                sb.append(str);
                sb.append(";");
            }
            return sb.toString();
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    private String[] b(String str) {
        try {
            return str.split(";");
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public static String h() {
        return "a6=1";
    }

    public String a() {
        if (TextUtils.isEmpty(this.j) && !TextUtils.isEmpty(this.a)) {
            this.j = hp.c(this.a);
        }
        return this.j;
    }

    public void a(boolean z) {
        this.c = z ? 1 : 0;
    }

    public String b() {
        return this.g;
    }

    public String c() {
        if (TextUtils.isEmpty(this.h) && !TextUtils.isEmpty(this.b)) {
            this.h = hp.c(this.b);
        }
        return this.h;
    }

    public String d() {
        if (TextUtils.isEmpty(this.i) && !TextUtils.isEmpty(this.d)) {
            this.i = hp.c(this.d);
        }
        return this.i;
    }

    public String e() {
        if (TextUtils.isEmpty(this.k) && !TextUtils.isEmpty(this.f)) {
            this.k = hp.c(this.f);
        }
        if (TextUtils.isEmpty(this.k)) {
            this.k = "standard";
        }
        return this.k;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return getClass() == obj.getClass() && hashCode() == ((ho) obj).hashCode();
    }

    public boolean f() {
        return this.c == 1;
    }

    public String[] g() {
        if ((this.l == null || this.l.length == 0) && !TextUtils.isEmpty(this.e)) {
            this.l = b(hp.c(this.e));
        }
        return (String[]) this.l.clone();
    }

    public int hashCode() {
        hy hyVar = new hy();
        hyVar.a(this.j).a(this.g).a(this.h).a((Object[]) this.l);
        return hyVar.a();
    }
}
