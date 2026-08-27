package com.amap.api.mapcore.util;

import android.content.Context;
import android.text.TextUtils;

/* compiled from: TimeUpdateStrategy.java */
/* loaded from: classes.dex */
public class jv extends jw {
    private int b;
    private long c;
    private String d;
    private Context e;

    public jv(Context context, int i, String str, jw jwVar) {
        super(jwVar);
        this.b = i;
        this.d = str;
        this.e = context;
    }

    private long a(String str) {
        String a = ia.a(this.e, str);
        if (TextUtils.isEmpty(a)) {
            return 0L;
        }
        return Long.parseLong(a);
    }

    private void a(String str, long j) {
        this.c = j;
        ia.a(this.e, str, String.valueOf(j));
    }

    @Override // com.amap.api.mapcore.util.jw
    public void a(boolean z) {
        super.a(z);
        if (z) {
            a(this.d, System.currentTimeMillis());
        }
    }

    @Override // com.amap.api.mapcore.util.jw
    protected boolean a() {
        if (this.c == 0) {
            this.c = a(this.d);
        }
        return System.currentTimeMillis() - this.c >= ((long) this.b);
    }
}
