package com.amap.api.mapcore.util;

import android.content.Context;
import android.text.TextUtils;

/* compiled from: MobileUpdateStrategy.java */
/* loaded from: classes.dex */
public class jt extends jw {
    private Context b;
    private boolean c;
    private int d;
    private int e;

    public jt(Context context, boolean z, int i, int i2) {
        this.b = context;
        this.c = z;
        this.d = i;
        this.e = i2;
    }

    @Override // com.amap.api.mapcore.util.jw
    public void a(int i) {
        if (hi.q(this.b) == 1) {
            return;
        }
        String a = hp.a(System.currentTimeMillis(), "yyyyMMdd");
        String a2 = ia.a(this.b, "iKey");
        if (!TextUtils.isEmpty(a2)) {
            String[] split = a2.split("\\|");
            if (split == null || split.length < 2) {
                ia.b(this.b, "iKey");
            } else if (a.equals(split[0])) {
                i += Integer.parseInt(split[1]);
            }
        }
        ia.a(this.b, "iKey", a + "|" + i);
    }

    @Override // com.amap.api.mapcore.util.jw
    protected boolean a() {
        if (hi.q(this.b) == 1) {
            return true;
        }
        if (!this.c) {
            return false;
        }
        String a = ia.a(this.b, "iKey");
        if (TextUtils.isEmpty(a)) {
            return true;
        }
        String[] split = a.split("\\|");
        if (split != null && split.length >= 2) {
            return !hp.a(System.currentTimeMillis(), "yyyyMMdd").equals(split[0]) || Integer.parseInt(split[1]) < this.e;
        }
        ia.b(this.b, "iKey");
        return true;
    }

    @Override // com.amap.api.mapcore.util.jw
    public int b() {
        int i = (hi.q(this.b) == 1 || this.d <= 0) ? Integer.MAX_VALUE : this.d;
        return this.a != null ? Math.max(i, this.a.b()) : i;
    }
}
