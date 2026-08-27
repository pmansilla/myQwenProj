package com.loc;

import android.content.Context;

/* compiled from: WiFiUplateStrategy.java */
/* loaded from: classes.dex */
public final class cg extends cf {
    private Context b;
    private boolean c;

    public cg(Context context) {
        this.c = false;
        this.b = context;
        this.c = false;
    }

    @Override // com.loc.cf
    protected final boolean a() {
        return x.q(this.b) == 1 || this.c;
    }
}
