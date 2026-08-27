package com.amap.openapi;

import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.SparseIntArray;
import com.amap.location.common.network.IHttpClient;
import com.amap.location.uptunnel.core.db.DBProvider;
import com.amap.openapi.bi;
import com.amap.openapi.bj;
import com.loc.fc;
import java.util.concurrent.Executor;

/* compiled from: CountTunnel.java */
/* loaded from: classes.dex */
public class ec {
    private Uri a;
    private DBProvider b;
    private dt c;
    private dp d;
    private IHttpClient e;
    private int f;
    private bj g;
    private bi h;
    private SparseIntArray i = new SparseIntArray();
    private volatile long j;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: CountTunnel.java */
    /* loaded from: classes.dex */
    public class a implements bi.b<c> {
        a() {
        }

        private boolean b(long j) {
            if (e() > 0) {
                j -= r0 * 24;
            }
            long j2 = (j / 24) + (j % 24 > 0 ? 1 : 0);
            if (j2 <= 0) {
                return true;
            }
            try {
                Cursor a = ec.this.b.a(ec.this.a, new String[]{"ID"}, null, null, null, (j2 - 1) + ", 1");
                if (a != null) {
                    try {
                        if (a.moveToFirst()) {
                            return ec.this.b.a(ec.this.a, "ID <= ?", new String[]{String.valueOf(a.getLong(0))}) > 0;
                        }
                    } catch (Exception unused) {
                        return false;
                    } finally {
                        com.amap.location.common.util.e.a(a);
                    }
                }
            } catch (Exception unused2) {
            }
            return false;
        }

        private int e() {
            return ec.this.b.a(ec.this.a, "time < ?", new String[]{String.valueOf(System.currentTimeMillis() - ec.this.d.h())});
        }

        @Override // com.amap.openapi.bi.b
        public void a() {
        }

        /* JADX WARN: Code restructure failed: missing block: B:29:0x00c9, code lost:
        
            if (r13.a.b.a(r13.a.a, r9, "ID = " + r6, null) >= 0) goto L25;
         */
        @Override // com.amap.openapi.bi.b
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void a(java.util.ArrayList<com.amap.openapi.ec.c> r14) {
            /*
                Method dump skipped, instructions count: 292
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.ec.a.a(java.util.ArrayList):void");
        }

        @Override // com.amap.openapi.bi.b
        public boolean a(long j) {
            try {
                long a = (ec.this.b.a(ec.this.a) * 24) + (j * 24);
                if (a > ec.this.d.g()) {
                    return b(a - ec.this.d.g());
                }
                return true;
            } catch (Exception unused) {
                return false;
            }
        }

        @Override // com.amap.openapi.bi.b
        public void b() {
        }

        @Override // com.amap.openapi.bi.b
        public long c() {
            return ec.this.d.c();
        }

        @Override // com.amap.openapi.bi.b
        public long d() {
            return ec.this.d.e();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: CountTunnel.java */
    /* loaded from: classes.dex */
    public class b implements bj.a {
        b() {
        }

        @Override // com.amap.openapi.bj.a
        public Object a(long j) {
            d dVar = new d();
            long j2 = j / 24;
            if (j2 <= 0) {
                return null;
            }
            Cursor b = ec.this.b.b(ec.this.a, dw.a, null, null, null, "0, " + j2);
            if (b != null) {
                try {
                    if (b.getCount() > 0) {
                        fc fcVar = new fc();
                        int a = bk.a(fcVar, ec.this.c.a());
                        int[] iArr = new int[b.getCount()];
                        long j3 = -1;
                        int i = 0;
                        while (b.moveToNext()) {
                            j3 = b.getLong(b.getColumnIndex("ID"));
                            iArr[i] = dx.a(fcVar, b.getInt(b.getColumnIndex("type")), b.getInt(b.getColumnIndex("value")), b.getLong(b.getColumnIndex("time")));
                            i++;
                        }
                        int a2 = dz.a(fcVar, iArr);
                        dz.a(fcVar);
                        dz.a(fcVar, (byte) 0);
                        dz.a(fcVar, a);
                        dz.b(fcVar, a2);
                        dz.d(fcVar, dz.b(fcVar));
                        dVar.a = fcVar.f();
                        dVar.b = j3;
                        dVar.c = r6 * 24;
                        ec.this.j = j3;
                        com.amap.location.common.util.e.a(b);
                        return dVar;
                    }
                } catch (Exception unused) {
                } catch (Throwable th) {
                    com.amap.location.common.util.e.a(b);
                    throw th;
                }
            }
            com.amap.location.common.util.e.a(b);
            return null;
        }

        @Override // com.amap.openapi.bj.a
        public void a() {
        }

        @Override // com.amap.openapi.bj.a
        public void a(int i) {
            ec.this.j = -1L;
        }

        @Override // com.amap.openapi.bj.a
        public void a(int i, Object obj) {
            if (obj instanceof d) {
                ec.this.c.a(ec.this.f, i, ((d) obj).c);
            }
        }

        @Override // com.amap.openapi.bj.a
        public boolean a(Object obj) {
            if (obj instanceof d) {
                return ea.a(ec.this.e, ec.this.c.a(ec.this.f), ((d) obj).a, ec.this.d.f());
            }
            return false;
        }

        @Override // com.amap.openapi.bj.a
        public void b() {
        }

        @Override // com.amap.openapi.bj.a
        public void b(Object obj) {
            if (obj instanceof d) {
                ec.this.b.a(ec.this.a, "ID <= ? ", new String[]{String.valueOf(((d) obj).b)});
            }
        }

        @Override // com.amap.openapi.bj.a
        public boolean b(int i) {
            return ec.this.d.c(i);
        }

        @Override // com.amap.openapi.bj.a
        public long c() {
            try {
                return ec.this.b.a(ec.this.a) * 24;
            } catch (Exception unused) {
                return 0L;
            }
        }

        @Override // com.amap.openapi.bj.a
        public long c(int i) {
            return ec.this.d.b(i) - ec.this.c.a(ec.this.f, i);
        }

        @Override // com.amap.openapi.bj.a
        public int d() {
            return 3;
        }

        @Override // com.amap.openapi.bj.a
        public long d(int i) {
            return ec.this.d.a(i);
        }

        @Override // com.amap.openapi.bj.a
        public long e() {
            return ec.this.d.d();
        }

        @Override // com.amap.openapi.bj.a
        public int f() {
            return ec.this.d.f();
        }

        @Override // com.amap.openapi.bj.a
        public void g() {
            ec.this.j = -1L;
        }

        @Override // com.amap.openapi.bj.a
        public Executor h() {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: CountTunnel.java */
    /* loaded from: classes.dex */
    public static class c implements bi.a {
        private static final c a = new c();

        c() {
        }

        @Override // com.amap.openapi.bi.a
        public long a() {
            return 1L;
        }
    }

    /* compiled from: CountTunnel.java */
    /* loaded from: classes.dex */
    static class d {
        byte[] a;
        long b;
        long c;

        d() {
        }
    }

    public void a() {
        this.h.a();
        this.g.a();
    }

    public void a(int i) {
        this.i.put(i, this.i.get(i) + 1);
        this.d.b();
        this.h.a((bi) c.a);
    }

    public void a(@NonNull dt dtVar, @NonNull dp dpVar, @NonNull IHttpClient iHttpClient, @NonNull Looper looper) {
        this.c = dtVar;
        this.f = 1;
        this.d = new eb(dpVar);
        this.e = iHttpClient;
        this.b = dtVar.b();
        this.a = dt.b(this.f);
        this.h = new bi();
        this.h.a(new a(), looper);
        this.g = new bj();
        this.g.a(dtVar.a(), new b(), looper);
        this.g.a(20000L);
    }

    public void b(int i) {
        if (i != -1) {
            this.d.b();
            this.g.a(20000L);
        }
    }
}
