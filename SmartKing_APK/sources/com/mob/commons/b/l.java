package com.mob.commons.b;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.support.v4.os.EnvironmentCompat;
import android.text.TextUtils;
import com.amap.location.common.model.AmapLoc;
import com.mob.commons.b.f;

/* compiled from: Vivo.java */
/* loaded from: classes.dex */
public class l extends f {
    private a c;
    private a d;
    private a e;
    private String f;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: Vivo.java */
    /* loaded from: classes.dex */
    public static class a extends ContentObserver {
        private int a;
        private l b;

        public a(l lVar, int i) {
            super(null);
            this.a = i;
            this.b = lVar;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            if (this.b != null) {
                this.b.a(z, this.a);
            }
        }
    }

    public l(Context context) {
        super(context);
        this.c = null;
        this.d = null;
        this.e = null;
        this.f = "11154";
    }

    private String a(String str, String str2) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getMethod("get", String.class, String.class).invoke(cls, str, EnvironmentCompat.MEDIA_UNKNOWN);
        } catch (Throwable th) {
            c.a().a(th);
            return str2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(boolean z, int i) {
        try {
            String a2 = a(i);
            if (i == 0) {
                a(a2);
            } else if (i == 2) {
                b(a2);
            } else if (i == 1) {
                c(a2);
            }
        } catch (Throwable unused) {
        }
    }

    private String b(int i) {
        switch (i) {
            case 0:
                return com.mob.commons.k.a(95);
            case 1:
                return com.mob.commons.k.a(96) + this.f;
            case 2:
                return com.mob.commons.k.a(97) + this.f;
            default:
                return null;
        }
    }

    private void c(int i) {
        switch (i) {
            case 0:
                if (this.c == null) {
                    this.c = new a(this, 0);
                    this.a.getContentResolver().registerContentObserver(Uri.parse(b(0)), true, this.c);
                    return;
                }
                return;
            case 1:
                if (this.d == null) {
                    this.d = new a(this, 1);
                    this.a.getContentResolver().registerContentObserver(Uri.parse(b(1)), false, this.d);
                    return;
                }
                return;
            case 2:
                if (this.e == null) {
                    this.e = new a(this, 2);
                    this.a.getContentResolver().registerContentObserver(Uri.parse(b(2)), false, this.e);
                    return;
                }
                return;
            default:
                return;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0039, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x004e, code lost:
    
        if (r0 == null) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0037, code lost:
    
        if (r0 != null) goto L41;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r0v1, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r0v4 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.String a(int r10) {
        /*
            r9 = this;
            java.lang.String r0 = r9.b(r10)
            r1 = 0
            if (r0 != 0) goto L8
            return r1
        L8:
            android.net.Uri r3 = android.net.Uri.parse(r0)     // Catch: java.lang.Throwable -> L40 java.lang.Throwable -> L45
            android.content.Context r0 = r9.a     // Catch: java.lang.Throwable -> L40 java.lang.Throwable -> L45
            android.content.ContentResolver r2 = r0.getContentResolver()     // Catch: java.lang.Throwable -> L40 java.lang.Throwable -> L45
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r0 = r2.query(r3, r4, r5, r6, r7)     // Catch: java.lang.Throwable -> L40 java.lang.Throwable -> L45
            if (r0 == 0) goto L37
            boolean r2 = r0.moveToNext()     // Catch: java.lang.Throwable -> L35 java.lang.Throwable -> L52
            if (r2 == 0) goto L37
            java.lang.String r2 = "value"
            int r2 = r0.getColumnIndex(r2)     // Catch: java.lang.Throwable -> L35 java.lang.Throwable -> L52
            java.lang.String r2 = r0.getString(r2)     // Catch: java.lang.Throwable -> L35 java.lang.Throwable -> L52
            if (r0 == 0) goto L31
            r0.close()     // Catch: java.lang.Throwable -> L31
        L31:
            r9.c(r10)     // Catch: java.lang.Throwable -> L34
        L34:
            return r2
        L35:
            r2 = move-exception
            goto L47
        L37:
            if (r0 == 0) goto L3c
        L39:
            r0.close()     // Catch: java.lang.Throwable -> L3c
        L3c:
            r9.c(r10)     // Catch: java.lang.Throwable -> L51
            goto L51
        L40:
            r0 = move-exception
            r8 = r1
            r1 = r0
            r0 = r8
            goto L53
        L45:
            r2 = move-exception
            r0 = r1
        L47:
            com.mob.commons.b.c r3 = com.mob.commons.b.c.a()     // Catch: java.lang.Throwable -> L52
            r3.a(r2)     // Catch: java.lang.Throwable -> L52
            if (r0 == 0) goto L3c
            goto L39
        L51:
            return r1
        L52:
            r1 = move-exception
        L53:
            if (r0 == 0) goto L58
            r0.close()     // Catch: java.lang.Throwable -> L58
        L58:
            r9.c(r10)     // Catch: java.lang.Throwable -> L5b
        L5b:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mob.commons.b.l.a(int):java.lang.String");
    }

    @Override // com.mob.commons.b.f
    protected f.c c() {
        f.c cVar = new f.c();
        cVar.b = a(0);
        cVar.e = a(1);
        cVar.c = a(2);
        if (TextUtils.isEmpty(cVar.c)) {
            cVar.c = i();
        }
        return cVar;
    }

    @Override // com.mob.commons.b.f
    public synchronized boolean h() {
        return AmapLoc.RESULT_TYPE_WIFI_ONLY.equals(a(com.mob.commons.k.a(94), AmapLoc.RESULT_TYPE_GPS));
    }
}
