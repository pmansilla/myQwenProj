package com.loc;

import android.content.Context;
import android.text.TextUtils;

/* compiled from: TimeUpdateStrategy.java */
/* loaded from: classes.dex */
public final class ce extends cf {
    private int b;
    private long c;
    private String d;
    private Context e;

    public ce(Context context, int i, String str, cf cfVar) {
        super(cfVar);
        this.b = i;
        this.d = str;
        this.e = context;
    }

    @Override // com.loc.cf
    public final void a(boolean z) {
        super.a(z);
        if (z) {
            String str = this.d;
            long currentTimeMillis = System.currentTimeMillis();
            this.c = currentTimeMillis;
            ao.a(this.e, str, String.valueOf(currentTimeMillis));
        }
    }

    @Override // com.loc.cf
    protected final boolean a() {
        if (this.c == 0) {
            String a = ao.a(this.e, this.d);
            this.c = TextUtils.isEmpty(a) ? 0L : Long.parseLong(a);
        }
        return System.currentTimeMillis() - this.c >= ((long) this.b);
    }
}
