package com.amap.api.mapcore.util;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

/* compiled from: HttpsDecisionUtil.java */
/* loaded from: classes.dex */
public class hk {
    private volatile b a = new b();
    private im b = new im("HttpsDecisionUtil");

    /* compiled from: HttpsDecisionUtil.java */
    /* loaded from: classes.dex */
    private static class a {
        static hk a = new hk();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: HttpsDecisionUtil.java */
    /* loaded from: classes.dex */
    public static class b {
        protected boolean a;
        private int b;
        private final boolean c;
        private boolean d;

        private b() {
            this.b = 0;
            this.a = true;
            this.c = true;
            this.d = false;
        }

        private int b() {
            if (this.b <= 0) {
                return 28;
            }
            return this.b;
        }

        private boolean c() {
            return b() >= 28;
        }

        private boolean d() {
            return Build.VERSION.SDK_INT >= 28;
        }

        private boolean e() {
            return d() && (!this.a || c());
        }

        public void a(Context context) {
            if (context != null && this.b <= 0 && Build.VERSION.SDK_INT >= 4) {
                this.b = context.getApplicationContext().getApplicationInfo().targetSdkVersion;
            }
        }

        public void a(boolean z) {
            this.a = z;
        }

        public boolean a() {
            return this.d || e();
        }

        public void b(boolean z) {
            this.d = z;
        }
    }

    public static hk a() {
        return a.a;
    }

    public static String a(String str) {
        if (TextUtils.isEmpty(str) || str.startsWith("https")) {
            return str;
        }
        try {
            Uri.Builder buildUpon = Uri.parse(str).buildUpon();
            buildUpon.scheme("https");
            return buildUpon.build().toString();
        } catch (Throwable unused) {
            return str;
        }
    }

    private void b(Context context, boolean z) {
        this.b.a(context, "isTargetRequired", z);
    }

    public static boolean c() {
        return Build.VERSION.SDK_INT == 19;
    }

    private boolean c(Context context) {
        return this.b.b(context, "isTargetRequired", true);
    }

    private void d(Context context) {
        this.b.a(context, "isTargetRequired", true);
    }

    public void a(Context context) {
        if (this.a == null) {
            this.a = new b();
        }
        this.a.a(c(context));
        this.a.a(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Context context, boolean z) {
        if (this.a == null) {
            this.a = new b();
        }
        b(context, z);
        this.a.a(z);
    }

    public void a(boolean z) {
        if (this.a == null) {
            this.a = new b();
        }
        this.a.b(z);
    }

    public void b(Context context) {
        d(context);
    }

    public boolean b() {
        if (this.a == null) {
            this.a = new b();
        }
        return this.a.a();
    }

    public boolean b(boolean z) {
        if (c()) {
            return false;
        }
        return z || b();
    }
}
