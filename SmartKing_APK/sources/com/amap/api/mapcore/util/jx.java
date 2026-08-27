package com.amap.api.mapcore.util;

import android.content.Context;

/* compiled from: WiFiUplateStrategy.java */
/* loaded from: classes.dex */
public class jx extends jw {
    private Context b;
    private boolean c;

    public jx(Context context, boolean z) {
        this.c = false;
        this.b = context;
        this.c = z;
    }

    @Override // com.amap.api.mapcore.util.jw
    protected boolean a() {
        return hi.q(this.b) == 1 || this.c;
    }
}
