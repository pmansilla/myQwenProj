package com.amap.openapi;

import android.content.Context;
import android.os.Looper;
import com.amap.openapi.bi;
import java.util.ArrayList;
import org.apache.commons.lang.time.DateUtils;

/* compiled from: DataManager.java */
/* loaded from: classes.dex */
public class t {
    private static final String a = "t";
    private Looper b;
    private bi<s> c = new bi<>();
    private a d = new a();
    private u e;

    /* compiled from: DataManager.java */
    /* loaded from: classes.dex */
    private class a implements bi.b<s> {
        private a() {
        }

        @Override // com.amap.openapi.bi.b
        public void a() {
        }

        @Override // com.amap.openapi.bi.b
        public void a(ArrayList<s> arrayList) {
            if (arrayList == null || arrayList.size() == 0) {
                return;
            }
            t.this.e.a(arrayList);
        }

        @Override // com.amap.openapi.bi.b
        public boolean a(long j) {
            return t.this.e.a(j);
        }

        @Override // com.amap.openapi.bi.b
        public void b() {
            t.this.e.a();
        }

        @Override // com.amap.openapi.bi.b
        public long c() {
            return 10240L;
        }

        @Override // com.amap.openapi.bi.b
        public long d() {
            return DateUtils.MILLIS_PER_MINUTE;
        }
    }

    public t(Context context, Looper looper) {
        this.b = looper;
        this.e = new u(context);
    }

    public au a(boolean z, int i, long j) {
        return this.e.a(z, i, j);
    }

    public void a() {
        this.c.a(this.d, this.b);
    }

    public void a(int i, byte[] bArr) {
        this.c.a((bi<s>) new s(i, bArr));
    }

    public void a(au auVar) {
        this.e.a(auVar);
    }

    public void b() {
        try {
            this.c.a();
        } catch (Throwable unused) {
        }
    }

    public int c() {
        return this.e.b();
    }

    public int d() {
        return this.e.c();
    }
}
