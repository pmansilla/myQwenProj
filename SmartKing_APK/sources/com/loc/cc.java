package com.loc;

import android.content.Context;
import android.text.TextUtils;

/* compiled from: MobileUpdateStrategy.java */
/* loaded from: classes.dex */
public final class cc extends cf {
    private Context b;
    private boolean c;
    private int d;
    private int e;

    public cc(Context context, boolean z, int i, int i2) {
        this.b = context;
        this.c = z;
        this.d = i;
        this.e = i2;
    }

    @Override // com.loc.cf
    public final void a(int i) {
        if (x.q(this.b) == 1) {
            return;
        }
        String a = ad.a(System.currentTimeMillis(), "yyyyMMdd");
        String a2 = ao.a(this.b, "iKey");
        if (!TextUtils.isEmpty(a2)) {
            String[] split = a2.split("\\|");
            if (split == null || split.length < 2) {
                ao.b(this.b, "iKey");
            } else if (a.equals(split[0])) {
                i += Integer.parseInt(split[1]);
            }
        }
        ao.a(this.b, "iKey", a + "|" + i);
    }

    @Override // com.loc.cf
    protected final boolean a() {
        if (x.q(this.b) == 1) {
            return true;
        }
        if (!this.c) {
            return false;
        }
        String a = ao.a(this.b, "iKey");
        if (TextUtils.isEmpty(a)) {
            return true;
        }
        String[] split = a.split("\\|");
        if (split != null && split.length >= 2) {
            return !ad.a(System.currentTimeMillis(), "yyyyMMdd").equals(split[0]) || Integer.parseInt(split[1]) < this.e;
        }
        ao.b(this.b, "iKey");
        return true;
    }

    @Override // com.loc.cf
    public final int b() {
        int i = (x.q(this.b) == 1 || this.d <= 0) ? Integer.MAX_VALUE : this.d;
        return this.a != null ? Math.max(i, this.a.b()) : i;
    }
}
