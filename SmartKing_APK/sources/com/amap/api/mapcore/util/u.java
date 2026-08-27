package com.amap.api.mapcore.util;

import android.content.Context;
import android.text.TextUtils;
import com.amap.api.mapcore.util.iu;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Map;

/* compiled from: AuthTaskDownload.java */
/* loaded from: classes.dex */
public class u implements iu.a {
    a a;
    private final Context b;
    private RandomAccessFile c;
    private ja d;
    private String e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AuthTaskDownload.java */
    /* loaded from: classes.dex */
    public static class a {
        protected String a;
        protected String b;
        protected String c;
        protected String d;
        protected c e;

        public a(String str, String str2, String str3) {
            this.a = str;
            this.b = str2;
            this.c = str3 + ".tmp";
            this.d = str3;
        }

        public String a() {
            return this.a;
        }

        public void a(c cVar) {
            this.e = cVar;
        }

        public String b() {
            return this.b;
        }

        public String c() {
            return this.c;
        }

        public String d() {
            return this.d;
        }

        public c e() {
            return this.e;
        }
    }

    /* compiled from: AuthTaskDownload.java */
    /* loaded from: classes.dex */
    static class b extends eq {
        private final a d;

        b(a aVar) {
            this.d = aVar;
        }

        @Override // com.amap.api.mapcore.util.eq, com.amap.api.mapcore.util.ix
        public Map<String, String> getParams() {
            return null;
        }

        @Override // com.amap.api.mapcore.util.eq, com.amap.api.mapcore.util.ix
        public Map<String, String> getRequestHead() {
            return null;
        }

        @Override // com.amap.api.mapcore.util.ix
        public String getURL() {
            if (this.d != null) {
                return this.d.a();
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: AuthTaskDownload.java */
    /* loaded from: classes.dex */
    public static class c {
        protected String a;
        protected String b;

        public c(String str, String str2) {
            this.a = str;
            this.b = str2;
        }

        public String a() {
            return this.a;
        }

        public String b() {
            return this.b;
        }

        public boolean c() {
            return (TextUtils.isEmpty(this.a) || TextUtils.isEmpty(this.b)) ? false : true;
        }
    }

    /* compiled from: AuthTaskDownload.java */
    /* loaded from: classes.dex */
    static class d extends a {
        public d(String str, String str2, String str3) {
            super(str, str2, str3);
        }

        public void a(String str, String str2) {
            a(new c(str, str2));
        }
    }

    public u(Context context, a aVar, ho hoVar) {
        this.b = context.getApplicationContext();
        if (aVar == null) {
            return;
        }
        this.a = aVar;
        this.d = new ja(new b(aVar));
        this.e = aVar.c();
    }

    private boolean b() {
        c e = this.a.e();
        return (e != null && e.c() && fh.a(this.b, e.a(), e.b(), "").equalsIgnoreCase(this.a.b())) ? false : true;
    }

    public void a() {
        try {
            if (!b() || this.d == null) {
                return;
            }
            this.d.a(this);
        } catch (Throwable th) {
            ic.c(th, "AuthTaskDownload", "startDownload()");
        }
    }

    @Override // com.amap.api.mapcore.util.iu.a
    public void onDownload(byte[] bArr, long j) {
        try {
            if (this.c == null) {
                File file = new File(this.e);
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                this.c = new RandomAccessFile(file, "rw");
            }
            this.c.seek(j);
            this.c.write(bArr);
        } catch (Throwable th) {
            ic.c(th, "AuthTaskDownload", "onDownload()");
        }
    }

    @Override // com.amap.api.mapcore.util.iu.a
    public void onException(Throwable th) {
        try {
            if (this.c == null) {
                return;
            }
            this.c.close();
        } catch (Throwable th2) {
            ic.c(th2, "AuthTaskDownload", "onException()");
        }
    }

    @Override // com.amap.api.mapcore.util.iu.a
    public void onFinish() {
        try {
        } catch (Throwable th) {
            ic.c(th, "AuthTaskDownload", "onFinish()");
        }
        if (this.c == null) {
            return;
        }
        try {
            this.c.close();
        } catch (Throwable th2) {
            ic.c(th2, "AuthTaskDownload", "onFinish3");
        }
        String b2 = this.a.b();
        String a2 = hl.a(this.e);
        if (a2 == null || !b2.equalsIgnoreCase(a2)) {
            try {
                new File(this.e).delete();
                return;
            } catch (Throwable th3) {
                ic.c(th3, "AuthTaskDownload", "onFinish");
                return;
            }
        }
        String d2 = this.a.d();
        try {
            cg cgVar = new cg();
            File file = new File(this.e);
            cgVar.a(file, new File(d2), -1L, cm.a(file), null);
            c e = this.a.e();
            if (e != null && e.c()) {
                fh.a(this.b, e.a(), e.b(), (Object) a2);
            }
            new File(this.e).delete();
            return;
        } catch (Throwable th4) {
            ic.c(th4, "AuthTaskDownload", "onFinish1");
            return;
        }
        ic.c(th, "AuthTaskDownload", "onFinish()");
    }

    @Override // com.amap.api.mapcore.util.iu.a
    public void onStop() {
    }
}
