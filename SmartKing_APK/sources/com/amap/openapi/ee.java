package com.amap.openapi;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.amap.location.common.network.IHttpClient;
import com.amap.location.uptunnel.core.db.DBProvider;
import com.amap.openapi.bi;
import com.amap.openapi.bj;
import com.litesuits.orm.db.assit.SQLBuilder;
import java.util.ArrayList;
import java.util.concurrent.Executor;

/* compiled from: DataTunnel.java */
/* loaded from: classes.dex */
public class ee {
    private String a = "DataTunnel";
    private String b = null;
    private Uri c;
    private DBProvider d;
    private dt e;
    private dq f;
    private IHttpClient g;
    private int h;
    private bi<c> i;
    private bj j;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DataTunnel.java */
    /* loaded from: classes.dex */
    public class a implements bi.b<c> {
        a() {
        }

        private boolean a(long j, long j2) {
            if (e() > 0) {
                Cursor a = ee.this.d.a(ee.this.c, new String[]{"sum(size)"}, null, null, null);
                if (a != null) {
                    try {
                        if (a.moveToFirst()) {
                            j2 -= j - a.getLong(0);
                        }
                    } catch (Exception unused) {
                        return false;
                    } finally {
                        com.amap.location.common.util.e.a(a);
                    }
                }
                return false;
            }
            SQLiteDatabase c = ee.this.d.c();
            if (c == null) {
                return false;
            }
            while (j2 > 0) {
                try {
                    Cursor rawQuery = c.rawQuery("select sum(size) from (select * from " + ee.this.b + " limit 0, " + ee.this.f.a() + SQLBuilder.PARENTHESES_RIGHT, null);
                    if (rawQuery != null) {
                        try {
                            try {
                                if (rawQuery.moveToFirst()) {
                                    long j3 = rawQuery.getLong(0);
                                    if (j3 <= 0) {
                                        return false;
                                    }
                                    try {
                                        c.execSQL("delete from " + ee.this.b + " where ID < ( select ID from " + ee.this.b + " limit " + ee.this.f.a() + ", 1)");
                                        j2 -= j3;
                                    } catch (Exception unused2) {
                                        return false;
                                    }
                                }
                            } catch (Exception unused3) {
                                return false;
                            }
                        } finally {
                            com.amap.location.common.util.e.a(rawQuery);
                        }
                    }
                } catch (Exception unused4) {
                }
                return false;
            }
            return true;
        }

        private int e() {
            return ee.this.d.a(ee.this.c, "time < ?", new String[]{String.valueOf(System.currentTimeMillis() - ee.this.f.h())});
        }

        @Override // com.amap.openapi.bi.b
        public void a() {
        }

        @Override // com.amap.openapi.bi.b
        public void a(ArrayList<c> arrayList) {
            ContentValues[] contentValuesArr = new ContentValues[arrayList.size()];
            for (int i = 0; i < arrayList.size(); i++) {
                c cVar = arrayList.get(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put("type", Integer.valueOf(cVar.a));
                contentValues.put("time", Long.valueOf(cVar.b));
                contentValues.put("size", Long.valueOf(cVar.a()));
                contentValues.put("value", cVar.c);
                contentValuesArr[i] = contentValues;
            }
            ee.this.d.a(ee.this.c, contentValuesArr);
        }

        @Override // com.amap.openapi.bi.b
        public boolean a(long j) {
            Cursor a = ee.this.d.a(ee.this.c, new String[]{"sum(size)"}, null, null, null);
            if (a != null) {
                try {
                    if (a.moveToFirst()) {
                        long j2 = a.getLong(0);
                        long j3 = j + j2;
                        if (j3 > ee.this.f.g()) {
                            return a(j2, j3 - ee.this.f.g());
                        }
                        com.amap.location.common.util.e.a(a);
                        return true;
                    }
                } catch (Exception unused) {
                    return false;
                } finally {
                    com.amap.location.common.util.e.a(a);
                }
            }
            return false;
        }

        @Override // com.amap.openapi.bi.b
        public void b() {
        }

        @Override // com.amap.openapi.bi.b
        public long c() {
            return ee.this.f.c();
        }

        @Override // com.amap.openapi.bi.b
        public long d() {
            return ee.this.f.e();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DataTunnel.java */
    /* loaded from: classes.dex */
    public class b implements bj.a {
        b() {
        }

        /* JADX WARN: Code restructure failed: missing block: B:30:0x00bd, code lost:
        
            com.amap.location.common.util.e.a(r7);
         */
        @Override // com.amap.openapi.bj.a
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public java.lang.Object a(long r27) {
            /*
                Method dump skipped, instructions count: 281
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.amap.openapi.ee.b.a(long):java.lang.Object");
        }

        @Override // com.amap.openapi.bj.a
        public void a() {
        }

        @Override // com.amap.openapi.bj.a
        public void a(int i) {
        }

        @Override // com.amap.openapi.bj.a
        public void a(int i, Object obj) {
            if (obj instanceof d) {
                ee.this.e.a(ee.this.h, i, ((d) obj).c);
            }
        }

        @Override // com.amap.openapi.bj.a
        public boolean a(Object obj) {
            if (obj instanceof d) {
                return ea.a(ee.this.g, ee.this.e.a(ee.this.h), ((d) obj).a, ee.this.f.f());
            }
            return false;
        }

        @Override // com.amap.openapi.bj.a
        public void b() {
        }

        @Override // com.amap.openapi.bj.a
        public void b(Object obj) {
            if (obj instanceof d) {
                ee.this.d.a(ee.this.c, "ID <= ? ", new String[]{String.valueOf(((d) obj).b)});
            }
        }

        @Override // com.amap.openapi.bj.a
        public boolean b(int i) {
            return ee.this.f.c(i);
        }

        @Override // com.amap.openapi.bj.a
        public long c() {
            Cursor a = ee.this.d.a(ee.this.c, new String[]{"sum(size)"}, null, null, null);
            long j = 0;
            if (a != null) {
                try {
                    if (a.moveToFirst()) {
                        j = a.getLong(0);
                    }
                } catch (Exception unused) {
                } catch (Throwable th) {
                    com.amap.location.common.util.e.a(a);
                    throw th;
                }
            }
            com.amap.location.common.util.e.a(a);
            return j;
        }

        @Override // com.amap.openapi.bj.a
        public long c(int i) {
            return ee.this.f.b(i) - ee.this.e.a(ee.this.h, i);
        }

        @Override // com.amap.openapi.bj.a
        public int d() {
            return 3;
        }

        @Override // com.amap.openapi.bj.a
        public long d(int i) {
            return ee.this.f.a(i);
        }

        @Override // com.amap.openapi.bj.a
        public long e() {
            return ee.this.f.d();
        }

        @Override // com.amap.openapi.bj.a
        public int f() {
            return ee.this.f.f();
        }

        @Override // com.amap.openapi.bj.a
        public void g() {
        }

        @Override // com.amap.openapi.bj.a
        public Executor h() {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: DataTunnel.java */
    /* loaded from: classes.dex */
    public static class c implements bi.a {
        int a;
        long b;
        byte[] c;

        c() {
        }

        @Override // com.amap.openapi.bi.a
        public long a() {
            return (this.c == null ? 0 : this.c.length) + 24;
        }
    }

    /* compiled from: DataTunnel.java */
    /* loaded from: classes.dex */
    static class d {
        byte[] a;
        long b;
        long c;

        d() {
        }
    }

    public void a() {
        this.i.a();
        this.j.a();
    }

    public void a(int i) {
        if (i != -1) {
            this.f.b();
            this.j.a(20000L);
        }
    }

    public void a(int i, byte[] bArr) {
        this.f.b();
        c cVar = new c();
        cVar.a = i;
        cVar.b = System.currentTimeMillis();
        cVar.c = bArr;
        this.i.a((bi<c>) cVar);
    }

    public void a(@NonNull dt dtVar, @NonNull dq dqVar, @NonNull IHttpClient iHttpClient, int i, @NonNull Looper looper) {
        this.b = dt.c(i);
        this.a += this.b;
        this.e = dtVar;
        this.h = i;
        this.f = new ed(dqVar);
        this.g = iHttpClient;
        this.d = dtVar.b();
        this.c = dt.b(i);
        this.i = new bi<>();
        this.j = new bj();
        this.i.a(new a(), looper);
        this.j.a(dtVar.a(), new b(), looper);
        this.j.a(20000L);
    }

    public void b() {
        this.i.b();
    }
}
