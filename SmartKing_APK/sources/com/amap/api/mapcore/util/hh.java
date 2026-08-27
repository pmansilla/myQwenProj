package com.amap.api.mapcore.util;

import android.content.Context;

/* compiled from: CoordinatorSoDownloader.java */
@Deprecated
/* loaded from: classes.dex */
public class hh extends hn {
    private boolean a;
    private boolean b;

    public hh(Context context, String str, String str2, String str3) {
        super(context, str, str2, str3);
        this.a = false;
        this.b = true;
    }

    @Override // com.amap.api.mapcore.util.hn
    public void a() {
    }

    public void a(boolean z) {
        this.b = z;
    }

    @Override // com.amap.api.mapcore.util.hn, com.amap.api.mapcore.util.iu.a
    public void onFinish() {
    }
}
