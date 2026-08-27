package com.loc;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

/* compiled from: HttpsDecisionUtil.java */
/* loaded from: classes.dex */
public final class z {
    private volatile b a = new b(0);
    private bb b = new bb("HttpsDecisionUtil");

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: HttpsDecisionUtil.java */
    /* loaded from: classes.dex */
    public static class a {
        static z a = new z();
    }

    /* compiled from: HttpsDecisionUtil.java */
    /* loaded from: classes.dex */
    private static class b {
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

        /* synthetic */ b(byte b) {
            this();
        }

        public final void a(Context context) {
            if (context != null && this.b <= 0 && Build.VERSION.SDK_INT >= 4) {
                this.b = context.getApplicationContext().getApplicationInfo().targetSdkVersion;
            }
        }

        public final void a(boolean z) {
            this.a = z;
        }

        /* JADX WARN: Removed duplicated region for block: B:19:0x0031 A[RETURN] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final boolean a() {
            /*
                r5 = this;
                boolean r0 = r5.d
                r1 = 1
                if (r0 != 0) goto L32
                int r0 = android.os.Build.VERSION.SDK_INT
                r2 = 28
                r3 = 0
                if (r0 < r2) goto Le
                r0 = 1
                goto Lf
            Le:
                r0 = 0
            Lf:
                boolean r4 = r5.a
                if (r4 == 0) goto L26
                int r4 = r5.b
                if (r4 > 0) goto L1a
                r4 = 28
                goto L1c
            L1a:
                int r4 = r5.b
            L1c:
                if (r4 < r2) goto L20
                r2 = 1
                goto L21
            L20:
                r2 = 0
            L21:
                if (r2 == 0) goto L24
                goto L26
            L24:
                r2 = 0
                goto L27
            L26:
                r2 = 1
            L27:
                if (r0 == 0) goto L2d
                if (r2 == 0) goto L2d
                r0 = 1
                goto L2e
            L2d:
                r0 = 0
            L2e:
                if (r0 == 0) goto L31
                goto L32
            L31:
                return r3
            L32:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.loc.z.b.a():boolean");
        }

        public final void b(boolean z) {
            this.d = z;
        }
    }

    public static z a() {
        return a.a;
    }

    public static String a(String str) {
        if (!TextUtils.isEmpty(str) && !str.startsWith("https")) {
            try {
                Uri.Builder buildUpon = Uri.parse(str).buildUpon();
                buildUpon.scheme("https");
                return buildUpon.build().toString();
            } catch (Throwable unused) {
            }
        }
        return str;
    }

    public static boolean b() {
        return Build.VERSION.SDK_INT == 19;
    }

    public final void a(Context context) {
        if (this.a == null) {
            this.a = new b((byte) 0);
        }
        this.a.a(this.b.a(context, "isTargetRequired"));
        this.a.a(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void a(Context context, boolean z) {
        if (this.a == null) {
            this.a = new b((byte) 0);
        }
        this.b.a(context, "isTargetRequired", z);
        this.a.a(z);
    }

    public final void a(boolean z) {
        if (this.a == null) {
            this.a = new b((byte) 0);
        }
        this.a.b(z);
    }

    public final void b(Context context) {
        this.b.a(context, "isTargetRequired", true);
    }

    public final boolean b(boolean z) {
        byte b2 = 0;
        if (b()) {
            return false;
        }
        if (z) {
            return true;
        }
        if (this.a == null) {
            this.a = new b(b2);
        }
        return this.a.a();
    }
}
