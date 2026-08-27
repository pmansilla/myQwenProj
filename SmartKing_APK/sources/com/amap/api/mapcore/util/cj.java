package com.amap.api.mapcore.util;

import android.content.Context;
import com.amap.api.mapcore.util.cp;
import com.amap.api.mapcore.util.iu;
import com.amap.api.maps.AMapException;
import com.amap.api.maps.MapsInitializer;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/* compiled from: NetFileFetch.java */
/* loaded from: classes.dex */
public class cj implements iu.a {
    ck a;
    long d;
    ce f;
    a h;
    private Context i;
    private cp j;
    private String k;
    private ja l;
    private cf m;
    long b = 0;
    long c = 0;
    boolean e = true;
    long g = 0;
    private boolean n = false;

    /* compiled from: NetFileFetch.java */
    /* loaded from: classes.dex */
    public interface a {
        void c();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: NetFileFetch.java */
    /* loaded from: classes.dex */
    public static class b extends eq {
        private final String d;

        public b(String str) {
            this.d = str;
        }

        @Override // com.amap.api.mapcore.util.ix
        public String getURL() {
            return this.d;
        }
    }

    public cj(ck ckVar, String str, Context context, cp cpVar) throws IOException {
        this.a = null;
        this.f = ce.a(context.getApplicationContext());
        this.a = ckVar;
        this.i = context;
        this.k = str;
        this.j = cpVar;
        d();
    }

    private void a(long j) {
        if (this.d <= 0 || this.j == null) {
            return;
        }
        this.j.a(this.d, j);
        this.g = System.currentTimeMillis();
    }

    private void c() throws IOException {
        cq cqVar = new cq(this.k);
        cqVar.setConnectionTimeout(1800000);
        cqVar.setSoTimeout(1800000);
        this.l = new ja(cqVar, this.b, this.c, MapsInitializer.getProtocol() == 2);
        this.m = new cf(this.a.b() + File.separator + this.a.c(), this.b);
    }

    private void d() {
        File file = new File(this.a.b() + this.a.c());
        if (!file.exists()) {
            this.b = 0L;
            this.c = 0L;
            return;
        }
        this.e = false;
        this.b = file.length();
        try {
            this.d = g();
            this.c = this.d;
        } catch (IOException unused) {
            if (this.j != null) {
                this.j.a(cp.a.file_io_exception);
            }
        }
    }

    private boolean e() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.a.b());
        sb.append(File.separator);
        sb.append(this.a.c());
        return new File(sb.toString()).length() >= 10;
    }

    private void f() throws AMapException {
        if (hf.a != 1) {
            for (int i = 0; i < 3; i++) {
                try {
                } catch (Throwable th) {
                    ic.c(th, "SiteFileFetch", "authOffLineDownLoad");
                    th.printStackTrace();
                }
                if (hf.a(this.i, fr.e())) {
                    return;
                }
            }
        }
    }

    private long g() throws IOException {
        Map<String, String> map;
        try {
            map = iw.b().b(new b(this.a.a()), MapsInitializer.getProtocol() == 2);
        } catch (hc e) {
            e.printStackTrace();
            map = null;
        }
        int i = -1;
        if (map != null) {
            for (String str : map.keySet()) {
                if ("Content-Length".equalsIgnoreCase(str)) {
                    i = Integer.parseInt(map.get(str));
                }
            }
        }
        return i;
    }

    private void h() {
        long currentTimeMillis = System.currentTimeMillis();
        if (this.a == null || currentTimeMillis - this.g <= 500) {
            return;
        }
        i();
        this.g = currentTimeMillis;
        a(this.b);
    }

    private void i() {
        this.f.a(this.a.e(), this.a.d(), this.d, this.b, this.c);
    }

    public void a() {
        try {
            if (!fr.d(this.i)) {
                if (this.j != null) {
                    this.j.a(cp.a.network_exception);
                    return;
                }
                return;
            }
            f();
            if (hf.a != 1) {
                if (this.j != null) {
                    this.j.a(cp.a.amap_exception);
                    return;
                }
                return;
            }
            if (!e()) {
                this.e = true;
            }
            if (this.e) {
                this.d = g();
                if (this.d == -1) {
                    cm.a("File Length is not known!");
                } else if (this.d == -2) {
                    cm.a("File is not access!");
                } else {
                    this.c = this.d;
                }
                this.b = 0L;
            }
            if (this.j != null) {
                this.j.n();
            }
            if (this.b >= this.c) {
                onFinish();
            } else {
                c();
                this.l.a(this);
            }
        } catch (AMapException e) {
            ic.c(e, "SiteFileFetch", "download");
            if (this.j != null) {
                this.j.a(cp.a.amap_exception);
            }
        } catch (IOException unused) {
            if (this.j != null) {
                this.j.a(cp.a.file_io_exception);
            }
        }
    }

    public void a(a aVar) {
        this.h = aVar;
    }

    public void b() {
        if (this.l != null) {
            this.l.a();
        }
    }

    @Override // com.amap.api.mapcore.util.iu.a
    public void onDownload(byte[] bArr, long j) {
        try {
            this.m.a(bArr);
            this.b = j;
            h();
        } catch (IOException e) {
            e.printStackTrace();
            ic.c(e, "fileAccessI", "fileAccessI.write(byte[] data)");
            if (this.j != null) {
                this.j.a(cp.a.file_io_exception);
            }
            if (this.l != null) {
                this.l.a();
            }
        }
    }

    @Override // com.amap.api.mapcore.util.iu.a
    public void onException(Throwable th) {
        this.n = true;
        b();
        if (this.j != null) {
            this.j.a(cp.a.network_exception);
        }
        if ((th instanceof IOException) || this.m == null) {
            return;
        }
        this.m.a();
    }

    @Override // com.amap.api.mapcore.util.iu.a
    public void onFinish() {
        h();
        if (this.j != null) {
            this.j.o();
        }
        if (this.m != null) {
            this.m.a();
        }
        if (this.h != null) {
            this.h.c();
        }
    }

    @Override // com.amap.api.mapcore.util.iu.a
    public void onStop() {
        if (this.n) {
            return;
        }
        if (this.j != null) {
            this.j.p();
        }
        i();
    }
}
